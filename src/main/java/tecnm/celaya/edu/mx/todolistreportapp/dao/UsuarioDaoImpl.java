package tecnm.celaya.edu.mx.todolistreportapp.dao;

import tecnm.celaya.edu.mx.todolistreportapp.config.DatabaseConnection;
import tecnm.celaya.edu.mx.todolistreportapp.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de la interfaz UsuarioDao.
 * Contiene la lógica de acceso a datos (código JDBC) para la tabla 'Usuarios'.
 */
public class UsuarioDaoImpl implements UsuarioDao {

    private Connection getConnection() {
        return DatabaseConnection.getConnection();
    }

    @Override
    public Optional<Usuario> findById(Integer id) {
        String sql = "SELECT * FROM Usuarios WHERE id_usuario = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUsuario(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        String sql = "SELECT * FROM Usuarios WHERE nombre_usuario = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUsuario(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por nombre de usuario: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                usuarios.add(mapRowToUsuario(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar todos los usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    @Override
    public boolean save(Usuario usuario) {
        String sql = "INSERT INTO Usuarios (nombre_usuario, contrasena) VALUES (?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombreUsuario());
            pstmt.setString(2, usuario.getContrasena()); // NOTA: Hashear la contraseña antes de guardarla.
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Usuario usuario) {
        String sql = "UPDATE Usuarios SET nombre_usuario = ?, contrasena = ? WHERE id_usuario = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNombreUsuario());
            pstmt.setString(2, usuario.getContrasena());
            pstmt.setInt(3, usuario.getIdUsuario());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM Usuarios WHERE id_usuario = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método de utilidad para mapear una fila de un ResultSet a un objeto Usuario.
     */
    private Usuario mapRowToUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("id_usuario"),
                rs.getString("nombre_usuario"),
                rs.getString("contrasena")
        );
    }
}
