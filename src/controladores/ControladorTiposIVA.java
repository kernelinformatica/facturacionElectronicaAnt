package controladores;

import accesoDB.ConexionSyBase;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import entidades.EntidadTipoIVA;
import entidades.Entidad;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Esta clase se creo para implementar la persistencia de los tipos de documentos
 * 
 * @author Matias Panasci Alias Panic
 */
public class ControladorTiposIVA extends ConexionSyBase implements IPersistencia  {

    private final static Logger log = Logger.getLogger(ControladorTiposIVA.class);
            
    /**
     * Metodo constructor de la clase
     * 
     * @throws Exception 
     */
    public ControladorTiposIVA() throws Exception {
        
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
        String instruccionSQL = "DELETE FROM afipws_fe_TiposIVA";
        
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
        String instruccionSQL = "INSERT INTO afipws_fe_TiposIVA VALUES (?,?,?,?)";
        
        // Instancio el objeto que me permite ejecutar la instruccion
        PreparedStatement sentencia = crearSentencia(instruccionSQL);
        
        // Casteo el objeto como entidad
        EntidadTipoIVA entidad = (EntidadTipoIVA)objeto;
        
        // Completo los datos del objeto
        sentencia.setInt(1, entidad.getId() );
        sentencia.setString(2, entidad.getDescripcion());
        sentencia.setDate(3, new java.sql.Date(entidad.getFechaDesde().getTime()));
        sentencia.setDate(4, new java.sql.Date(entidad.getFechaHasta().getTime()));
        
        // Preguntos si esta activado el log de la bd
        if(this.getDb_log()){

            // Genero salida en la consola
            log.info(instruccionSQL);
            log.info("Parametro 1: " + entidad.getId() );
            log.info("Parametro 2: " + entidad.getDescripcion());
            log.info("Parametro 3: " + new java.sql.Date(entidad.getFechaDesde().getTime()));
            log.info("Parametro 4: " + new java.sql.Date(entidad.getFechaHasta().getTime()));
         
        }  
        
        // Ejecuto la instruccion
        return actualizar(sentencia);           
    }

    /**
     *  Este metodo guarda una colecci�n en la base de datos
     * 
     * @param listaTipoIVA 
     * @throws java.lang.Exception 
     */
    @Override
    public void guardarDatos(List listaTipoIVA )throws Exception{
        
        // Recorro la lista recibida como parametro
        for (Iterator it = listaTipoIVA.iterator(); it.hasNext();) {
            
            wsAFIP.IvaTipo obj = (wsAFIP.IvaTipo) it.next();
            
            // Obtengo un elemento de la lista
            EntidadTipoIVA tIva = new EntidadTipoIVA(Integer.parseInt(obj.getId()), 
                                                     obj.getDesc(), 
                                                     obj.getFchDesde(), 
                                                     obj.getFchHasta());
            
            // Lo guardo en la base de datos
            this.insertar(tIva);
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
