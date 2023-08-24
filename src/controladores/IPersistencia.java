package controladores;

import java.util.ArrayList;
import java.util.List;
import entidades.Entidad;

/**
 * Esta interface se creo para definir los metodos de persistencia obligatorios
 * que deberan implementar las clases de persistencia
 *
 * @author Matias Panasci Alias Panic
 */
public interface IPersistencia {

    /**
     * Este metodo se utilizara para insertar registros en la BD
     *
     * @param objeto
     * @return
     * @throws Exception
     */
    public int insertar(Entidad objeto) throws Exception;

    /**
     * Este metodo se utilizara para borrar registros de la BD
     *
     * @param condicion
     * @return
     * @throws Exception
     */
    public int borrar(String condicion) throws Exception;

    /**
     * Este metodo se utilizara para modificar registros de la BD
     *
     * @param objeto
     * @return
     * @throws Exception
     */
    public int modificar(Entidad objeto) throws Exception;

    /**
     * Este metodo se utilizara para buscar un dato en particular dentro de la
     * BD
     *
     * @param codigo
     * @return
     * @throws Exception
     */
    public Entidad buscar(String codigo) throws Exception;

    /**
     * Este metodo se utilizara para listar el contenido de la BD en forma de
     * objetos
     *
     * @param condicion
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public ArrayList listar(String condicion) throws Exception;
    
    public int vaciarTabla()throws Exception;
    
    public void guardarDatos(List lista) throws Exception;
}