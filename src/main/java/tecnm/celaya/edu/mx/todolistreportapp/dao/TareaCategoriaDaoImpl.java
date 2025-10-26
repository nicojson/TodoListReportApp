package tecnm.celaya.edu.mx.todolistreportapp.dao;

import tecnm.celaya.edu.mx.todolistreportapp.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TareaCategoriaDaoImpl implements TareaCategoriaDao {

    private Connection getConnection() {
        return DatabaseConnection.getConnection();
    }

    @Override
    public boolean associate(int idTarea, int idCategoria) {
        // Primero, eliminamos cualquier asociación existente para esta tarea para simplificar la lógica.
        // Esto asegura que una tarea solo tenga una categoría a la vez, como en el ComboBox.
        disassociateByTareaId(idTarea);

        String sql = "INSERT INTO Tarea_Categoria (id_tarea, id_categoria) VALUES (?, ?)";
        System.out.println("[DIAGNÓSTICO] TareaCategoriaDao: Asociando tarea ID: " + idTarea + " con categoría ID: " + idCategoria);

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, idTarea);
            pstmt.setInt(2, idCategoria);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al asociar tarea con categoría: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean disassociateByTareaId(int idTarea) {
        String sql = "DELETE FROM Tarea_Categoria WHERE id_tarea = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, idTarea);
            pstmt.executeUpdate(); // No necesitamos saber cuántas filas se borraron, solo que se ejecute.
            return true;
        } catch (SQLException e) {
            System.err.println("Error al desasociar categorías de la tarea: " + e.getMessage());
            return false;
        }
    }
}
