package entidades;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Esta clase se creo para persistir los puntos de venta en la base de datos
 * Sybase
 *
 * @author Matias Panasci Alias Panic
 */
public final class EntidadPtoVenta extends Entidad{

    //-----------------------------------------//
    // ACA DECLARO LAS PROPIEDADES DE LA CLASE //
    //-----------------------------------------//    
    private int nro;
    private String emisinTipo;
    private String bloqueado;
    private Date fechaBaja;
    //-----------------------------------------//

    /**
     * Metodo constructor de la clase
     * 
     * @param nro
     * @param emisinTipo
     * @param bloqueado
     * @param fechaBaja 
     */
    public EntidadPtoVenta(int nro, String emisinTipo, String bloqueado, Date fechaBaja) {
        this.nro = nro;
        this.emisinTipo = emisinTipo;
        this.bloqueado = bloqueado;
        this.fechaBaja = fechaBaja;
    }

    /**
     * Metodo constructor de la clase
     * 
     * @param nro
     * @param emisinTipo
     * @param bloqueado
     * @param fechaBaja 
     */
    public EntidadPtoVenta(int nro, String emisinTipo, String bloqueado, String fechaBaja) {
        this.nro = nro;
        this.emisinTipo = emisinTipo;
        this.bloqueado = bloqueado;
        this.fechaBaja = this.getFecha(fechaBaja);
    }
    
    /**
     * Este metodo devuelve el campo fechaBaja
     *
     * @return the value of fechaBaja
     */
    public Date getFechaBaja() {
        return fechaBaja;
    }

    /**
     * Este setea devuelve el campo fechaBaja
     *
     * @param fechaBaja new value of fechaBaja
     */
    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    
    /**
     * Este metodo devuelve el campo bloqueado
     *
     * @return the value of bloqueado
     */
    public String getBloqueado() {
        return bloqueado;
    }

    /**
     * Este metodo setea el campo bloqueado
     *
     * @param bloqueado new value of bloqueado
     */
    public void setBloqueado(String bloqueado) {
        this.bloqueado = bloqueado;
    }
    

    /**
     * Este metodo devuelve el campo emisinTipo
     *
     * @return the value of emisinTipo
     */
    public String getEmisinTipo() {
        return emisinTipo;
    }

    /**
     * Este metodo setea el campo emisinTipo
     *
     * @param emisinTipo new value of emisinTipo
     */
    public void setEmisinTipo(String emisinTipo) {
        this.emisinTipo = emisinTipo;
    }

    /**
     * Este metodo devuelve el campo of nro
     *
     * @return the value of nro
     */
    public int getNro() {
        return nro;
    }

    /**
     * Este metodo setea el campo nro
     *
     * @param nro new value of nro
     */
    public void setNro(int nro) {
        this.nro = nro;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
