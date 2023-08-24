package util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import main.Main;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.NtpV3Packet;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Esta clase se creo para sincronizar la hora de la aplicacion con la AFIP
 *
 * @author Administrador
 */
public class ClienteNTP {

    //-----------------------------------------//
    // ACA DECLARO LAS PROPIEDADES DE LA CLASE //
    //-----------------------------------------//    
    private final static Logger log = Logger.getLogger(ClienteNTP.class);    
    private final NTPUDPClient cliente = new NTPUDPClient();
    private List<String> listaServidores = new ArrayList();
    private GregorianCalendar fechaServidor;
    private boolean bandera = false;
    //-----------------------------------------//

    /**
     * Metodo constructor de la clase
     */
    public ClienteNTP() {
        
        // Inicio las propiedades del logger
        PropertyConfigurator.configure("log4j.properties");
        
        // Seteo el tiempo de time out en 10 segundos
        cliente.setDefaultTimeout(10000);
    }

    /**
     * Metodo que otorga visibilidad al objeto
     *
     * @return
     */
    public List<String> getListaServidores() {
        return listaServidores;
    }

    /**
     * Metodo que otorga visibilidad al objeto
     *
     * @param listaServidores
     */
    public void setListaServidores(List<String> listaServidores) {
        this.listaServidores = listaServidores;
    }

    /**
     * Metodo que otorga visibilidad al objeto
     *
     * @return
     * @throws main.FacElectronicaException
     */
    public GregorianCalendar getFechaServidor() throws Exception {

        // Valido que la propiedad no este en null antes de devolverla        
        if (this.fechaServidor == null) {

            // Pregunto si la lista de servidores NTP esta vacia
            if (this.listaServidores.isEmpty()) {

                // Genero log
                log.error("Lista de servidores NTP Vacia, se utilizara la hora de la pc");
                
                // Instancio el objeto con la hora local
                this.fechaServidor = new GregorianCalendar();                
                
            } else {

                // Llamo al metodo que obtiene la hora del servidor
                this.obtenerHoraDeServidores();
            }
        }

        // Devuelvo el resultado
        return fechaServidor;
    }

    /**
     * Este metodo devuelve la fecha de expiracion, segu sea el parametro
     * recibido
     *
     * @param cantidadMinutos
     * @throws main.FacElectronicaException
     * @return
     */
    public GregorianCalendar getFechaExpiracionTicket(int cantidadMinutos) throws Exception {

        // Calculo la fecha de vencimiento en base al parametro
        GregorianCalendar cAux = this.getFechaServidor();
        cAux.add(Calendar.MINUTE, cantidadMinutos);

        // Devuelvo el resultado
        return cAux;
    }

    /**
     * Este metodo devuelve la fecha del servidor en formato java.util.Date
     *
     * @return
     */
    public Date getFechaServidorFormatoDate() {

        return this.fechaServidor.getTime();
    }

    /**
     * Metodo que otorga visibilidad al objeto
     *
     * @param fechaServidor
     */
    public void setFechaServidor(GregorianCalendar fechaServidor) {
        this.fechaServidor = fechaServidor;
    }

    /**
     * Este metodo se conecta al primer servidor NTP disponible y obtiene la
     * hora del mismo
     * @throws java.net.SocketException
     */
    public void obtenerHoraDeServidores() throws SocketException {

        // Inicio las conexiones
        cliente.open();

        // Recorro la lista de servidores buscando uno disponible
        for (String servidor : this.listaServidores) {

            // Capturo posibles errores en la conexion al servidor
            try {

                // Genero una conexion al servidor
                InetAddress hostAddr = InetAddress.getByName(servidor);

                // Muestro por consola los datos del servidor
                log.info("- Utilizando Servidor NTP: " + hostAddr.getHostAddress());

                // Obtengo la info del tiempo del servidor
                TimeInfo info = cliente.getTime(hostAddr);

                // Extraigo los datos que necesito de la info que me dio el servidor NTP
                procesarRespuesta(info);

                // Si pude procesar la respuesta
                if (bandera) {

                    // Corto el bucle
                    break;
                }

            } catch (IOException ioe) {

                // Agrego mensaje al log
                log.error("El servidor: " + servidor + " no se encuentra disponible");
                
            } // FIN CATCH DEL FOR
            
            
        } // FIN FOR LISTA SERVIDORES

        // Pregunto si hubo alguna conexion exitosa
        if(!bandera){
            
            // Agrego mensaje al log
            log.info("Se utilizara la hora de la pc, no se encontraron servidores NTP disponibles");        
            
            // Instancio el objeto con la hora local
            this.fechaServidor = new GregorianCalendar();              
        }
        
        // Cierro las conexiones
        cliente.close();
    }

    /**
     * Este metodo extrae la hora del objeto recibido del servidor NTP y la
     * guarda en un formato que pueda usarse
     *
     * @param info
     */
    private void procesarRespuesta(TimeInfo info) {

        // Convierto la respuesta en un paquete NTP
        NtpV3Packet message = info.getMessage();

        // Receive Time is time request received by server (t2)
        TimeStamp rcvNtpTime = message.getReceiveTimeStamp();

        // Valido si tengo que generar el log
        if (Main.config.getApp_log()) {
            
            // Agrego mensaje al log
            log.error("Hora Recibida:  " + rcvNtpTime.toDateString());            
        }


        // Instancio el objeto calendario
        this.fechaServidor = new GregorianCalendar();

        // Le seteo la fecha del servidor
        this.fechaServidor.setTime(rcvNtpTime.getDate());

        // Seteo la bandera en TRUE por que pude obetener los datos
        this.bandera = true;
    }

}