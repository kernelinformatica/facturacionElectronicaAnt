package entidades;

import wsAFIP.FECAEDetRequest;

/**
 * Esta clase se creo para implementar las facturas de venta electronica
 * 
 * @author Administrador
 */
public class Comprobante {

    //----------------------------------------------------//
    // Aca delcaro las variables necesarias para la clase //
    //----------------------------------------------------//    
    private int v_tipo_comprobante;     // fac ventas
    private int pto_numero;             // fac ventas
    private long v_numero_comprobante;   // fac ventas
    //----------------------------------------------------//
    private FECAEDetRequest comprobanteAFIP;
    //----------------------------------------------------//
    
    /**
     * Metodo constructor de la clase que inicia las variables
     * 
     * @param v_tipo_comprobante
     * @param pto_numero
     * @param v_numero_comprobante
     */
    public Comprobante(int v_tipo_comprobante, int pto_numero, long v_numero_comprobante,  FECAEDetRequest cbteAfip) {
        this.v_tipo_comprobante = v_tipo_comprobante;
        this.pto_numero = pto_numero;
        this.v_numero_comprobante = v_numero_comprobante;
        this.comprobanteAFIP = cbteAfip;
    }

    
    /**
     * Get the value of v_numero_comprobante
     *
     * @return the value of v_numero_comprobante
     */
    public long getV_numero_comprobante() {
        return v_numero_comprobante;
    }

    /**
     * Este metodo devuelve el numero de comprobante de gestagro
     *
     * @param v_numero_comprobante new value of v_numero_comprobante
     */
    public void setV_numero_comprobante(int v_numero_comprobante) {
        this.v_numero_comprobante = v_numero_comprobante;
    }
    
    /**
     * Este metodo devuelve el punto de venta de gestAgro
     *
     * @return the value of pto_numero
     */
    public int getPto_numero() {
        return pto_numero;
    }

    /**
     * Este metodo setea el punto de venta de gestagro
     *
     * @param pto_numero new value of pto_numero
     */
    public void setPto_numero(int pto_numero) {
        this.pto_numero = pto_numero;
    }
    
    
    /**
     * Este metodo devuelve el tipo de comprobante de GESTAGRO
     *
     * @return the value of v_tipo_comprobante
     */
    public int getV_tipo_comprobante() {
        return v_tipo_comprobante;
    }

    /**
     * Set the value of v_tipo_comprobante
     *
     * @param v_tipo_comprobante new value of v_tipo_comprobante
     */
    public void setV_tipo_comprobante(int v_tipo_comprobante) {
        this.v_tipo_comprobante = v_tipo_comprobante;
    }

    public FECAEDetRequest getComprobanteAFIP() {
        return comprobanteAFIP;
    }

    public void setComprobanteAFIP(FECAEDetRequest comprobanteAFIP) {
        this.comprobanteAFIP = comprobanteAFIP;
    }
    

    /**
     * Este metodo devuelve el numero de comprobante de la AFIP
     *
     * @return the value of CbteDesde
     */
    public long getCbteDesde() {
        return this.comprobanteAFIP.getCbteDesde();
    }

    /**
     * Este metodo setea el numero de comprobante de la AFIP
     *
     * @param CbteDesde new value of CbteDesde
     */
    public void setCbteDesde(long CbteDesde) {
        this.comprobanteAFIP.setCbteDesde(CbteDesde);
        this.comprobanteAFIP.setCbteHasta(CbteDesde);
    }
    
    /**
     * Este metodo devuelve la descripcion del comprobante
     * 
     * @return 
     */
    public String getDescripcionCbteInterna() {
        
        // Devuelvo el string con la descripcion
        return "Tipo Cbte: " + this.getV_tipo_comprobante() + 
               " Pto: " + this.getPto_numero() + 
               " Nro Cbte: " + this.getV_numero_comprobante();
    }
    
    /**
     * Este metodo devuelve la descripcion del comprobante
     * 
     * @return 
     */
    public String getDescripcionCbteAFIP() {
        
        // Devuelvo el string con la descripcion
        return "Tipo Cbte: " + this.getV_tipo_comprobante() + 
               " Pto: "      + this.getPto_numero() + 
               " Nro Cbte: " + this.getCbteDesde();
    }    
}
