package tecnm.celaya.edu.mx.todolistreportapp.model;

import tecnm.celaya.edu.mx.todolistreportapp.dao.UsuarioDao;
import tecnm.celaya.edu.mx.todolistreportapp.dao.UsuarioDaoImpl;

import java.util.Optional;

/**
 * El modelo para la lógica de inicio de sesión.
 * Utiliza la capa DAO para autenticar usuarios.
 */
public class LoginModel {

    private final UsuarioDao usuarioDao;

    public LoginModel() {
        this.usuarioDao = new UsuarioDaoImpl();
    }

    /**
     * Autentica a un usuario.
     *
     * @param username El nombre de usuario.
     * @param password La contraseña del usuario.
     * @return true si el usuario y la contraseña son correctos, false en caso contrario.
     */
    public boolean authenticate(String username, String password) {
        // Busca al usuario por su nombre de usuario
        Optional<Usuario> usuarioOptional = usuarioDao.findByUsername(username);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            // NOTA: Aquí es donde se debe comparar la contraseña hasheada.
            // Por ahora, comparamos texto plano.
            // Ejemplo con hasheo: return BCrypt.checkpw(password, usuario.getContrasena());
            return password.equals(usuario.getContrasena());
        }

        // Si el usuario no existe, la autenticación falla.
        return false;
    }
}
