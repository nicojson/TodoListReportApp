package tecnm.celaya.edu.mx.todolistreportapp.dao;

import tecnm.celaya.edu.mx.todolistreportapp.config.DatabaseConnection;
import tecnm.celaya.edu.mx.todolistreportapp.model.Tarea;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TareaDaoImpl implements TareaDao {

    private Connection getConnection() {
        return DatabaseConnection.getConnection();
    }

    @Override
    public List<Tarea> findByUsuarioId(int usuarioId) {
        Map<Integer, Tarea> tareaMap = new LinkedHashMap<>();
        String sql = "SELECT t.*, c.nombre as nombre_categoria FROM Tareas t " +
                     "LEFT JOIN Tarea_Categoria tc ON t.id_tarea = tc.id_tarea " +
                     "LEFT JOIN Categorias c ON tc.id_categoria = c.id_categoria " +
                     "WHERE t.id_usuario = ? ORDER BY t.fecha_creacion DESC, c.nombre";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int idTarea = rs.getInt("id_tarea");
                    Tarea tarea = tareaMap.get(idTarea);

                    if (tarea == null) {
                        tarea = mapRowToTarea(rs);
                        tareaMap.put(idTarea, tarea);
                    } else {
                        String categoriaExistente = tarea.getNombreCategoria();
                        String nuevaCategoria = rs.getString("nombre_categoria");
                        if (categoriaExistente != null && !categoriaExistente.isEmpty() && nuevaCategoria != null) {
                            tarea.setNombreCategoria(categoriaExistente + ", " + nuevaCategoria);
                        } else if (nuevaCategoria != null) {
                            tarea.setNombreCategoria(nuevaCategoria);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar tareas por ID de usuario: " + e.getMessage());
        }
        return new ArrayList<>(tareaMap.values());
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

    @Override
    public Optional<Tarea> findById(Integer id) {
        List<Tarea> allTasks = findAll();
        return allTasks.stream().filter(t -> t.getIdTarea() == id).findFirst();
    }

    @Override
    public List<Tarea> findAll() {
        return findByUsuarioId(-1);
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

    @Override
    public Map<String, Integer> getTaskCountsByStatus(int userId, LocalDate startDate, LocalDate endDate) {
        Map<String, Integer> statusCounts = new HashMap<>();
        String sql = "SELECT estatus, COUNT(*) as count FROM Tareas " +
                     "WHERE id_usuario = ? AND fecha_creacion BETWEEN ? AND ? " +
                     "GROUP BY estatus";
        System.out.println("\n[DIAGNÓSTICO 1 - DAO] Ejecutando consulta para gráfico de estado...");
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setDate(2, java.sql.Date.valueOf(startDate));
            pstmt.setDate(3, java.sql.Date.valueOf(endDate));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("[DIAGNÓSTICO 1 - DAO] Fila cruda de la BD -> Estado: '" + rs.getString("estatus") + "', Conteo: " + rs.getInt("count"));
                    statusCounts.put(rs.getString("estatus"), rs.getInt("count"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener conteo de tareas por estado: " + e.getMessage());
        }
        System.out.println("[DIAGNÓSTICO 1 - DAO] Mapa final de estados que devuelve el DAO: " + statusCounts.toString());
        return statusCounts;
    }

    @Override
    public Map<String, Integer> getTaskCategoryCounts(int userId, LocalDate startDate, LocalDate endDate) {
        Map<String, Integer> categoryCounts = new HashMap<>();
        String sql = "SELECT c.nombre, COUNT(t.id_tarea) as count FROM Categorias c " +
                     "LEFT JOIN Tarea_Categoria tc ON c.id_categoria = tc.id_categoria " +
                     "LEFT JOIN Tareas t ON tc.id_tarea = t.id_tarea AND t.id_usuario = ? AND t.fecha_creacion BETWEEN ? AND ? " +
                     "GROUP BY c.id_categoria, c.nombre ORDER BY c.nombre";
        System.out.println("\n[DIAGNÓSTICO 1 - DAO] Ejecutando consulta para gráfico de categorías...");
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setDate(2, java.sql.Date.valueOf(startDate));
            pstmt.setDate(3, java.sql.Date.valueOf(endDate));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("[DIAGNÓSTICO 1 - DAO] Fila cruda de la BD -> Categoría: '" + rs.getString("nombre") + "', Conteo: " + rs.getInt("count"));
                    categoryCounts.put(rs.getString("nombre"), rs.getInt("count"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener conteo de tareas por categoría: " + e.getMessage());
        }
        System.out.println("[DIAGNÓSTICO 1 - DAO] Mapa final de categorías que devuelve el DAO: " + categoryCounts.toString());
        return categoryCounts;
    }
}
