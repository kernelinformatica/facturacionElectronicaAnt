package entidades;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Esta clase se creo para persistir los tipos de documentos en la base de datos
 * Sybase
 *
 * @author Matias Panasci Alias Panic
 */
public final class EntidadTipoDocs extends Entidad {

    //-----------------------------------------//
    // ACA DECLARO LAS PROPIEDADES DE LA CLASE //
    //-----------------------------------------//    
    private int id;
    private String descripcion;
    private Date fechaDesde;
    private Date fechaHasta;
    //-----------------------------------------//
    
    /**
     * Metodo constructor de la clase
     * 
     * @param id
     * @param descripcion
     * @param fechaDesde
     * @param fechaHasta 
     */
    public EntidadTipoDocs(int id, String descripcion, Date fechaDesde, Date fechaHasta) {
        this.id = id;
        this.descripcion = descripcion;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
    }

    /**
     * Metodo constructor de la clase
     * 
     * @param id
     * @param descripcion
     * @param fechaDesde
     * @param fechaHasta 
     */
    public EntidadTipoDocs(int id, String descripcion, String fechaDesde, String fechaHasta) {
        this.id = id;
        this.descripcion = descripcion;
        this.fechaDesde = this.getFecha(fechaDesde);
        this.fechaHasta = this.getFecha(fechaHasta);
    }
    
    /**
     * Este metodo devuelve el valor del campo fechaHasta
     *
     * @return the value of fechaHasta
     */
    public Date getFechaHasta() {
        return fechaHasta;
    }

    /**
     * Este metodo setea el valor del campo fechaHasta
     *
     * @param fechaHasta new value of fechaHasta
     */
    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
    

    /**
     * Este metodo devuelve el valor del campo fechaDesde
     *
     * @return the value of fechaDesde
     */
    public Date getFechaDesde() {
        return fechaDesde;
    }

    /**
     * Este metodo setea el valor del campo fechaDesde
     *
     * @param fechaDesde new value of fechaDesde
     */
    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }
    

    /**
     * Este metodo devuelve el valor del campo descripcion
     *
     * @return the value of descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Este metodo setea el valor del campo descripcion
     *
     * @param descripcion new value of descripcion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Este metodo devuelve el valor del campo id
     *
     * @return the value of id
     */
    public int getId() {
        return id;
    }

    /**
     * Este metodo setea el valor del campo id
     *
     * @param id new value of id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Este metodo compara un objeto con consigo mismo y devuelve true si son 
     * equivalentes
     * 
     * @param objeto
     * @return 
     */    
    @Override
    public boolean comparar(Entidad objeto) {
        
        // Instancio un objeto de la misma clase
        EntidadTipoDocs objActual = (EntidadTipoDocs) objeto;

        // Valido que las propiedades ID sean iguales
        if (objActual.getId() == this.getId()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Este metodo recibe una fecha con el formato de la AFIP y la transforma en 
     * un java.util.Date
     * 
     * @param fecha
     * @return 
     */
    @Override
    public Date getFecha(String fecha)  {
        
        // Intancio una fecha por defecto
        Date aux = new Date();
        
        // Capturo posibles errores
        try {

            // Valido que el string no sea null
            if(!"NULL".equals(fecha)) {
                
                // Intancio el objeto formateador
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                // Formateo el string con lo que necesito
                String sAux = fecha.substring(0, 4) + "-" + fecha.substring(4, 6) + "-" + fecha.substring(6, 8);

                // Convierto la fecha
                aux = sdf.parse(sAux);   
            }
            
        } catch (Exception e) {
            
            // Le fuerzo null
            aux = null;
            
            // Muestro mensaje de error
            System.out.println("No se pudo convertir la fecha: " + e.getMessage());
            
        } finally {
            
            // Devuelvo una fecha
            return aux;
        }
    }    
}
