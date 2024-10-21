import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class registrarUsuario {

    private static final String URL = "jdbc:mysql://localhost:3306/zonavip";
    private static final String USER = "creador";
    private static final String PASSWORD = "contraseña1"; 
    public void registrar(String gmail, String password, String suscripcion) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO usuarios (gmail, password, suscripcion) VALUES (?, ?, ?)";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, gmail);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, suscripcion);
                
                int filasAfectadas = preparedStatement.executeUpdate();
                
                if (filasAfectadas > 0) {
                    System.out.println("Usuario registrado exitosamente.");
                } else {
                    System.out.println("No se pudo registrar el usuario.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al registrar el usuario: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        registrarUsuario registrar = new registrarUsuario();
        
        registrar.registrar("ejemplo@gmail.com", "tu_contraseña", "Sí");
    }
}
