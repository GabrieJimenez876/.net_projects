import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistroProductoApp extends JFrame {
    private Connection connection;

    // Componentes de la interfaz
    private JTextField nombreField;
    private JTextArea descripcionArea;
    private JTextField precioField;
    private JTextField stockField;
    private JTextField categoriaField;
    private JButton registrarButton;

    public RegistroProductoApp() {
        // Configuración de la ventana
        setTitle("Registrar Producto");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2, 5, 5));

        // Inicialización de los componentes de la interfaz
        nombreField = new JTextField();
        descripcionArea = new JTextArea();
        precioField = new JTextField();
        stockField = new JTextField();
        categoriaField = new JTextField();
        registrarButton = new JButton("Registrar Producto");

        // Añadiendo los componentes a la ventana
        add(new JLabel("Nombre del Producto:"));
        add(nombreField);
        add(new JLabel("Descripción:"));
        add(new JScrollPane(descripcionArea));
        add(new JLabel("Precio:"));
        add(precioField);
        add(new JLabel("Stock:"));
        add(stockField);
        add(new JLabel("Categoría:"));
        add(categoriaField);
        add(new JLabel(""));  // Espacio en blanco
        add(registrarButton);

        // Conexión a la base de datos al inicio
        connectToDatabase();

        // Acción para el botón de registro
        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarProducto();
            }
        });
    }

    // Método para conectar a la base de datos
    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/TiendaVirtual"; // Cambia según tu configuración
            String user = "root"; // Cambia según tu configuración
            String password = "password"; // Cambia según tu configuración

            connection = DriverManager.getConnection(url, user, password);
            JOptionPane.showMessageDialog(this, "Conexión Exitosa a la Base de Datos");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
        }
    }

    // Método para registrar un producto en la base de datos
    private void registrarProducto() {
        String nombre = nombreField.getText();
        String descripcion = descripcionArea.getText();
        String precio = precioField.getText();
        String stock = stockField.getText();
        String categoria = categoriaField.getText();

        if (nombre.isEmpty() || precio.isEmpty() || stock.isEmpty() || categoria.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos obligatorios.");
            return;
        }

        try {
            String query = "INSERT INTO Productos (nombre, descripcion, precio, stock, categoria) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nombre);
            statement.setString(2, descripcion);
            statement.setBigDecimal(3, new java.math.BigDecimal(precio));
            statement.setInt(4, Integer.parseInt(stock));
            statement.setString(5, categoria);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Producto registrado exitosamente.");
                limpiarCampos();
            }

            statement.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar el producto: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa un número válido para el precio y el stock.");
        }
    }

    // Método para limpiar los campos después de registrar un producto
    private void limpiarCampos() {
        nombreField.setText("");
        descripcionArea.setText("");
        precioField.setText("");
        stockField.setText("");
        categoriaField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RegistroProductoApp().setVisible(true);
            }
        });
    }
}
