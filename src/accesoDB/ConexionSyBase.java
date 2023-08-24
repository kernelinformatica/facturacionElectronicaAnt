package accesoDB;

import configuracion.Configuracion;
import java.sql.*;
import java.util.Properties;

/**
 * Esta clase se creo para realizar las conexion a la base de datos Sybase
 *
 * @author Matias Panasci Alias Panic
 */
public abstract class ConexionSyBase extends Configuracion {

    //-----------------------------------------//
    // ACA DECLARO LAS PROPIEDADES DE LA CLASE //
    //-----------------------------------------//
    private Connection conexion;
    private String url;
    // ----------------------------------------//

    /**
     * Este es el metodo constructor de la clase
     * @throws java.lang.Exception
     */
    public ConexionSyBase() throws Exception {
        
        // Realizo la conexion
        this.conectar();
    }

    /**
     * Este metodo realiza la conexion a la base de datos
     *
     * @throws Exception
     */
    public final void conectar() throws Exception {
        
        // Capturo posibles errores
        try {
            // Cargo el Driver
            Class.forName(this.getDb_driver());

        } catch (ClassNotFoundException ex) {

            // Imprimo el error en la consola
            System.out.println("Error de Driver: " + ex.getMessage());

            // Disparo la excepcion
            throw new Exception("Error de Driver: " + ex.getMessage());
        }

        // Capturo posibles errores realizando la conexion a la BD
        try {

            // Creo el objeto que tienen las propiedades de la conexion
            Properties props = new Properties();
            props.put("user", this.getDb_username());
            props.put("password", this.getDb_password());

            // Armo la URL
            url = this.getDb_tipo_db() + ":" + this.getDb_server_host() + ":" + this.getDb_server_port() + "?ServiceName=" + this.getDb_dbname();
            
            // Intento realizar la conexion
            conexion = DriverManager.getConnection(url, props);
            
        } catch (SQLException ex) {

            // Imprimo el error en la consola
            System.out.println("Error de Conexion: " + ex.getMessage());

            // Disparo la excepcion
            throw new Exception("Error de Conexion: \n Codigo:" + ex.getErrorCode() + "\n Explicacion:" + ex.getMessage());
        }
        
        
    }

    /**
     * Este metrodo ejecuta instrucciones contra la base de datos
     *
     * @param sentencia
     * @return
     * @throws Exception
     */
    public int actualizar(PreparedStatement sentencia) throws Exception {
        // Capturo posibles errores al ejecutar instrucciones
        try {
            
            // Ejecuto la instruccion
            int res = sentencia.executeUpdate();

            // Devuelvo la respuesta
            return res;

        } catch (SQLException ex) {

            // Imprimo el error en la consola
            System.out.println("Error al ejecutar la sentencia: " + ex.getMessage());

            // Disparo la excepcion
            throw new SQLException("Error al ejecutar sentencia: \n Codigo: " + ex.getErrorCode() + " \n Explicacion: " + ex.getMessage());
        }
    }

    /**
     * Este metodo realiza consultas a la base de datoss
     *
     * @param sentencia
     * @return
     * @throws Exception
     */
    public ResultSet consultar(PreparedStatement sentencia) throws Exception {
        // Capturo posibles errores al realizar la consulta
        try {

            // Realizo la consulta
            ResultSet filasBD = sentencia.executeQuery();

            // Devuelvo el resultado de la consulta
            return filasBD;

        } catch (SQLException ex) {

            // Imprimo el error en la consola
            System.out.println("Error al ejecutar la consulta: " + ex.getMessage());

            // Disparo la excepcion
            throw new SQLException("Error al ejecutar consulta: " + ex.getMessage());
        }
    }

    /**
     * Este metodo realiza la desconexion de la base de datos
     */
    public void desconectar() {
        
        // Capturo posibles errores al desconectarme
        try {

            // Realizo la desconexion
            conexion.close();

        } catch (SQLException ex) {

            // Destruyo el objeto conexion
            conexion = null;
        }
    }

    /**
     * Este metodo crea un objeto que permite ejecutar sentencias contra la BD
     *
     * @param sql
     * @return
     * @throws Exception
     */
    public PreparedStatement crearSentencia(String sql) throws Exception {

        // Pregunto si la conexion esta cerrada
        if(this.conexion.isClosed()){
            this.conectar();
        }

        // Intento crear el objeto
        PreparedStatement sentencia = conexion.prepareStatement(sql);

        // Devuelvo el objeto
        return sentencia;

    }

    /**
     * Este metodo devuelve la conexion a la base de datos
     *
     * @return
     */
    public Connection getConexion() {
        return conexion;
    }
    
    /**
     * Este metodo devuelve la cadena de conexion a la base de datos
     * @return 
     */
    public String getURL(){
        return url;     
    }
}