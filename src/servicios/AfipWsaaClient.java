package servicios;

import main.Main;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.rpc.ParameterMode;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.Base64;
import org.apache.axis.encoding.XMLType;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertStoreException;
import java.security.cert.CertificateException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.rpc.ServiceException;
import main.FacElectronicaException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.bouncycastle.cms.CMSException;
import util.ClienteNTP;

public class AfipWsaaClient {

    //--------------------------------------------------------//
    // ACA DECLARO LAS PROPIEDADES QUE NECESITO PARA LA CLASE //
    //--------------------------------------------------------//    
    private final static Logger log = Logger.getLogger(AfipWsaaClient.class);
    //--------------------------------------------------------//
    
    public AfipWsaaClient(){
        // Inicio las propiedades del logger
        PropertyConfigurator.configure("log4j.properties");    
    }
    
    public static String invoke_wsaa(byte[] LoginTicketRequest_xml_cms, String endpoint) throws Exception {

        String LoginTicketResponse = null;
        
        try {

            Service service = new Service();
            Call call = (Call) service.createCall();

            //
            // Prepare the call for the Web service
            //
            call.setTargetEndpointAddress(new java.net.URL(endpoint));
            call.setOperationName("loginCms");
            call.addParameter("request", XMLType.XSD_STRING, ParameterMode.IN);
            call.setReturnType(XMLType.XSD_STRING);
            
            // Valido si tengo que generar el log
            if (Main.config.getWs_log()) {
                log.info("WSAA Url= " + endpoint);            
            }

            //
            // Make the actual call and assign the answer to a String
            //
            LoginTicketResponse = (String) call.invoke(new Object[]{
                Base64.encode(LoginTicketRequest_xml_cms)});

        } catch (ServiceException | MalformedURLException | RemoteException e) {
            
            // Genero error en el log
            log.error("Error invocando WSAA: " + e.getMessage());
            
            // Lanzo un error
            throw new FacElectronicaException("Error invocando WSAA: " + e.getMessage());

        }
        return (LoginTicketResponse);
    }

    //
    // Create the CMS Message
    //
    public static byte[] create_cms(String p12file, String p12pass, String signer, String dstDN, String service, Long TicketTime) throws FacElectronicaException, Exception {

        PrivateKey pKey = null;
        X509Certificate pCertificate = null;
        byte[] asn1_cms = null;
        CertStore cstore = null;
        String LoginTicketRequest_xml;
        String SignerDN = null;

        //
        // Manage Keys & Certificates
        //
        try {
            // Create a keystore using keys from the pkcs#12 p12file
            KeyStore ks = KeyStore.getInstance("pkcs12");
            FileInputStream p12stream = new FileInputStream(p12file);
            ks.load(p12stream, p12pass.toCharArray());
            p12stream.close();

            // Get Certificate & Private key from KeyStore
            pKey = (PrivateKey) ks.getKey(signer, p12pass.toCharArray());
            pCertificate = (X509Certificate) ks.getCertificate(signer);
            SignerDN = pCertificate.getSubjectDN().toString();

            // Create a list of Certificates to include in the final CMS
            ArrayList<X509Certificate> certList = new ArrayList<>();
            certList.add(pCertificate);

            if (Security.getProvider("BC") == null) {
                Security.addProvider(new BouncyCastleProvider());
            }

            cstore = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certList), "BC");
            
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | InvalidAlgorithmParameterException | NoSuchProviderException e) {
            
            // Lanzo un error
            throw new FacElectronicaException("Error intentando acceder a los archivos pk12 " + e.getMessage());

        }

        //
        // Create XML Message
        // 
        LoginTicketRequest_xml = create_LoginTicketRequest(SignerDN, dstDN, service, TicketTime);

        //
        // Create CMS Message
        //
        try {
            // Create a new empty CMS Message
            CMSSignedDataGenerator gen = new CMSSignedDataGenerator();

            // Add a Signer to the Message
            gen.addSigner(pKey, pCertificate, CMSSignedDataGenerator.DIGEST_SHA1);

            // Add the Certificate to the Message
            gen.addCertificatesAndCRLs(cstore);

            // Add the data (XML) to the Message
            CMSProcessable data = new CMSProcessableByteArray(LoginTicketRequest_xml.getBytes());

            // Add a Sign of the Data to the Message
            CMSSignedData signed = gen.generate(data, true, "BC");

            // 
            asn1_cms = signed.getEncoded();
        } catch (IllegalArgumentException | CertStoreException | CMSException | NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
                        
            // Lanzo un error
            throw new FacElectronicaException("Error creando mensaje CMS: " + e.getMessage());
        }

        return (asn1_cms);
    }

    //
    // Create XML Message for AFIP wsaa
    // 	
    public static String create_LoginTicketRequest(String SignerDN, String dstDN, String service, Long TicketTime) throws FacElectronicaException, Exception {

        String LoginTicketRequest_xml;
        XMLGregorianCalendar XMLGenTime = null;
        XMLGregorianCalendar XMLExpTime = null;
        String UniqueId = "";
        GregorianCalendar gentime;
        GregorianCalendar exptime = new GregorianCalendar();

        //Toma hora via servidor NTP
        ClienteNTP clienteNtp = new ClienteNTP();
        
        // Valido que el servidor NTP contenga algo
        if(Main.config.getServidorNTP1() != null){
            clienteNtp.getListaServidores().add(Main.config.getServidorNTP1());        
        }

        // Valido que el servidor NTP contenga algo
        if(Main.config.getServidorNTP2() != null){
            clienteNtp.getListaServidores().add(Main.config.getServidorNTP2());
        }        

        
        // Capturo posibles errores
        try {
            
            gentime = clienteNtp.getFechaServidor();
            exptime.setTime(new Date(clienteNtp.getFechaServidorFormatoDate().getTime() + TicketTime));
            XMLGenTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(gentime);
            XMLExpTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(exptime);
            
            // Genero el unique ID
            UniqueId = Long.toString(clienteNtp.getFechaServidorFormatoDate().getTime() / 1000);
            
        } catch (FacElectronicaException | DatatypeConfigurationException e) {
            
            // Lanzo un error
            throw new FacElectronicaException("Error al solicitar ticket: " + e.getMessage());
        }
        
        // Valido si tengo que mostrar el error
        if (Main.config.getApp_log()) {
            log.info("GenTime: " + XMLGenTime);
            log.info("ExpTime: " + XMLExpTime);
        }

        LoginTicketRequest_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<loginTicketRequest version=\"1.0\">"
                + "<header>"
                + "<source>" + SignerDN + "</source>"
                + "<destination>" + dstDN + "</destination>"
                + "<uniqueId>" + UniqueId + "</uniqueId>"
                + "<generationTime>" + XMLGenTime + "</generationTime>"
                + "<expirationTime>" + XMLExpTime + "</expirationTime>"
                + "</header>"
                + "<service>" + service + "</service>"
                + "</loginTicketRequest>";

        //System.out.println("TRA: " + LoginTicketRequest_xml);
        return (LoginTicketRequest_xml);
    }
}