package util;

import main.Main;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.sql.SQLWarning;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Carlos
 */
public class MyUtil {

    /**
     * Fecha y hora actual en formato XMLGregorianCalendar
     *
     * @return Fecha y hora actual en formato XMLGregorianCalendar
     * @throws DatatypeConfigurationException
     */
    public static XMLGregorianCalendar getXMLGregorianCalendarNow() throws DatatypeConfigurationException {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        return now;
    }

    /**
     * Convierte fecha y hora a string
     *
     * @return
     */
    public static String fechaYHoraAString() {
        
        // Instancio un objeto que me permite formatear la fecha
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        
        // Genero un nombre para el archivo
        String nombreArchivo = sdf.format(new Date());

        // Reemplazo los caracteres invalidos
        nombreArchivo = nombreArchivo.replaceFirst(":","hs");
        nombreArchivo = nombreArchivo.replaceFirst(":","min");
        
        return nombreArchivo;
    }

    /**
     * Crea directorio
     *
     * @param nombre Nombre del directorio a crear
     * @return
     */
    public static boolean crearDirectorio(String nombre) {
        java.io.File theDir = new java.io.File(nombre);

        // if the directory does not exist, create it
        if (!theDir.exists()) {
            //System.out.println("creating directory: " + directoryName);
            boolean result = false;

            try {
                theDir.mkdir();
                result = true;
            } catch (SecurityException se) {
                System.err.println("Error: No se puede crear directorio " + nombre + " " + se.getMessage());
            }
            return result;

        }
        return true;
    }

    /**
     * Elimina los archivos del directorio log
     */
    public static void limpiarArchivos() {
        File file = new File("logs/resultado.txt");
        file.delete();

        file = new File("logs/errores.txt");
        file.delete();

        file = new File("logs/request.xml");
        file.delete();

        file = new File("logs/response.xml");
        file.delete();

        file = new File("logs/fault.xml");
        file.delete();
    }

    /**
     * Graba archivo, si ya existe agrega el contenido
     *
     * @param nombre Nombre del archivo
     * @param content Contenido del archivo
     * @return
     */
    public static boolean grabarArchivo(String nombre, String content) {
        FileWriter w = null;
        try {
            File archivo = new File(nombre);
            //Si coincide el nombre del archivo, agrega el contenido
            w = new FileWriter(archivo, true);
            BufferedWriter bw = new BufferedWriter(w);
            PrintWriter wr = new PrintWriter(bw);
            wr.append(content);
            wr.close();
            bw.close();
            return true;
        } catch (IOException ex) {
            System.err.println("Error grabarArchivo - No se puede grabar archivo " + nombre + " - " + ex.getMessage());
            return false;
        } finally {
            try {
                if (w != null) {
                    w.close();
                }
            } catch (IOException ex) {
                System.err.println("Error grabarArchivo - No se puede cerrar archivo " + nombre + " - " + ex.getMessage());
            }

        }
    }

    /**
     *
     * @param tipo
     * @param warning
     * @return
     */
    public static String sqlWarningToString(String tipo, SQLWarning warning) {
        String resp = null;
        if (warning == null) {
            return "";
        }
        while (warning != null) {
            resp += "Mensaje: " + warning.getMessage();
            resp += " SQLState: " + warning.getSQLState();
            resp += " Cod.Interno: " + warning.getErrorCode() + "\r\n";
            warning = warning.getNextWarning();
        }
        return "[" + tipo + "] " + resp;
    }

    /**
     * Prueba conexi�n a Google
     *
     * @return
     */
    public static boolean testConexionInternet() {
        // Valido que la conexion a internet
        int puerto = 80;
        try {
            Socket s = new Socket("www.google.com.ar", puerto);
            if (s.isConnected()) {
                s.close();
            }
        } catch (IOException ex) {
            LogError("Error: No hay conexión a internet " + ex.getMessage(), true);
            return false;
        }
        
        return true;
    }

    /**
     * Devuelve el path de ejecución del Jar
     *
     * @return
     */
    public static String pathEjecucion() {
        try {
            //Muestra path de ejecución
            String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            return URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            LogError("Error pathEjecucion: " + ex.getMessage(), true);
            return "";
        }

    }

    /**
     * Convierte java.util.Date a java.sql.Date
     *
     * @param date fecha de tipo java.util.Date
     * @return fecha de tipo java.sql.Date
     */
    public static java.sql.Date convertirJavaDateASqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    /**
     * Rutina de logueo de errores
     *
     * @param contenido contenido del error
     * @param graba_archivo define si se graba a archivo o no
     */
    public static void LogError(String contenido, boolean graba_archivo) {

        //Siempre muestra por consola
        System.err.println(contenido);

//        Main.logProceso += contenido + "\r\n";

        if (graba_archivo) {
            //Armo nombre de archivo, uno por mes
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            String pre_file = sdf.format(new Date());
            String filename = "logs/error_" + pre_file + ".log";

            //Armo el contenido del archivo
            sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String pre = sdf.format(new Date());
            String contenido_nuevo = pre + " " + contenido + "\r\n";

            grabarArchivo(filename, contenido_nuevo);
        }
    }

    public static String getNombrePCyUsuario() {
        String ope = "";

        try {
            ope += java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception ex) {
            ope += "N/D";
        }
        ope += " " + System.getProperty("user.name");

        return ope.substring(0, 20);
    }
    
    /**
     * Este metodo devuelve la fecha minima para la busqueda de comprobantes en la fac_ventas
     *
     * @param fecha
     * @return
     */
    public static Date getFechaMinima(Date fecha) {

        // Obtengo el objeto calendario
        Calendar calendarDate = Calendar.getInstance();

        // Le seteo la fecha de busqueda
        calendarDate.setTime(fecha);

        // Le resto los dias del parametro
        calendarDate.add(Calendar.DATE, (-1 * Main.config.getApp_cantDiasFiltro()));

        // Le seteo el tiempo
        calendarDate.set(Calendar.HOUR_OF_DAY, 0);
        calendarDate.set(Calendar.MINUTE, 0);
        calendarDate.set(Calendar.SECOND, 0);

        // Devuelvo el resultado
        return calendarDate.getTime();
    }     
}
