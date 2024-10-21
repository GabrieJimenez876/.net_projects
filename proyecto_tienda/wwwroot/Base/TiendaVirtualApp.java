import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TiendaVirtualApp extends JFrame {
    private Connection connection;
    private JComboBox<String> categoriaComboBox;
    private JButton verProductosButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Integer> categoriaIds = new ArrayList<>();

    public TiendaVirtualApp() {
        // Configuración de la ventana
        setTitle("Tienda Virtual - Selección de Categorías");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de selección de categorías
        JPanel topPanel = new JPanel();
        categoriaComboBox = new JComboBox<>();
        verProductosButton = new JButton("Ver Productos");

        topPanel.add(new JLabel("Seleccione una Categoría:"));
        topPanel.add(categoriaComboBox);
        topPanel.add(verProductosButton);

        add(topPanel, BorderLayout.NORTH);

        // Panel para los botones de conexión y visualización de datos
        JPanel buttonPanel = new JPanel();
        JButton connectButton = new JButton("Conectar");
        JButton showClientsButton = new JButton("Mostrar Clientes");

        buttonPanel.add(connectButton);
        buttonPanel.add(showClientsButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Configuración de la tabla
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Acción para el botón de conexión
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToDatabase();
                cargarCategorias();
            }
        });

        // Acción para mostrar productos de la categoría seleccionada
        verProductosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedCategoryIndex = categoriaComboBox.getSelectedIndex();
                if (selectedCategoryIndex >= 0) {
                    int categoriaId = categoriaIds.get(selectedCategoryIndex);
                    showTable("Productos WHERE categoria_id = " + categoriaId);
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione una categoría.");
                }
            }
        });

        // Acción para mostrar clientes
        showClientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (connection != null) {
                    showTable("Clientes");
                } else {
                    JOptionPane.showMessageDialog(null, "No conectado a la base de datos.");
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

    // Método para cargar categorías en el JComboBox
    private void cargarCategorias() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT categoria_id, nombre FROM Categorias");
            categoriaComboBox.removeAllItems();
            categoriaIds.clear();
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

    // Método para mostrar registros en la tabla
    private void showTable(String tableName) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

            // Limpiar el modelo de la tabla
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);

            // Obtener metadata para nombres de columnas
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(metaData.getColumnName(i));
            }

            // Llenar los datos en la tabla
            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(row);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al mostrar datos: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TiendaVirtualApp().setVisible(true));
    }
}
