import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaProductoApp extends JFrame {
    private Connection connection;
    private JComboBox<String> categoriaComboBox;
    private JTextField categoriaField;
    private JTextField productoField;
    private JTextField descripcionField;
    private JTextField precioField;
    private JTextField stockField;
    private JButton agregarCategoriaButton;
    private JButton agregarProductoButton;

    // Listas para almacenar los IDs de las categorías
    private List<Integer> categoriaIds = new ArrayList<>();

    public CategoriaProductoApp() {
        // Configuración de la ventana
        setTitle("Administrar Categorías y Productos");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 2, 5, 5));

        // Inicialización de los componentes
        categoriaComboBox = new JComboBox<>();
        categoriaField = new JTextField();
        productoField = new JTextField();
        descripcionField = new JTextField();
        precioField = new JTextField();
        stockField = new JTextField();
        agregarCategoriaButton = new JButton("Agregar Categoría");
        agregarProductoButton = new JButton("Agregar Producto");

        // Añadiendo los componentes a la ventana
        add(new JLabel("Nueva Categoría:"));
        add(categoriaField);
        add(new JLabel(""));
        add(agregarCategoriaButton);
        add(new JLabel("Seleccionar Categoría:"));
        add(categoriaComboBox);
        add(new JLabel("Nombre del Producto:"));
        add(productoField);
        add(new JLabel("Descripción:"));
        add(descripcionField);
        add(new JLabel("Precio:"));
        add(precioField);
        add(new JLabel("Stock:"));
        add(stockField);
        add(new JLabel(""));
        add(agregarProductoButton);

        // Conectar a la base de datos y cargar las categorías
        connectToDatabase();
        cargarCategorias();

        // Acción para el botón de agregar categoría
        agregarCategoriaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarCategoria();
            }
        });

        // Acción para el botón de agregar producto
        agregarProductoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProducto();
            }
        });
    }

    // Método para conectar a la base de datos
    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/zonavip";
            String user = "root";
            String password = "password";
            connection = DriverManager.getConnection(url, user, password);
            JOptionPane.showMessageDialog(this, "Conexión Exitosa a la Base de Datos");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
        }
    }

    // Método para cargar categorías en el JComboBox
    private void cargarCategorias() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT categoria_id, nombre FROM Categorias");
            while (resultSet.next()) {
                int categoriaId = resultSet.getInt("categoria_id");
                String nombre = resultSet.getString("nombre");
                categoriaComboBox.addItem(nombre);
                categoriaIds.add(categoriaId);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar categorías: " + ex.getMessage());
        }
    }

    // Método para agregar una nueva categoría
    private void agregarCategoria() {
        String categoriaNombre = categoriaField.getText();

        if (categoriaNombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa un nombre para la categoría.");
            return;
        }

        try {
            String query = "INSERT INTO Categorias (nombre) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, categoriaNombre);
            statement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Categoría agregada exitosamente.");
            categoriaComboBox.addItem(categoriaNombre);
            categoriaField.setText("");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar la categoría: " + ex.getMessage());
        }
    }

    // Método para agregar un producto bajo la categoría seleccionada
    private void agregarProducto() {
        int selectedCategoryIndex = categoriaComboBox.getSelectedIndex();
        int categoriaId = categoriaIds.get(selectedCategoryIndex);

        String nombreProducto = productoField.getText();
        String descripcion = descripcionField.getText();
        String precio = precioField.getText();
        String stock = stockField.getText();

        if (nombreProducto.isEmpty() || precio.isEmpty() || stock.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos obligatorios.");
            return;
        }

        try {
            String query = "INSERT INTO Productos (nombre, descripcion, precio, stock, categoria_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nombreProducto);
            statement.setString(2, descripcion);
            statement.setBigDecimal(3, new java.math.BigDecimal(precio));
            statement.setInt(4, Integer.parseInt(stock));
            statement.setInt(5, categoriaId);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Producto agregado exitosamente.");
            limpiarCamposProducto();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar el producto: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa un número válido para el precio y el stock.");
        }
    }

    // Método para limpiar los campos de producto después de agregar uno
    private void limpiarCamposProducto() {
        productoField.setText("");
        descripcionField.setText("");
        precioField.setText("");
        stockField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CategoriaProductoApp().setVisible(true);
            }
        });
    }
}
