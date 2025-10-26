package tecnm.celaya.edu.mx.todolistreportapp.model;

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
}
