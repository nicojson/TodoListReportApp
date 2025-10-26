package tecnm.celaya.edu.mx.todolistreportapp.model;

import java.util.Objects;

/**
 * Clase POJO que representa la entidad 'Categorias' de la base de datos.
 */
public class Categoria {

    private int idCategoria;
    private String nombre;

    public Categoria() {}

    public Categoria(int idCategoria, String nombre) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
    }

    // Getters y Setters

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Es importante sobreescribir toString() para que el ComboBox muestre el nombre.
     */
    @Override
    public String toString() {
        return nombre;
    }

    /**
     * Dos objetos Categoria son iguales si sus IDs son iguales.
     * Esto es CRUCIAL para que el CheckComboBox pueda encontrar y marcar las categorías correctas.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return idCategoria == categoria.idCategoria;
    }

    /**
     * Siempre que se sobreescribe equals(), se debe sobreescribir hashCode().
     */
    @Override
    public int hashCode() {
        return Objects.hash(idCategoria);
    }
}
