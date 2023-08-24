package controladores;

import accesoDB.ConexionSyBase;
import entidades.EntidadTicket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import entidades.Entidad;
import java.io.Reader;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.logging.Level;
import main.FacElectronicaException;
import main.Main;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import wsAFIP.FEAuthRequest;
import servicios.AfipWsaaClient;

/**
 * Esta clase se creo para gestionar los tickets de acceso al Web services
 *
 * @author Matias Panasci Alias Panic
 */
public class ControladorTickets extends ConexionSyBase implements IPersistencia {

    //-----------------------------------------//
    // ACA DECLARO LAS PROPIEDADES DE LA CLASE //
    //-----------------------------------------//
    private final static Logger log = Logger.getLogger(ControladorTickets.class);
    private String trustStoreFile;
    private String trustStorePwd;
    private String keyStoreFile;
    private String keyStoreSigner;
    private String keyStorePwd;
    private Long keyStoreTimeOut;
    private String destino;
    private String servicio;
    private String endPointLogin;
    private String endPointWS;
    private Long cuit;
    //-----------------------------------------//    
    
    /**
     * Metodo constructor de la clase
     *
     * @throws Exception
     */
    public ControladorTickets()throws Exception {
        
        // Mantengo Herencia
        super();

        // Declaro variable auxiliar
        String sTest= ( this.getWs_test() ? "S": "N" );
        
        // Capturo posibles errores
        try {

            // Inicio las propiedades del logger
            PropertyConfigurator.configure("log4j.properties");

            // Esta consulta esta para cuando funcionen todos los servicios con una sola tabla
            //String sentenciaSQL = "SELECT * FROM afipws_wsaa_TRA_java WHERE TestSN = ? AND servicio = ?";            
            
            // Creo la consulta SQL            
            String sentenciaSQL = "SELECT * FROM afipws_wsaa_TRA_java WHERE TestSN = ?";

            // Instancio el objeto que me permite ejecutar consultas
            PreparedStatement sentencia = crearSentencia(sentenciaSQL);

            // Seteo el parametro para traerme los datos de homologacion            
            sentencia.setString(1, sTest);
            //sentencia.setString(2, Main.config.getWs_nombre());

            // Preguntos si esta activado el log de la bd
            if(this.getDb_log()){
                
                // Genero salida en la consola
                log.info(sentenciaSQL);
                log.info("Parametro 1: " + sTest);
                //log.info("Parametro 2: " + Main.config.getWs_nombre());
            }
            
            // Ejecuto la consulta
            ResultSet filasConsulta = consultar(sentencia);

            // Recorro la consulta
            while (filasConsulta.next()) {

                // Seteo las propiedades
                this.trustStoreFile = filasConsulta.getString("trustStoreFile");
                this.trustStorePwd = filasConsulta.getString("trustStorePwd");
                this.keyStoreFile = filasConsulta.getString("keyStoreFile");
                this.keyStoreSigner = filasConsulta.getString("keyStoreSigner");
                this.keyStorePwd = filasConsulta.getString("keyStorePwd");
                this.keyStoreTimeOut = filasConsulta.getLong("keyStoreTimeOut");
                this.destino = filasConsulta.getString("destino");
                //this.servicio = filasConsulta.getString("servicio");
                this.servicio = Main.config.getWs_nombre();
                this.endPointLogin = filasConsulta.getString("endpoint");
            }

            // Preguntos si esta activado el log de la bd
            if(this.getDb_log()){
                
                // Genero salida en la consola
                log.info(sentenciaSQL + " - Valor Parametro: " + sTest);
            }
            
            // Creo la consulta SQL para obtener el cuit
            sentenciaSQL = "SELECT emp_cuit FROM Empresas WHERE emp_codigo = ?";

            // Instancio el objeto que me permite ejecutar consultas
            sentencia = crearSentencia(sentenciaSQL);

            // Seteo el parametro para traerme el cuit de la empresa
            sentencia.setInt(1, 1);

            // Ejecuto la consulta
            filasConsulta = consultar(sentencia);

            // Recorro la consulta
            while (filasConsulta.next()) {
                this.cuit = filasConsulta.getLong("emp_cuit");
            }

            // Preguntos si esta activado el log de la bd
            if(this.getDb_log()){
                
                // Genero salida en la consola
                log.info(sentenciaSQL + " - Valor Parametro: " + 1);
                
            }
            
            // Creo la consulta SQL para obtener el URL del servicio WEB
            sentenciaSQL = "SELECT * FROM afipws_wsfe WHERE TestSN = ? AND servicio = ?";

            // Instancio el objeto que me permite ejecutar consultas
            sentencia = crearSentencia(sentenciaSQL);

            // Seteo el parametro para traerme los datos de homologacion            
            sentencia.setString(1, sTest);
            sentencia.setString(2, Main.config.getWs_nombre());

            // Ejecuto la consulta
            filasConsulta = consultar(sentencia);

            // Recorro la consulta
            if (filasConsulta.next()) {
                this.endPointWS = filasConsulta.getString("url");
            } else {
           
                // Disparo error por que esta vacio el endpoint del Web services
                throw new FacElectronicaException("ERROR: No se encontro endpoint del Web services para el servicio: " + Main.config.getWs_nombre());
            }
            
            // Preguntos si esta activado el log de la bd
            if(this.getDb_log()){
                
                // Genero salida en la consola
                log.info(sentenciaSQL);
                log.info("Parametro 1: " + sTest);
                log.info("Parametro 2: " + Main.config.getWs_nombre());
                
            }
            
        } catch (Exception e) {
            
            // Genero salida en el log
            log.error("ATENCIÓN: no se pudo leer la tabla de tickets de acceso: " + e.getMessage() + " debe validar la base de datos..");            
        }
    }

    /**
     * Este metodo devuelve un ticket de acceso activo para el servicio web
     *
     * @return
     * @throws main.FacElectronicaException
     */
    public FEAuthRequest getTicketActivo() throws FacElectronicaException {

        // Instancio el ticket vacio
        FEAuthRequest ticketActivo = new FEAuthRequest();

        // Capturo posibles errores
        try {

            // Obtengo el tiket activo
            EntidadTicket t = (EntidadTicket) this.buscarActivo();

            // Si encontre un ticket
            if (t != null) {

                // Relleno el ticket actual
                ticketActivo.setSign(t.getSign());
                ticketActivo.setToken(t.getToken());
                ticketActivo.setCuit(this.cuit);

                // Genero salida en el log
                log.info("Reutilizando ticket activo");
                
            } else {

                // Si no encontre nada, genero uno nuevo
                ticketActivo = this.generarTicketNuevo();

                // Genero salida en el log
                log.info("Ticket nuevo generado con exito!!!!");
            }

            // Devuelvo el resultado
            return ticketActivo;

        } catch (Exception e) {
            
            // Lanzo un error
            throw new FacElectronicaException("Error generando ticket Nuevo > " + e.getMessage());            
        }

    }

    /**
     * Este metodo inserta guarda la entidad en la base de datos
     *
     * @param objeto
     * @return
     * @throws Exception
     */
    @Override
    public int insertar(Entidad objeto) throws Exception {

        // Armo la instruccion SQL
        //String instruccionSQL = "INSERT INTO afipws_fe_wsaa_TA VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";  --> Para cuando usa el servicio      
        String instruccionSQL = "INSERT INTO afipws_fe_wsaa_TA VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        // Instancio el objeto que me permite ejecutar la instruccion
        PreparedStatement sentencia = crearSentencia(instruccionSQL);

        // Casteo el objeto como entidad
        EntidadTicket entidad = (EntidadTicket) objeto;

        // Completo los datos del objeto
        sentencia.setLong(1, entidad.getUid());
        sentencia.setString(2, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(entidad.getGenTime()));
        sentencia.setString(3, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(entidad.getExpTime()));
        sentencia.setString(4, entidad.getToken());
        sentencia.setString(5, entidad.getSign());
        sentencia.setString(6, entidad.getSource());
        sentencia.setString(7, entidad.getDestination());
        sentencia.setString(8, "JAVA");
        sentencia.setString(9, entidad.getActivo());
        sentencia.setString(10, entidad.getTestSN());
        sentencia.setString(11, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        //sentencia.setString(12, Main.config.getWs_nombre());

        // Preguntos si esta activado el log de la bd
        if(this.getDb_log()){

            // Genero salida en la consola
            log.info(instruccionSQL);
            log.info("Parametro 1: " + entidad.getUid());
            log.info("Parametro 2: " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(entidad.getGenTime()));
            log.info("Parametro 3: " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(entidad.getExpTime()));
            log.info("Parametro 4: " + entidad.getToken());
            log.info("Parametro 5: " + entidad.getSign());
            log.info("Parametro 6: " + entidad.getSource());
            log.info("Parametro 7: " + entidad.getDestination());
            log.info("Parametro 8: " + "JAVA");
            log.info("Parametro 9: " + entidad.getActivo());
            log.info("Parametro 10: " + entidad.getTestSN());
            log.info("Parametro 11: " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
            //log.info("Parametro 12: " + Main.config.getWs_nombre());            
           
        }   
        
        // Ejecuto la instruccion
        return actualizar(sentencia);
    }

    @Override
    public int borrar(String condicion) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int modificar(Entidad objeto) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Este metodo de devuelve el ticket activo de la base de datos
     *
     * @return
     * @throws Exception
     */
    private EntidadTicket buscarActivo() throws Exception {

        // Instancio variables auxiliares
        java.sql.Timestamp ahora = new java.sql.Timestamp(java.util.Calendar.getInstance().getTime().getTime());        
        EntidadTicket ticketActivo = null;

        // Esta consulta esta para cuando funcionen todos los serivicios con una sola tabla
        //String instruccionSQL = "UPDATE afipws_fe_wsaa_TA SET activo = 'N' WHERE ultActualizacion < ? AND servicio = ?";
        
        // Armo la instruccion SQL        
        String instruccionSQL = "UPDATE afipws_fe_wsaa_TA SET activo = 'N' WHERE exptime < ? ";

        // Instancio el objeto que me permite ejecutar la instruccion
        PreparedStatement sentencia = crearSentencia(instruccionSQL);
        sentencia.setTimestamp(1, ahora);
        //sentencia.setString(2, Main.config.getWs_nombre());
        
        // Preguntos si esta activado el log de la bd
        if(this.getDb_log()){

            // Genero salida en la consola
            log.info(instruccionSQL);
            log.info("Parametro 1: " + ahora);
            //log.info("Parametro 2: " + Main.config.getWs_nombre());            
        }  

        // Marco los ticket vencidos como inactivos
        this.actualizar(sentencia);
        
        // Esta consulta esta para cuando funcionen todos los serivicios con una sola tabla        
        //instruccionSQL = "SELECT * FROM afipws_fe_wsaa_TA WHERE activo = 'S' AND servicio = ?";
        
        // Armo la instruccion SQL
        instruccionSQL = "SELECT * FROM afipws_fe_wsaa_TA WHERE activo = 'S'";

        // Instancio el objeto que me permite ejecutar la instruccion
        sentencia = crearSentencia(instruccionSQL);
        //sentencia.setString(1, Main.config.getWs_nombre());
        
        // Preguntos si esta activado el log de la bd
        if(this.getDb_log()){

            // Genero salida en la consola
            log.info(instruccionSQL);
            //log.info("Parametro 1: " + Main.config.getWs_nombre());                        
        } 
        
        // Ejecuto la consulta
        ResultSet rdo = consultar(sentencia);

        // Recorro el resultado de la consulta
        while (rdo.next()) {
            ticketActivo = new EntidadTicket(rdo.getLong("uid"),
                    rdo.getDate("genTime"),
                    rdo.getDate("expTime"),
                    rdo.getString("token"),
                    rdo.getString("sign"),
                    rdo.getString("source"),
                    rdo.getString("destination"),
                    rdo.getString("activo"),
                    rdo.getString("TestSN"));
        }

        // Devuelvo el resultado
        return ticketActivo;
    }

    /**
     * Este metodo genera un ticket de acceso al Web services nuevo
     *
     * @return ticket AFIP
     * @throws main.FacElectronicaException
     */
    private FEAuthRequest generarTicketNuevo() throws FacElectronicaException{

        // Capturo posibles errores
        try {
            
            // Creo una variable de tipo String que almacenara la respuesta
            String LoginTicketResponse;
            
            // Creo el ticket que voy a enviar al Web Services
            byte[] LoginTicketRequest_xml_cms = AfipWsaaClient.create_cms(this.keyStoreFile,
                    this.keyStorePwd,
                    this.keyStoreSigner,
                    this.destino,
                    this.servicio,
                    this.keyStoreTimeOut);
            
            // Envio la solicitud al Web Services de autenticacion
            LoginTicketResponse = AfipWsaaClient.invoke_wsaa(LoginTicketRequest_xml_cms, this.endPointLogin);
            
            // Instancio las objetos que me permiten leer la respuesta
            Reader tokenReader = new StringReader(LoginTicketResponse);
            Document tokenDoc = new SAXReader(false).read(tokenReader);
            
            //Intancio un ticket AFIP y lo relleno con los datos
            FEAuthRequest ticketAFIP = new FEAuthRequest();
            
            ticketAFIP.setCuit(this.cuit);
            ticketAFIP.setToken(tokenDoc.valueOf("/loginTicketResponse/credentials/token"));
            ticketAFIP.setSign(tokenDoc.valueOf("/loginTicketResponse/credentials/sign"));
            
            // Genero un ticket nuevo
            EntidadTicket ticketActual = new EntidadTicket(Long.parseLong(tokenDoc.valueOf("/loginTicketResponse/header/uniqueId")),
                    tokenDoc.valueOf("/loginTicketResponse/header/generationTime"),
                    tokenDoc.valueOf("/loginTicketResponse/header/expirationTime"),
                    ticketAFIP.getToken(),
                    ticketAFIP.getSign(),
                    tokenDoc.valueOf("/loginTicketResponse/header/source"),
                    tokenDoc.valueOf("/loginTicketResponse/header/destination"));
            
            // Guardo el ticket
            this.insertar(ticketActual);
            
            // Devuelvo el ticket activo
            return ticketAFIP;

        } catch (Exception e) {
            
            // Genero error en el log
            log.error("No se pudo generar el ticket de acceso > " + e.getMessage());
            
            // Lanzo un error
            throw new FacElectronicaException("No se pudo generar el ticket de acceso > " + e.getMessage());            

        }
    }

    /**
     * Metodo que otorga visibilidad a la URL del servicio Web para la obtencion de ticket
     * 
     * @return 
     */
    public String getEndPointLogin() {
        return endPointLogin;
    }

    /**
     * Metodo que otorga visibilidad a la URL del servicio Web para la obtencion de ticket
     * 
     * @param endpoint 
     */
    public void setEndPointLogin(String endpoint) {
        this.endPointLogin = endpoint;
    }

    /**
     * Metodo que otorga visibilidad a la URL del servicio Web de facturacion
     *
     * @return the value of endPointWS
     */
    public String getEndPointWS() {
        return endPointWS;
    }

    /**
     * Metodo que otorga visibilidad a la URL del servicio Web de facturacion
     *
     * @param endPointWS new value of endPointWS
     */
    public void setEndPointWS(String endPointWS) {
        this.endPointWS = endPointWS;
    }
    
    public String getTrustStoreFile() {
        return trustStoreFile;
    }

    public void setTrustStoreFile(String trustStoreFile) {
        this.trustStoreFile = trustStoreFile;
    }

    public String getTrustStorePwd() {
        return trustStorePwd;
    }

    public void setTrustStorePwd(String trustStorePwd) {
        this.trustStorePwd = trustStorePwd;
    }
    
    @Override
    public ArrayList listar(String condicion) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int vaciarTabla() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void guardarDatos(List lista) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Entidad buscar(String codigo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Este metodo inserta un registro en la tabla afipws_fe_log con el resultado 
     * del intento de facturacion
     * 
     * @param puntoVenta
     * @param cbteDesde
     * @param cbteHasta
     * @param mensaje
     * @param tipoComprobante
     */
    public void generarLogEnBD(int tipoComprobante, int puntoVenta, int cbteDesde, int cbteHasta, String mensaje){
        
        // Capturo posibles errores
        try {
            
            // Armo la instruccion SQL
            String instruccionSQL = "INSERT INTO afipws_fe_log(CbteTipo, PtoVta, CbteDesde, CbteHasta, mensaje)"
                    + "VALUES(?,?,?,?,?)";
            
            // Instancio el objeto que me permite ejecutar la instruccion
            PreparedStatement sentencia = crearSentencia(instruccionSQL);
            
            // Completo los datos del objeto
            sentencia.setInt(1, tipoComprobante);
            sentencia.setInt(2, puntoVenta);
            sentencia.setInt(3, cbteDesde);
            sentencia.setInt(4, cbteHasta);
            sentencia.setString(5, mensaje);
            
            // Ejecuto el update
            actualizar(sentencia);
            
        } catch (SQLException ex) {
            
            // Muestro mensaje de error
            java.util.logging.Logger.getLogger(ControladorTickets.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (Exception ex) {
            
            // Muestro mensaje de error
            java.util.logging.Logger.getLogger(ControladorTickets.class.getName()).log(Level.SEVERE, null, ex);
        }
    }     
}