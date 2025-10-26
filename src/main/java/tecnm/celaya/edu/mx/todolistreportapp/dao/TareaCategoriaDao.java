package tecnm.celaya.edu.mx.todolistreportapp.dao;

/**
 * Interfaz DAO para gestionar la tabla pivote Tarea_Categoria.
 */
public interface TareaCategoriaDao {

    /**
     * Crea una asociación entre una tarea y una categoría.
     * @param idTarea El ID de la tarea.
     * @param idCategoria El ID de la categoría.
     * @return true si la asociación se guardó correctamente, false en caso contrario.
     */
    boolean associate(int idTarea, int idCategoria);

    /**
     * (Opcional) Elimina todas las asociaciones de una tarea.
     * @param idTarea El ID de la tarea.
     * @return true si se eliminaron las asociaciones, false en caso contrario.
     */
    boolean disassociateByTareaId(int idTarea);
}
