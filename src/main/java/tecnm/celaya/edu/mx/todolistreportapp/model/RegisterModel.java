package tecnm.celaya.edu.mx.todolistreportapp.model;

import tecnm.celaya.edu.mx.todolistreportapp.dao.UsuarioDao;
import tecnm.celaya.edu.mx.todolistreportapp.dao.UsuarioDaoImpl;

import java.util.Optional;

/**
 * El modelo para la lógica de registro de usuarios.
 * Utiliza la capa DAO para crear nuevos usuarios.
 */
public class RegisterModel {

    private final UsuarioDao usuarioDao;

    public RegisterModel() {
        this.usuarioDao = new UsuarioDaoImpl();
    }

    /**
     * Registra un nuevo usuario.
     *
     * @param username El nombre de usuario para el nuevo usuario.
     * @param password La contraseña para el nuevo usuario.
     * @return true si el usuario fue registrado exitosamente, false en caso contrario.
     */
    public boolean registerUser(String username, String password) {
        // 1. Verificar si el usuario ya existe para evitar registros duplicados
        Optional<Usuario> existingUser = usuarioDao.findByUsername(username);
        if (existingUser.isPresent()) {
            System.err.println("El nombre de usuario ya está en uso.");
            return false; // El usuario ya existe
        }

        // 2. Crear el nuevo objeto Usuario
        // NOTA: Aquí es donde se debería hashear la contraseña antes de guardarla.
        // Ejemplo: String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Usuario newUser = new Usuario();
        newUser.setNombreUsuario(username);
        newUser.setContrasena(password); // Guardar la contraseña hasheada, no en texto plano

        // 3. Guardar el nuevo usuario usando el DAO
        return usuarioDao.save(newUser);
    }
}
