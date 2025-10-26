package tecnm.celaya.edu.mx.todolistreportapp.model;

/**
 * Clase POJO (Plain Old Java Object) que representa la entidad 'Usuarios' de la base de datos.
 * Se utiliza para transferir datos entre la capa DAO y la capa de negocio (Modelos).
 */
public class Usuario {

    private int idUsuario;
    private String nombreUsuario;
    private String contrasena;

    // Constructor vacío
    public Usuario() {}

    // Constructor con todos los campos
    public Usuario(int idUsuario, String nombreUsuario, String contrasena) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
    }

    // Getters y Setters

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                '}';
    }
}
