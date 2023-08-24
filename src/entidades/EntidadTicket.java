package entidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Esta clase se creo para persistir los tipos de autenticacion en la base de datos
 * Sybase
 *
 * @author Matias Panasci Alias Panic
 */
public class EntidadTicket extends Entidad {

    //-----------------------------------------//
    // ACA DECLARO LAS PROPIEDADES DE LA CLASE //
    //-----------------------------------------//    
    private Long uid;
    private Date genTime;
    private Date expTime;
    private String token;
    private String sign;
    private String source;
    private String destination;
    private String activo;
    private String TestSN;
    //-----------------------------------------//
    
    /**
     * Metodo constructor de la clase
     * 
     * @param uid
     * @param genTime
     * @param expTime
     * @param token
     * @param sign
     * @param source
     * @param destination
     * @param activo
     * @param TestSN 
     */
    public EntidadTicket(Long uid, Date genTime, Date expTime, String token, String sign, String source, String destination, String activo, String TestSN) {
        this.uid = uid;
        this.genTime = genTime;
        this.expTime = expTime;
        this.token = token;
        this.sign = sign;
        this.source = source;
        this.destination = destination;
        this.activo = activo;
        this.TestSN = TestSN;
    }

    public EntidadTicket(Long uid, String genTime, String expTime, String token, String sign, String source, String destination) throws ParseException {
        this.uid = uid;
        this.setGenTime(genTime);
        this.setExpTime(expTime);
        this.token = token;
        this.sign = sign;
        this.source = source;
        this.destination = destination;
        this.activo = "S";
        this.TestSN = (destination.indexOf("cn=wsaahomo") > 0? "N" : "S");
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Date getGenTime() {
        return genTime;
    }

    public void setGenTime(Date genTime) {
        this.genTime = genTime;
    }

    public final void setGenTime(String genTime) throws ParseException {
        // Elimino la T que no me sirve y los numeros que estan de mas
        String sAux = genTime.replace("T", " ");
        sAux = sAux.substring(0, 19);
        
        // Formateo la fecha
        this.genTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(sAux);
    }
        
    public Date getExpTime() {
        return expTime;
    }

    public void setExpTime(Date expTime) {
        this.expTime = expTime;
    }

    public final void setExpTime(String expTime) throws ParseException {
        // Elimino la T que no me sirve y los numeros que estan de mas
        String sAux = expTime.replace("T", " ");
        sAux = sAux.substring(0, 19); 
        
        // Formateo la fecha
        this.expTime = new SimpleDateFormat("yyyy-MM-dd").parse(sAux);
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public String getTestSN() {
        return TestSN;
    }

    public void setTestSN(String TestSN) {
        this.TestSN = TestSN;
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
        EntidadTicket objActual = (EntidadTicket) objeto;

        // Valido que las propiedades ID sean iguales
        if (objActual.getUid() == this.getUid()) {
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
