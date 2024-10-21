import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class example { // Cambié el nombre de la clase a Example con mayúscula al inicio por convención
    public static void main(String[] args) { // Corrijo la declaración de la función main

        String host = "127.0.0.1"; // Definir host
        String user = "root"; // Usuario de MySQL
        String password = ""; // Contraseña de MySQL
        int port = 3306; // Puerto de MySQL
        String database = "ejemplo"; // Nombre de la base de datos

        // Construir la URL de conexión
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;

        Connection connection = null; // Inicializar la conexión

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Cargar el driver de MySQL

            // Establecer la conexión
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión exitosa a la base de datos.");

        } catch (ClassNotFoundException e) { // Capturar excepción de clase no encontrada
            System.out.println("Librería no encontrada: " + e.getMessage());
        } catch (SQLException e) { // Capturar excepción SQL
            System.out.println("Error de conexión: " + e.getMessage());
        } finally {
            // Cerrar la conexión si fue establecida
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Conexión cerrada.");
                } catch (SQLException e) {
                    System.out.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }
}
