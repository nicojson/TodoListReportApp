package tecnm.celaya.edu.mx.todolistreportapp.dao;

import tecnm.celaya.edu.mx.todolistreportapp.model.Tarea;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TareaDao extends Dao<Tarea, Integer> {

    List<Tarea> findByUsuarioId(int usuarioId);

    Map<String, Integer> getTaskCategoryCounts(int userId, LocalDate startDate, LocalDate endDate);

    /**
     * Obtiene un mapa con el conteo de tareas para cada estado (Completada, Pendiente, En Progreso).
     * @param userId El ID del usuario.
     * @param startDate La fecha de inicio del rango.
     * @param endDate La fecha de fin del rango.
     * @return Un Mapa donde la clave es el estado y el valor es el número de tareas.
     */
    Map<String, Integer> getTaskCountsByStatus(int userId, LocalDate startDate, LocalDate endDate);
}
