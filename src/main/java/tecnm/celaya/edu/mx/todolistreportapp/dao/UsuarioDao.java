package tecnm.celaya.edu.mx.todolistreportapp.dao;

import tecnm.celaya.edu.mx.todolistreportapp.model.Usuario;

import java.util.Optional;

/**
 * Interfaz DAO específica para la entidad Usuario.
 * Hereda las operaciones CRUD de la interfaz genérica Dao y puede añadir métodos específicos.
 */
public interface UsuarioDao extends Dao<Usuario, Integer> {

    /**
     * Busca un usuario por su nombre de usuario.
     * @param username El nombre de usuario a buscar.
     * @return un Optional que contiene el Usuario si se encuentra, o un Optional vacío si no.
     */
    Optional<Usuario> findByUsername(String username);

}
