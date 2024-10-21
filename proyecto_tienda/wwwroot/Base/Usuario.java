import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/registro")
public class Usuario extends HttpServlet {

    // Configuración de la base de datos
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tienda virtual";
    private static final String DB_USER = "creador";
    private static final String DB_PASSWORD = "contraseña1";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String gmail = request.getParameter("gmail");
        String password = request.getParameter("password");
        String suscripcion = request.getParameter("suscripcion") != null ? "Sí" : "No";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Verificar si el usuario ya está registrado
            if (isUserRegistered(conn, gmail)) {
                // Usuario ya registrado
                response.addCookie(new Cookie("usuario", gmail));
                sendResponse(response, "Bienvenido de nuevo a la comunidad");
            } else {
                // Registrar nuevo usuario
                registerUser(conn, gmail, password, suscripcion);
                response.addCookie(new Cookie("usuario", gmail));
                sendResponse(response, "Bienvenido a la comunidad");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendResponse(response, "Error de registro");
        }
    }

    private boolean isUserRegistered(Connection conn, String gmail) throws SQLException {
        String sqlSelect = "SELECT * FROM usuarios WHERE gmail = ?";
        try (PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect)) {
            stmtSelect.setString(1, gmail);
            ResultSet rs = stmtSelect.executeQuery();
            return rs.next(); // Retorna true si el usuario está registrado
        }
    }

    private void registerUser(Connection conn, String gmail, String password, String suscripcion) throws SQLException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String sqlInsert = "INSERT INTO usuarios (gmail, password, suscripcion) VALUES (?, ?, ?)";
        try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
            stmtInsert.setString(1, gmail);
            stmtInsert.setString(2, hashedPassword);
            stmtInsert.setString(3, suscripcion);
            stmtInsert.executeUpdate();
        }
    }

    private void sendResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }
}
