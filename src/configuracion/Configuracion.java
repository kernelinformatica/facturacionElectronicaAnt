package configuracion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Esta clase se creo para el manejo de los archivos de configuracion
 * 
 * @author Carlos - Editado: Matias Panasci 14/10/2015
 */
public class Configuracion {

    //----------------------------------------------------//
    // Aca delcaro las variables necesarias para la clase //
    //----------------------------------------------------//    
    private String db_server_host = "10.0.0.104";    
    private String db_driver = "com.sybase.jdbc3.jdbc.SybDriver";
    private String db_tipo_db = "jdbc\\:sybase\\:Tds";
    private String db_dbname = "Testing_fe";
    private Integer db_server_port = 2638;
    private String db_username = "DBA";
    private String db_password = "SQL";

    private String ws_nombre="wsfe";    
    private Boolean ws_test = true;    
    
    private Boolean db_log = false;
    private Boolean ws_log = false;
    private Boolean app_log = false;
    private Boolean app_pausa = false;
    
    private Boolean actualizaComprobantes=false;
    private int app_cantDiasFiltro=10;
    private int timeOutWS=180;
            
    private String ntp_servidorNTP = "time.afip.gob.ar2";
    private String ntp_servidorNTP2 = "ar.pool.ntp.org";

    //----------------------------------------------------// 
    private final String CONFIG_FILE = "config.properties";
    private final Properties config;
    private OutputStream output = null;
    private InputStream input = null;
    //----------------------------------------------------// 
    
    /**
     * Metodo constructor de la clase, lee los datos del archivo config.properties, si no existe el mismo toma
     * los datos de default.properties (dentro del jar)
     *
     * @throws IOException
     */
    public Configuracion() throws Exception, IOException {
        
        // Declaro variables auxiliares
        config = new Properties();

        // Intento abrir el archivo de configuracion
        File f = new File(CONFIG_FILE);
        
        // Valido que exista
        if (f.isFile()) {
            
            input = new FileInputStream(CONFIG_FILE);
            
        } else {
            
            // Genero el archivo con las propiedades por defecto
            this.actualizar();
            
            // Levanto el archivo
            input = new FileInputStream(CONFIG_FILE);            
        }

        // Abro el archivo properties
        config.load(input);

        // Seteo las configuraciones
        this.db_server_host = config.getProperty("db.serverhost");        
        this.db_driver = config.getProperty("db.driver");
        this.db_tipo_db = config.getProperty("db.tipodb");
        this.db_dbname = config.getProperty("db.dbname");
        this.db_server_port = Integer.parseInt(config.getProperty("db.serverport"));
        this.db_username = config.getProperty("db.username");
        this.db_password = config.getProperty("db.password");
        
        this.ws_nombre = config.getProperty("ws.nombre");        
        this.ws_test = Boolean.valueOf(config.getProperty("ws.test"));        

        this.app_log = Boolean.valueOf(config.getProperty("app.log"));        
        this.db_log = Boolean.valueOf(config.getProperty("db.log"));
        this.ws_log = Boolean.valueOf(config.getProperty("ws.log"));
        this.app_pausa = Boolean.valueOf(config.getProperty("app.pausa"));
        
        this.app_cantDiasFiltro = Integer.valueOf(config.getProperty("cantidadDiasFiltro"));        
        this.actualizaComprobantes = Boolean.valueOf(config.getProperty("actualizaComprobantes"));
        this.timeOutWS = Integer.valueOf(config.getProperty("timeOutWS"));
        
        this.ntp_servidorNTP = config.getProperty("ntp.servidorNTP");
        this.ntp_servidorNTP2 = config.getProperty("ntp.servidorNTP2");
        
        input.close();
    }

    /**
     * Este metodo genera un archivo de configuracion en base a la configuracion por defecto
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public final void actualizar() throws FileNotFoundException, IOException {
        
        // Obtengo referencia al archivo
        output = new FileOutputStream(CONFIG_FILE);
        
        // Seteo los valores de las propiedades
        config.setProperty("db.serverhost", this.db_server_host);        
        config.setProperty("db.driver", this.db_driver);
        config.setProperty("db.tipodb", this.db_tipo_db);
        config.setProperty("db.serverport", this.db_server_port.toString());
        config.setProperty("db.dbname", this.db_dbname);
        config.setProperty("db.username", this.db_username);
        config.setProperty("db.password", this.db_password);

        config.setProperty("ws.nombre", this.ws_nombre);        
        config.setProperty("ws.test", this.ws_test.toString());

        config.setProperty("app.log", this.app_log.toString());        
        config.setProperty("db.log", this.db_log.toString());
        config.setProperty("ws.log", this.ws_log.toString());
        config.setProperty("app.pausa", this.app_pausa.toString());
        config.setProperty("cantidadDiasFiltro", String.valueOf(this.app_cantDiasFiltro));
        config.setProperty("actualizaComprobantes", this.actualizaComprobantes.toString());
        config.setProperty("timeOutWS", String.valueOf(this.timeOutWS));

        config.setProperty("ntp.servidorNTP", this.ntp_servidorNTP);
        config.setProperty("ntp.servidorNTP2", this.ntp_servidorNTP2);
        
        // Guardo los cambios y cierro el archivo
        config.store(output, null);
        output.close();
    }

    //--------------------------------------------------------------------------//
    //                      INICIO METODOS DE VISIBILIDAD                       //
    //--------------------------------------------------------------------------//
    
    /**
     * Metodo que otorga visbilidad a la propiedad
     * 
     * @return
     */
    public Boolean getDb_log() {
        return db_log;
    }

    /**
     * Metodo que otorga visbilidad a la propiedad
     * 
     * @param db_log
     */
    public void setDb_log(Boolean db_log) {
        this.db_log = db_log;
    }

    /**
     * Metodo que otorga visbilidad a la propiedad
     * 
     * @return
     */
    public String getDb_driver() {
        return db_driver;
    }

    /**
     * Metodo que otorga visbilidad a la propiedad
     * 
     * @param db_driver
     */
    public void setDb_driver(String db_driver) {
        this.db_driver = db_driver;
    }

    /**
     * Metodo que otorga visbilidad a la propiedad
     * 
     * @return
     */
    public String getDb_tipo_db() {
        return db_tipo_db;
    }

    /**
     *  Metodo que otorga visbilidad a la propiedad
     * 
     * @param db_tipo_db
     */
    public void setDb_tipo_db(String db_tipo_db) {
        this.db_tipo_db = db_tipo_db;
    }

    /**
     *
     * @return
     */
    public String getDb_server_host() {
        return db_server_host;
    }

    /**
     *  Metodo que otorga visbilidad a la propiedad
     * 
     * @param db_server_host
     */
    public void setDb_server_host(String db_server_host) {
        this.db_server_host = db_server_host;
    }

    /**
     *  Metodo que otorga visbilidad a la propiedad
     * 
     * @return
     */
    public Integer getDb_server_port() {
        return db_server_port;
    }

    /**
     *  Metodo que otorga visbilidad a la propiedad
     * 
     * @param db_server_port
     */
    public void setDb_server_port(Integer db_server_port) {
        this.db_server_port = db_server_port;
    }

    /**
     * Metodo que otorga visbilidad a la propiedad
     * 
     * @return
     */
    public String getDb_dbname() {
        return db_dbname;
    }

    /**
     *  Metodo que otorga visbilidad a la propiedad
     * 
     * @param db_dbname
     */
    public void setDb_dbname(String db_dbname) {
        this.db_dbname = db_dbname;
    }

    /**
     *  Metodo que otorga visbilidad a la propiedad
     * 
     * @return
     */
    public String getDb_username() {
        return db_username;
    }

    /**
     * Metodo que otorga visbilidad a la propiedad
     * 
     * @param db_username
     */
    public void setDb_username(String db_username) {
        this.db_username = db_username;
    }

    /**
     * Metodo que otorga visbilidad a la propiedad
     * 
     * @return
     */
    public String getDb_password() {
        return db_password;
    }

    /**
     * Metodo que otorga visbilidad a la propiedad
     * 
     * @param db_password
     */
    public void setDb_password(String db_password) {
        this.db_password = db_password;
    }

    /**
     *  Metodo que otorga visbilidad a la propiedad
     * 
     * @return
     */
    public String getWs_nombre() {
        return ws_nombre;
    }

    /**
     *  Metodo que otorga visbilidad a la propiedad
     * 
     * @param ws_nombre
     */   
    public void setWs_nombre(String ws_nombre) {
        this.ws_nombre = ws_nombre;
    }
    
    /**
     *  Metodo que otorga visbilidad a la propiedad
     * 
     * @return
     */
    public Boolean getWs_log() {
        return ws_log;
    }

    /**
     *  Metodo que otorga visbilidad a la propiedad
     * 
     * @param ws_log
     */
    public void setWs_log(Boolean ws_log) {
        this.ws_log = ws_log;
    }

    /**
     *  Metodo que otorga visbilidad a la propiedad
     * 
     * @return
     */
    public Boolean getWs_test() {
        return ws_test;
    }

    /**
     *  Metodo que otorga visbilidad a la propiedad
     * 
     * @param ws_test
     */
    public void setWs_test(Boolean ws_test) {
        this.ws_test = ws_test;
    }

    /**
     * Metodo que otorga visbilidad a la propiedad
     * 
     * @return
     */
    public Boolean getApp_log() {
        return app_log;
    }

    /**
     *  Metodo que otorga visbilidad a la propiedad
     * 
     * @param app_log
     */
    public void setApp_log(Boolean app_log) {
        this.app_log = app_log;
    }

    /**
     * Metodo que otorga visbilidad a la propiedad
     * 
     * @return 
     */
    public Boolean getApp_pausa() {
        return app_pausa;
    }

    /**
     * Metodo que otorga visbilidad a la propiedad
     * 
     * @param app_pausa 
     */
    public void setApp_pausa(Boolean app_pausa) {
        this.app_pausa = app_pausa;
    }

    /**
     * Metodo que otorga visbilidad a la propiedad
     * 
     * @return 
     */
    public int getApp_cantDiasFiltro() {
        return app_cantDiasFiltro;
    }
    
    /**
     * Metodo que otorga visbilidad a la propiedad
     * 
     * @return 
     */
    public Boolean getActualizaComprobantes() {
        return actualizaComprobantes;
    }

    /**
     * Metodo que otorga visbilidad a la propiedad
     * 
     * @param actualizaComprobantes 
     */
    public void setActualizaComprobantes(Boolean actualizaComprobantes) {
        this.actualizaComprobantes = actualizaComprobantes;
    }

    /**
     * Metodo que otorga visbilidad a la propiedad
     * 
     * @return 
     */
    public int getTimeOutWS() {
        return timeOutWS;
    }

    /**
     * Metodo que otorga visbilidad a la propiedad
     * 
     * @param timeOutWS 
     */
    public void setTimeOutWS(int timeOutWS) {
        this.timeOutWS = timeOutWS;
    }

    /**
     * Metodo que otorga visbilidad a la propiedad
     * 
     * @param app_cantDiasFiltro 
     */
    public void setApp_cantDiasFiltro(int app_cantDiasFiltro) {
        this.app_cantDiasFiltro = app_cantDiasFiltro;
    }

    
    /**
     *  Metodo que otorga visbilidad a la propiedad
     * 
     * @return 
     */
    public String getServidorNTP1() {
        return ntp_servidorNTP;
    }

    /**
     *  Metodo que otorga visbilidad a la propiedad
     * 
     * @param ntp_servidorNTP 
     */
    public void setNtp_servidorNTP(String ntp_servidorNTP) {
        this.ntp_servidorNTP = ntp_servidorNTP;
    }

    /**
     * Metodo que otorga visbilidad a la propiedad
     * 
     * @return 
     */
    public String getServidorNTP2() {
        return ntp_servidorNTP2;
    }

    /**
     * Metodo que otorga visbilidad a la propiedad
     * 
     * @param ntp_servidorNTP2 
     */
    public void setNtp_servidorNTP2(String ntp_servidorNTP2) {
        this.ntp_servidorNTP2 = ntp_servidorNTP2;
    }
    
    //-------------------------------------------------------------------------//
    //                       FIN METODOS DE VISIBILIDAD                        //
    //-------------------------------------------------------------------------//    
}