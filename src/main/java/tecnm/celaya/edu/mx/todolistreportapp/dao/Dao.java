package tecnm.celaya.edu.mx.todolistreportapp.dao;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz genérica para el patrón Data Access Object (DAO).
 * Define las operaciones CRUD estándar que cada DAO debe implementar.
 *
 * @param <T> El tipo del objeto de modelo (ej. Usuario, Tarea).
 * @param <I> El tipo del identificador del objeto (ej. Integer, Long).
 */
public interface Dao<T, I> {

    /**
     * Busca un objeto por su ID.
     * @param id El ID del objeto a buscar.
     * @return un Optional que contiene el objeto si se encuentra, o un Optional vacío si no.
     */
    Optional<T> findById(I id);

    /**
     * Obtiene todos los objetos de este tipo de la base de datos.
     * @return una Lista de todos los objetos.
     */
    List<T> findAll();

    /**
     * Guarda un nuevo objeto en la base de datos.
     * @param t El objeto a guardar.
     * @return true si se guardó correctamente, false en caso contrario.
     */
    boolean save(T t);

    /**
     * Actualiza un objeto existente en la base de datos.
     * @param t El objeto con los datos actualizados.
     * @return true si se actualizó correctamente, false en caso contrario.
     */
    boolean update(T t);

    /**
     * Elimina un objeto de la base de datos por su ID.
     * @param id El ID del objeto a eliminar.
     * @return true si se eliminó correctamente, false en caso contrario.
     */
    boolean delete(I id);
}
