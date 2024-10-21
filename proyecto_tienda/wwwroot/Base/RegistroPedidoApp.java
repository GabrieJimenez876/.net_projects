import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegistroPedidoApp extends JFrame {
    private Connection connection;
    private JComboBox<String> clienteComboBox;
    private JComboBox<String> productoComboBox;
    private JTextField cantidadField;
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel totalLabel;
    private double total = 0.0;

    // Listas para almacenar IDs y precios
    private List<Integer> productoIds = new ArrayList<>();
    private List<Double> productoPrecios = new ArrayList<>();

    public RegistroPedidoApp() {
        // Configuración de la ventana
        setTitle("Registrar Pedido");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel superior para selección de cliente y productos
        JPanel topPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        clienteComboBox = new JComboBox<>();
        productoComboBox = new JComboBox<>();
        cantidadField = new JTextField();
        JButton agregarButton = new JButton("Agregar Producto");

        topPanel.add(new JLabel("Cliente:"));
        topPanel.add(clienteComboBox);
        topPanel.add(new JLabel("Producto:"));
        topPanel.add(productoComboBox);
        topPanel.add(new JLabel("Cantidad:"));
        topPanel.add(cantidadField);
        topPanel.add(new JLabel("")); // Espacio en blanco
        topPanel.add(agregarButton);
        add(topPanel, BorderLayout.NORTH);

        // Conectar a la base de datos y cargar los datos
        connectToDatabase();
        cargarClientes();
        cargarProductos();

        // Configuración de la tabla para los productos agregados
        tableModel = new DefaultTableModel(new String[]{"Producto", "Cantidad", "Precio Unitario", "Subtotal"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel inferior para mostrar el total y botón de registro
        JPanel bottomPanel = new JPanel(new BorderLayout());
        totalLabel = new JLabel("Total: $0.0");
        JButton registrarButton = new JButton("Registrar Pedido");
        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(registrarButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Acción para el botón de agregar producto
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarProducto();
            }
        });

        // Acción para el botón de registrar pedido
        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarPedido();
            }
        });
    }

    // Conexión a la base de datos
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

    // Cargar clientes en el JComboBox
    private void cargarClientes() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT cliente_id, nombre FROM Clientes");
            while (resultSet.next()) {
                int clienteId = resultSet.getInt("cliente_id");
                String nombre = resultSet.getString("nombre");
                clienteComboBox.addItem(clienteId + " - " + nombre);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes: " + ex.getMessage());
        }
    }

    // Cargar productos en el JComboBox y almacenar IDs y precios
    private void cargarProductos() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT producto_id, nombre, precio FROM Productos");
            while (resultSet.next()) {
                int productoId = resultSet.getInt("producto_id");
                String nombre = resultSet.getString("nombre");
                double precio = resultSet.getDouble("precio");

                productoComboBox.addItem(nombre);
                productoIds.add(productoId);
                productoPrecios.add(precio);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + ex.getMessage());
        }
    }

    // Agregar producto al pedido
    private void agregarProducto() {
        int selectedProductIndex = productoComboBox.getSelectedIndex();
        String productoNombre = (String) productoComboBox.getSelectedItem();
        double precio = productoPrecios.get(selectedProductIndex);

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadField.getText());
            if (cantidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa una cantidad válida.");
            return;
        }

        double subtotal = precio * cantidad;
        total += subtotal;
        totalLabel.setText("Total: $" + total);

        tableModel.addRow(new Object[]{productoNombre, cantidad, precio, subtotal});
        cantidadField.setText("");
    }

    // Registrar el pedido en la base de datos
    private void registrarPedido() {
        int selectedClienteIndex = clienteComboBox.getSelectedIndex();
        int clienteId = Integer.parseInt(clienteComboBox.getItemAt(selectedClienteIndex).split(" - ")[0]);

        try {
            // Registrar el pedido
            String pedidoQuery = "INSERT INTO Pedidos (cliente_id, fecha_pedido, estado, total) VALUES (?, CURRENT_DATE, 'Pendiente', ?)";
            PreparedStatement pedidoStmt = connection.prepareStatement(pedidoQuery, Statement.RETURN_GENERATED_KEYS);
            pedidoStmt.setInt(1, clienteId);
            pedidoStmt.setDouble(2, total);
            pedidoStmt.executeUpdate();

            ResultSet generatedKeys = pedidoStmt.getGeneratedKeys();
            generatedKeys.next();
            int pedidoId = generatedKeys.getInt(1);

            // Registrar detalles del pedido
            String detalleQuery = "INSERT INTO DetallesPedido (pedido_id, producto_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
            PreparedStatement detalleStmt = connection.prepareStatement(detalleQuery);
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int productoId = productoIds.get(productoComboBox.getSelectedIndex());
                int cantidad = (int) tableModel.getValueAt(i, 1);
                double precioUnitario = (double) tableModel.getValueAt(i, 2);

                detalleStmt.setInt(1, pedidoId);
                detalleStmt.setInt(2, productoId);
                detalleStmt.setInt(3, cantidad);
                detalleStmt.setDouble(4, precioUnitario);
                detalleStmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Pedido registrado exitosamente.");
            limpiarPedido();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar el pedido: " + ex.getMessage());
        }
    }

    // Limpiar el pedido para un nuevo registro
    private void limpiarPedido() {
        tableModel.setRowCount(0);
        total = 0.0;
        totalLabel.setText("Total: $0.0");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RegistroPedidoApp().setVisible(true);
            }
        });
    }
}
