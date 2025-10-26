package tecnm.celaya.edu.mx.todolistreportapp.dao;

import tecnm.celaya.edu.mx.todolistreportapp.config.DatabaseConnection;
import tecnm.celaya.edu.mx.todolistreportapp.model.Tarea;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TareaDaoImpl implements TareaDao {

    private Connection getConnection() {
        return DatabaseConnection.getConnection();
    }

    @Override
    public List<Tarea> findByUsuarioId(int usuarioId) {
        List<Tarea> tareas = new ArrayList<>();
        String sql = "SELECT t.*, c.nombre as nombre_categoria FROM Tareas t " +
                     "LEFT JOIN Tarea_Categoria tc ON t.id_tarea = tc.id_tarea " +
                     "LEFT JOIN Categorias c ON tc.id_categoria = c.id_categoria " +
                     "WHERE t.id_usuario = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tareas.add(mapRowToTarea(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar tareas por ID de usuario: " + e.getMessage());
        }
        return tareas;
    }

    @Override
    public Optional<Tarea> findById(Integer id) {
        String sql = "SELECT t.*, c.nombre as nombre_categoria FROM Tareas t " +
                     "LEFT JOIN Tarea_Categoria tc ON t.id_tarea = tc.id_tarea " +
                     "LEFT JOIN Categorias c ON tc.id_categoria = c.id_categoria " +
                     "WHERE t.id_tarea = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToTarea(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar tarea por ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Tarea> findAll() {
        List<Tarea> tareas = new ArrayList<>();
        String sql = "SELECT t.*, c.nombre as nombre_categoria FROM Tareas t " +
                     "LEFT JOIN Tarea_Categoria tc ON t.id_tarea = tc.id_tarea " +
                     "LEFT JOIN Categorias c ON tc.id_categoria = c.id_categoria";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                tareas.add(mapRowToTarea(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar todas las tareas: " + e.getMessage());
        }
        return tareas;
    }

    @Override
    public boolean save(Tarea tarea) {
        String sql = "INSERT INTO Tareas (id_usuario, nombre, descripcion, estatus, fecha_vencimiento) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, tarea.getIdUsuario());
            pstmt.setString(2, tarea.getNombre());
            pstmt.setString(3, tarea.getDescripcion());
            pstmt.setString(4, tarea.getEstatus());
            if (tarea.getFechaVencimiento() != null) {
                pstmt.setTimestamp(5, Timestamp.valueOf(tarea.getFechaVencimiento()));
            } else {
                pstmt.setNull(5, Types.TIMESTAMP);
            }
            
            boolean saved = pstmt.executeUpdate() > 0;
            if (saved) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        tarea.setIdTarea(generatedKeys.getInt(1));
                    }
                }
            }
            return saved;
        } catch (SQLException e) {
            System.err.println("Error al guardar la tarea: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Tarea tarea) {
        String sql = "UPDATE Tareas SET nombre = ?, descripcion = ?, estatus = ?, fecha_vencimiento = ? WHERE id_tarea = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, tarea.getNombre());
            pstmt.setString(2, tarea.getDescripcion());
            pstmt.setString(3, tarea.getEstatus());
            if (tarea.getFechaVencimiento() != null) {
                pstmt.setTimestamp(4, Timestamp.valueOf(tarea.getFechaVencimiento()));
            } else {
                pstmt.setNull(4, Types.TIMESTAMP);
            }
            pstmt.setInt(5, tarea.getIdTarea());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar la tarea: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM Tareas WHERE id_tarea = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar la tarea: " + e.getMessage());
            return false;
        }
    }

    private Tarea mapRowToTarea(ResultSet rs) throws SQLException {
        Tarea tarea = new Tarea();
        tarea.setIdTarea(rs.getInt("id_tarea"));
        tarea.setIdUsuario(rs.getInt("id_usuario"));
        tarea.setNombre(rs.getString("nombre"));
        tarea.setDescripcion(rs.getString("descripcion"));
        tarea.setEstatus(rs.getString("estatus"));
        Timestamp vencimiento = rs.getTimestamp("fecha_vencimiento");
        if (vencimiento != null) {
            tarea.setFechaVencimiento(vencimiento.toLocalDateTime());
        }
        tarea.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
        tarea.setNombreCategoria(rs.getString("nombre_categoria"));
        return tarea;
    }
}
