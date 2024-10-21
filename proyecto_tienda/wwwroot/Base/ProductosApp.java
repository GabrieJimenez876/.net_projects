import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductosApp extends JFrame {
    private Connection connection;
    private JCheckBox filtroUltimosCheckBox;
    private JList<String> productosList;
    private DefaultListModel<String> listModel;
    private List<Integer> productoIds = new ArrayList<>();
    private JButton verDetalleButton;

    public ProductosApp() {
        // Configuración de la ventana
        setTitle("Lista de Productos");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Inicialización de componentes
        filtroUltimosCheckBox = new JCheckBox("Mostrar solo los últimos agregados");
        listModel = new DefaultListModel<>();
        productosList = new JList<>(listModel);
        verDetalleButton = new JButton("Ver Detalle");

        // Panel para el filtro y el botón
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new FlowLayout());
        panelSuperior.add(filtroUltimosCheckBox);
        panelSuperior.add(verDetalleButton);

        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(productosList), BorderLayout.CENTER);

        // Conectar a la base de datos y cargar productos
        connectToDatabase();
        cargarProductos(false);

        // Acción para el checkbox de filtro
        filtroUltimosCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean filtrarUltimos = filtroUltimosCheckBox.isSelected();
                cargarProductos(filtrarUltimos);
            }
        });

        // Acción para el botón de ver detalle
        verDetalleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = productosList.getSelectedIndex();
                if (selectedIndex != -1) {
                    int productoId = productoIds.get(selectedIndex);
                    new DetalleProductoWindow(productoId).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(ProductosApp.this, "Seleccione un producto.");
                }
            }
        });
    }

    // Método para conectar a la base de datos
    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/TiendaVirtual";
            String user = "root";
            String password = "password";
            connection = DriverManager.getConnection(url, user, password);
            JOptionPane.showMessageDialog(this, "Conexión Exitosa a la Base de Datos");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
        }
    }

    // Método para cargar productos desde la base de datos
    private void cargarProductos(boolean filtrarUltimos) {
        listModel.clear();
        productoIds.clear();
        
        String query = "SELECT producto_id, nombre FROM Productos";
        if (filtrarUltimos) {
            query += " ORDER BY producto_id DESC LIMIT 5";
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int productoId = resultSet.getInt("producto_id");
                String nombre = resultSet.getString("nombre");

                listModel.addElement(nombre);
                productoIds.add(productoId);
            }

            if (listModel.isEmpty()) {
                listModel.addElement("No hay productos disponibles.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ProductosApp().setVisible(true);
            }
        });
    }
}

// Ventana para mostrar los detalles de un producto
class DetalleProductoWindow extends JFrame {
    private Connection connection;
    private int productoId;
    private JLabel nombreLabel;
    private JTextArea descripcionTextArea;
    private JLabel precioLabel;
    private JLabel stockLabel;

    public DetalleProductoWindow(int productoId) {
        this.productoId = productoId;

        // Configuración de la ventana
        setTitle("Detalle del Producto");
        setSize(300, 250);
        setLayout(new BorderLayout());

        nombreLabel = new JLabel();
        descripcionTextArea = new JTextArea();
        descripcionTextArea.setEditable(false);
        descripcionTextArea.setLineWrap(true);
        descripcionTextArea.setWrapStyleWord(true);
        precioLabel = new JLabel();
        stockLabel = new JLabel();

        // Panel para los detalles del producto
        JPanel detallePanel = new JPanel();
        detallePanel.setLayout(new GridLayout(4, 1, 5, 5));
        detallePanel.add(nombreLabel);
        detallePanel.add(new JScrollPane(descripcionTextArea));
        detallePanel.add(precioLabel);
        detallePanel.add(stockLabel);

        add(detallePanel, BorderLayout.CENTER);

        // Conectar a la base de datos y cargar detalles del producto
        connectToDatabase();
        cargarDetalleProducto();
    }

    // Método para conectar a la base de datos
    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/TiendaVirtual";
            String user = "root";
            String password = "password";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
        }
    }

    // Método para cargar los detalles del producto desde la base de datos
    private void cargarDetalleProducto() {
        try {
            String query = "SELECT nombre, descripcion, precio, stock FROM Productos WHERE producto_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, productoId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                String descripcion = resultSet.getString("descripcion");
                double precio = resultSet.getDouble("precio");
                int stock = resultSet.getInt("stock");

                nombreLabel.setText("Nombre: " + nombre);
                descripcionTextArea.setText("Descripción: " + descripcion);
                precioLabel.setText("Precio: $" + precio);
                stockLabel.setText("Stock: " + stock);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontraron detalles del producto.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar detalles del producto: " + ex.getMessage());
        }
    }
}
