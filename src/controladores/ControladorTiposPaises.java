package controladores;

import accesoDB.ConexionSyBase;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import entidades.EntidadTipoPais;
import entidades.Entidad;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Esta clase se creo para implementar la persistencia de los tipos de documentos
 * 
 * @author Matias Panasci Alias Panic
 */
public class ControladorTiposPaises extends ConexionSyBase implements IPersistencia  {

    private final static Logger log = Logger.getLogger(ControladorTiposPaises.class);
            
    /**
     * Metodo constructor de la clase
     * 
     * @throws Exception 
     */
    public ControladorTiposPaises() throws Exception {
        
        // Mantengo Herencia
        super();        
        
        // Inicio las propiedades del logger
        PropertyConfigurator.configure("log4j.properties");           
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
        String instruccionSQL = "DELETE FROM afipws_fe_TiposPaises";
        
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
     * Este metodo inserta guarda la entidad en la base de datos
     * 
     * @param objeto
     * @return
     * @throws Exception 
     */
    @Override
    public int insertar(Entidad objeto) throws Exception {
        
        // Armo la instruccion SQL
        String instruccionSQL = "INSERT INTO afipws_fe_TiposPaises VALUES (?,?)";
        
        // Instancio el objeto que me permite ejecutar la instruccion
        PreparedStatement sentencia = crearSentencia(instruccionSQL);
        
        // Casteo el objeto como entidad
        EntidadTipoPais entidad = (EntidadTipoPais)objeto;
        
        // Completo los datos del objeto
        sentencia.setInt(1, entidad.getId() );
        sentencia.setString(2, entidad.getDescripcion());
        
        // Preguntos si esta activado el log de la bd
        if(this.getDb_log()){

            // Genero salida en la consola
            log.info(instruccionSQL);
            log.info("Parametro 1: " + entidad.getId() );
            log.info("Parametro 2: " + entidad.getDescripcion());
        }          
        
        // Ejecuto la instruccion
        return actualizar(sentencia);           
    }

    /**
     *  Este metodo guarda una colecci�n en la base de datos
     * 
     * @param listaTiposPaises 
     */
    @Override
    public void guardarDatos(List listaTiposPaises) throws Exception{
        
        // Recorro la lista recibida como parametro
        for (Iterator it = listaTiposPaises.iterator(); it.hasNext();) {
            
            wsAFIP.PaisTipo obj = (wsAFIP.PaisTipo) it.next();
            
            // Obtengo un elemento de la lista
            EntidadTipoPais tipoPais = new EntidadTipoPais(obj.getId(),obj.getDesc());
            
            // Lo guardo en la base de datos
            this.insertar(tipoPais);
        }
    }
    
    @Override
    public int borrar(String condicion) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    
}
