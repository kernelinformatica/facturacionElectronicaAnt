package entidades;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Esta clase se creo para poder implementar los tipos de tributo de la
 * facturacion electronica del web services de la AFIP
 *
 * @author Matias Panasci Alias Panic
 *
 */
public class EntidadTributo extends Entidad {
    //-----------------------------------------//
    // ACA DECLARO LAS PROPIEDADES DE LA CLASE //
    //-----------------------------------------//
    private short idTipoTributo;
    private String descTributo;
    private double baseImponible;
    private double alicuota;
    private double importe;
    //-----------------------------------------//
    
    /**
     * Metodo constructor de la clase
     */
    public EntidadTributo() {}
    
    /**
     * Metodo constructor de la clase
     * 
     * @param idTipoTributo
     * @param descTributo
     * @param baseImponible
     * @param alicuota
     * @param importe 
     */
    public EntidadTributo(short idTipoTributo, String descTributo, double baseImponible, double alicuota, double importe) {
        this.idTipoTributo = idTipoTributo;
        this.descTributo = descTributo;
        this.baseImponible = baseImponible;
        this.alicuota = alicuota;
        this.importe = importe;
    }

    
    /**
     * Este metodo decide si la factura recibida como parametro es equivalente a
     * la factura actual
     * 
     * @param objeto
     * @return 
     */
    @Override
    public boolean comparar(Entidad objeto) {

        // Instancio un objeto de la misma clase
        EntidadTributo tributoActual = (EntidadTributo) objeto;

        // Valido que las propiedades sean iguales
        if (tributoActual.getIdTipoTributo() == this.getIdTipoTributo()
                && tributoActual.getBaseImponible() == this.getBaseImponible()
                && tributoActual.getImporte() == this.getImporte()
                && tributoActual.getAlicuota() == this.getAlicuota()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Este metodo devuelve el tipo de tributo
     *
     * @return
     */
    public short getIdTipoTributo() {
        return idTipoTributo;
    }

    /**
     * Este metodo setea el tipo de tributo
     *
     * @param idTipoTributo
     */
    public void setIdTipoTributo(short idTipoTributo) {
        this.idTipoTributo = idTipoTributo;
    }

    /**
     * Este metodo devuelve la descripcion del tipo de tributo
     *
     * @return
     */
    public String getDescTributo() {
        return descTributo;
    }

    /**
     * Este metodo setea la descripcion del tipo de tributo
     *
     * @param descTributo
     */
    public void setDescTributo(String descTributo) {
        this.descTributo = descTributo;
    }

    /**
     * Este metodo devuelve la alicuota del tributo
     *
     * @return
     */
    public double getAlicuota() {
        return alicuota;
    }

    /**
     * Este metodo setea la alicuota del tributo
     *
     * @param alicuota
     */
    public void setAlicuota(double alicuota) {
        this.alicuota = alicuota;
    }

    /**
     * Este metodo devuelve la base imponible del tributo
     *
     * @return
     */
    public double getBaseImponible() {
        return baseImponible;
    }

    /**
     * Este metodo setea la base imponible del tributo
     *
     * @param baseImponible
     */
    public void setBaseImponible(double baseImponible) {
        this.baseImponible = baseImponible;
    }

    /**
     * Este metodo devuelve el importe del tributo
     *
     * @return
     */
    public double getImporte() {
        return importe;
    }

    /**
     * Este metodo setea el importe del tributo
     *
     * @param importe
     */
    public void setImporte(double importe) {
        this.importe = importe;
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
            
            // Muestro mensaje de error
            System.out.println("No se pudo convertir la fecha: " + e.getMessage());
            
        } finally {
            
            // Devuelvo una fecha
            return aux;
        }
    }    
}
