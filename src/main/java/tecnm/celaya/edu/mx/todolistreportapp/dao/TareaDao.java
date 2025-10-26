package tecnm.celaya.edu.mx.todolistreportapp.dao;

import tecnm.celaya.edu.mx.todolistreportapp.model.Tarea;

import java.util.List;

/**
 * Interfaz DAO específica para la entidad Tarea.
 * Hereda las operaciones CRUD de la interfaz genérica Dao y añade métodos específicos.
 */
public interface TareaDao extends Dao<Tarea, Integer> {

    /**
     * Busca todas las tareas que pertenecen a un usuario específico.
     * @param usuarioId El ID del usuario cuyas tareas se quieren encontrar.
     * @return una Lista de todas las tareas del usuario.
     */
    List<Tarea> findByUsuarioId(int usuarioId);

}
