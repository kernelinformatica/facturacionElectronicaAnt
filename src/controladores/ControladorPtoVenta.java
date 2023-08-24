package controladores;

import accesoDB.ConexionSyBase;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import entidades.EntidadPtoVenta;
import entidades.Entidad;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Esta clase se creo para implementar la persistencia de los puntos de venta
 * 
 * @author Matias Panasci Alias Panic
 */
public class ControladorPtoVenta extends ConexionSyBase implements IPersistencia  {

    private final static Logger log = Logger.getLogger(ControladorPtoVenta.class);
            
    /**
     * Metodo constructor de la clase
     * 
     * @throws Exception 
     */
    public ControladorPtoVenta() throws Exception {
        
        // Mantengo Herencia
        super();      
        
        // Inicio las propiedades del logger
        PropertyConfigurator.configure("log4j.properties");           
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
        String instruccionSQL = "INSERT INTO afipws_fe_ptoventa VALUES (?,?,?,?)";
        
        // Instancio el objeto que me permite ejecutar la instruccion
        PreparedStatement sentencia = crearSentencia(instruccionSQL);
        
        EntidadPtoVenta entidad = (EntidadPtoVenta)objeto;
        
        // Completo los datos del objeto
        sentencia.setInt(1, entidad.getNro() );
        sentencia.setString(2, entidad.getEmisinTipo());
        sentencia.setString(3, entidad.getBloqueado());
        sentencia.setDate(4, new java.sql.Date(entidad.getFechaBaja().getTime()));
        
        // Preguntos si esta activado el log de la bd
        if(this.getDb_log()){

            // Genero salida en la consola
            log.info(instruccionSQL);
            log.info("Parametro 1: " + entidad.getNro() );
            log.info("Parametro 2: " + entidad.getEmisinTipo());
            log.info("Parametro 3: " + entidad.getBloqueado());
            log.info("Parametro 4: " + new java.sql.Date(entidad.getFechaBaja().getTime()));
        }  
        
        // Ejecuto la instruccion
        return actualizar(sentencia);           
    }

    /**
     * Este metodo elimina el contenido de la tabla donde se persisten los datos
     * 
     * @return
     * @throws Exception 
     */
    @Override
    public int vaciarTabla() throws Exception {
        
        // Armo la instruccion SQL
        String instruccionSQL = "DELETE FROM afipws_fe_ptoventa";
        
        // Instancio el objeto que me permite ejecutar la instruccion
        PreparedStatement sentencia = crearSentencia(instruccionSQL);
        
        // Preguntos si esta activado el log de la bd
        if(this.getDb_log()){

            // Genero salida en la consola
            log.info(instruccionSQL);
        }  
        
        // Ejecuto la instruccion
        return actualizar(sentencia);             
    }

    /**
     *  Este metodo guarda una colecci�n en la base de datos
     * 
     * @param listaPtoVenta 
     * @throws java.lang.Exception 
     */
    @Override
    public void guardarDatos(List listaPtoVenta) throws Exception{
        
        // Recorro la lista recibida como parametro
        for (Iterator it = listaPtoVenta.iterator(); it.hasNext();) {
            
            wsAFIP.PtoVenta obj = (wsAFIP.PtoVenta) it.next();
            
            // Obtengo un elemento de la lista
            EntidadPtoVenta pto = new EntidadPtoVenta(obj.getNro(),
                                                    obj.getEmisionTipo(),
                                                    obj.getBloqueado(),
                                                    obj.getFchBaja());
            
            // Lo guardo en la base de datos
            this.insertar(pto);
        }
    }
    
    @Override
    public int modificar(Entidad objeto) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Entidad buscar(String codigo) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList listar(String condicion) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int borrar(String condicion) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
