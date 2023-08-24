package entidades;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Esta clase se creo para poder implementar los tipos de iva de la facturacion
 * electronica del web services de la AFIP
 *
 * @author Matias Panasci Alias Panic
 *
 */
public class EntidadCbteAsoc extends Entidad {
    //-----------------------------------------//
    // ACA DECLARO LAS PROPIEDADES DE LA CLASE //
    //-----------------------------------------//
    private short CTipoAsoc;
    private short PVtaAsoc;
    private double CNroAsoc;
    private double CuitAsoc;
    private String CbteFch;
    //-----------------------------------------//

    /**
     * Este metodo decide si la factura recibida como parametro es equivalente a
     * la factura actual
     */
    @Override
    public boolean comparar(Entidad objeto) {

        // Instancio un objeto de la misma clase
        EntidadCbteAsoc cbteasocActual = (EntidadCbteAsoc) objeto;

        // Valido que las propiedades sean iguales
        if (cbteasocActual.getCTipoAsoc() == this.getCTipoAsoc()
                && cbteasocActual.getPVtaAsoc() == this.getPVtaAsoc()
                && cbteasocActual.getCNroAsoc() == this.getCNroAsoc()
                && cbteasocActual.getCuitAsoc() == this.getCuitAsoc()
                && cbteasocActual.getCbteFch() == this.getCbteFch()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Este metodo devuelve el tipo de Cbte asociado
     *
     * @return
     */
    public short getCTipoAsoc() {
        return CTipoAsoc;
    }

    /**
     * *
     * Este metodo setea el tipo de Cbte Asociado
     *
     * @param CTipoAsoc
     */
    public void setCTipoAsoc(short CTipoAsoc) {
        this.CTipoAsoc = CTipoAsoc;
    }

    /**
     * Este metodo devuelve el PtoVta
     *
     * @return
     */
    public short getPVtaAsoc() {
        return PVtaAsoc;
    }

    /**
     * Este metodo setea el PtoVta
     *
     * @param PVtaAsoc
     */
    public void setPVtaAsoc(short PVtaAsoc) {
        this.PVtaAsoc = PVtaAsoc;
    }
    
    /**
     * Este metodo devuelve el Nro. de Cbte Asoc
     *
     * @return
     */
    public Double getCNroAsoc() {
        return CNroAsoc;
    }

    /**
     * Este metodo setea el Nro. de Cbte Asoc
     *
     * @param CNroAsoc
     */
    public void setCNroAsoc(short CNroAsoc) {
        this.CNroAsoc = CNroAsoc;
    }
    
    /**
     * Este metodo devuelve el Cuit Emisior de Cbte Asoc
     *
     * @return
     */
    public Double getCuitAsoc() {
        return CuitAsoc;
    }
    
    /**
     * Este metodo setea el Cuit Emisor de Cbte Asoc
     *
     * @param CuitAsoc
     */
    public void setCuitAsoc(short CuitAsoc) {
        this.CuitAsoc = CuitAsoc;
    }
    
    /**
     * Este metodo devuelve el Fecha de Cbte Asoc
     *
     * @return
     */
    public String getCbteFch() {
        return CbteFch;
    }
    
    /**
     * Este metodo setea el Cuit Emisor de Cbte Asoc
     *
     * @param CbteFch
     */
    public void setCbteFch(String CbteFch) {
        this.CbteFch = CbteFch;
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
