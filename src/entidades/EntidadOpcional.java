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
public class EntidadOpcional extends Entidad {
    //-----------------------------------------//
    // ACA DECLARO LAS PROPIEDADES DE LA CLASE //
    //-----------------------------------------//

    private short Id;
    private String valor;
    //-----------------------------------------//

    /**
     * Este metodo decide si la factura recibida como parametro es equivalente a
     * la factura actual
     */
    @Override
    public boolean comparar(Entidad objeto) {

        // Instancio un objeto de la misma clase
        EntidadOpcional opcionalActual = (EntidadOpcional) objeto;

        // Valido que las propiedades sean iguales
        if (opcionalActual.getId() == this.getId()
                && opcionalActual.getvalor() == this.getvalor()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Este metodo devuelve el tipo de iva
     *
     * @return
     */
    public short getId() {
        return Id;
    }

    /**
     * *
     * Este metodo setea el tipo de iva
     *
     * @param id
     */
    public void setId(short id) {
        this.Id = id;
    }

    /**
     * Este metodo devuelve la base imponible
     *
     * @return
     */
    public String getvalor() {
        return valor;
    }

    /**
     * Este metodo setea la base imponible
     *
     * @param valor
     */
    public void setvalor(String valor) {
        this.valor = valor;
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
