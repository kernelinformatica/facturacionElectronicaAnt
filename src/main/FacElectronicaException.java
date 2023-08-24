package main;

/**
 * Esta clase se creo para detallar aquellas excepcion que arroja el web services 
 * de la afip y no tiene una clara explicacion
 * 
 * @author Matias Panasci
 */
public class FacElectronicaException extends Exception {

    /**
     * Metodo constructor de cla clase
     * 
     * @param message 
     */
    public FacElectronicaException(String message) {
        super(message);
    }

    /**
     * Metodo constructor de la clase
     * @param message
     * @param throwable 
     */
    public FacElectronicaException(String message, Throwable throwable) {
        super(message, throwable);
    }    
    
    /**
     * Este metodo esta sobre escrito de la super clase para controlar el error que devuelve
     * 
     * @return 
     */
    @Override
    public String getMessage()
    {
        return super.getMessage();
    }
}
