package entidades;

import java.util.Date;

/**
 *
 * @author administrador
 */
public abstract class Entidad {
    
    public abstract Date getFecha(String fecha);
    public abstract boolean comparar(Entidad objeto);
}
