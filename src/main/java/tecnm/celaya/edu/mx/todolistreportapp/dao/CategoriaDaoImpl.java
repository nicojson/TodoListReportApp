package tecnm.celaya.edu.mx.todolistreportapp.dao;

import tecnm.celaya.edu.mx.todolistreportapp.config.DatabaseConnection;
import tecnm.celaya.edu.mx.todolistreportapp.model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoriaDaoImpl implements CategoriaDao {

    private Connection getConnection() {
        return DatabaseConnection.getConnection();
    }

    @Override
    public List<Categoria> findAll() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM Categorias ORDER BY nombre";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                categorias.add(new Categoria(rs.getInt("id_categoria"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar todas las categorías: " + e.getMessage());
        }
        return categorias;
    }

    // --- Métodos CRUD no implementados por brevedad, pero deberían estar aquí ---

    @Override
    public Optional<Categoria> findById(Integer id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean save(Categoria categoria) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean update(Categoria categoria) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean delete(Integer id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
