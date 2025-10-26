package tecnm.celaya.edu.mx.todolistreportapp.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión a la base de datos usando un patrón Singleton.
 */
public class DatabaseConnection {
    private static Connection conn = null;
    private static final String HOSTNAME = "localhost";
    private static final String DBNAME = "ToDoListDB";
    private static final String DBPORT = "3306";
    private static final String DBUSER = "topicos_demo";
    private static final String DBPASS = "newPassword";
    private static final String URL = "jdbc:mysql://" + HOSTNAME + ":" + DBPORT + "/" + DBNAME + "?serverTimezone=UTC";

    private DatabaseConnection() {
        // Constructor privado para evitar instanciación.
    }

    public static synchronized Connection getConnection() {
        if (conn == null) {
            try {
                // Cargar el driver explícitamente (buena práctica en algunas configuraciones)
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("Intentando conectar a la base de datos en: " + URL);
                conn = DriverManager.getConnection(URL, DBUSER, DBPASS);
                System.out.println("¡Conexión a la base de datos exitosa!");
            } catch (ClassNotFoundException e) {
                System.err.println("Error Crítico: Driver JDBC de MySQL no encontrado.");
                throw new RuntimeException("No se pudo encontrar el driver de la base de datos.", e);
            } catch (SQLException e) {
                System.err.println("Error Crítico: No se pudo conectar a la base de datos.");
                System.err.println("SQLState: " + e.getSQLState());
                System.err.println("ErrorCode: " + e.getErrorCode());
                System.err.println("Mensaje: " + e.getMessage());
                throw new RuntimeException("Fallo en la conexión a la base de datos.", e);
            }
        }
        return conn;
    }

    public static void disconnect() {
        if (conn != null) {
            try {
                conn.close();
                conn = null; // Importante para permitir una reconexión futura
                System.out.println("Conexión a la base de datos terminada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
