package tecnm.celaya.edu.mx.todolistreportapp.dao;

import tecnm.celaya.edu.mx.todolistreportapp.config.DatabaseConnection;
import tecnm.celaya.edu.mx.todolistreportapp.model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TareaCategoriaDaoImpl implements TareaCategoriaDao {

    private Connection getConnection() {
        return DatabaseConnection.getConnection();
    }

    @Override
    public boolean associate(int idTarea, int idCategoria) {
        String sql = "INSERT INTO Tarea_Categoria (id_tarea, id_categoria) VALUES (?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, idTarea);
            pstmt.setInt(2, idCategoria);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Ignorar error de clave duplicada, ya que significa que la asociación ya existe.
            if (e.getErrorCode() != 1062) {
                System.err.println("Error al asociar tarea con categoría: " + e.getMessage());
            }
            return false;
        }
    }

    @Override
    public boolean disassociateByTareaId(int idTarea) {
        String sql = "DELETE FROM Tarea_Categoria WHERE id_tarea = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, idTarea);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al desasociar categorías de la tarea: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Categoria> findCategoriesByTareaId(int idTarea) {
        List<Categoria> categoriasAsociadas = new ArrayList<>();
        String sql = "SELECT c.id_categoria, c.nombre FROM Categorias c " +
                     "JOIN Tarea_Categoria tc ON c.id_categoria = tc.id_categoria " +
                     "WHERE tc.id_tarea = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, idTarea);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    categoriasAsociadas.add(new Categoria(rs.getInt("id_categoria"), rs.getString("nombre")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar categorías por ID de tarea: " + e.getMessage());
        }
        return categoriasAsociadas;
    }
}
