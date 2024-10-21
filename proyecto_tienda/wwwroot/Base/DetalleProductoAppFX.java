import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class DetalleProductoAppFX extends Application {
    private Connection connection;
    private int productoId;
    private ImageView imagenProducto;
    private Label nombreLabel;
    private Label precioLabel;
    private Label descripcionLabel;
    private VBox relacionadosPanel;

    // Constructor sin parámetros
    public DetalleProductoAppFX() {
        connectToDatabase();
    }

    // Setter para productoId
    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Detalle del Producto");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        BorderPane root = new BorderPane();

        VBox detallePanel = createProductoDetallePanel();
        root.setTop(detallePanel);

        relacionadosPanel = createProductosRelacionadosPanel();
        root.setBottom(relacionadosPanel);

        scrollPane.setContent(root);

        Scene scene = new Scene(scrollPane, 600, 800);
        primaryStage.setScene(scene);
        primaryStage.show();

        cargarDetalleProducto();
        cargarProductosRelacionados();
    }

    private VBox createProductoDetallePanel() {
        VBox detallePanel = new VBox(10);
        detallePanel.setAlignment(Pos.CENTER);
        detallePanel.setPadding(new Insets(20));

        imagenProducto = new ImageView(new Image("default-image.png"));
        imagenProducto.setFitHeight(300);
        imagenProducto.setPreserveRatio(true);

        nombreLabel = new Label("Nombre del Producto");
        precioLabel = new Label("Precio: $0.00");
        descripcionLabel = new Label("Descripción del producto.");
        descripcionLabel.setWrapText(true);

        Button añadirCarritoButton = new Button("Añadir al Carrito");
        configurarBoton(añadirCarritoButton);

        detallePanel.getChildren().addAll(imagenProducto, nombreLabel, precioLabel, descripcionLabel, añadirCarritoButton);

        return detallePanel;
    }

    private VBox createProductosRelacionadosPanel() {
        VBox relacionadosContainer = new VBox(10);
        relacionadosContainer.setAlignment(Pos.CENTER);
        relacionadosContainer.setPadding(new Insets(20));

        HBox relacionadosBox = new HBox(10);
        relacionadosBox.setAlignment(Pos.CENTER);
        relacionadosPanel = new VBox(10);
        relacionadosContainer.getChildren().addAll(new Label("Productos Relacionados"), relacionadosBox);

        return relacionadosContainer;
    }

    private void configurarBoton(Button boton) {
        boton.setStyle("-fx-background-color: black; -fx-text-fill: blue;");
        boton.setOnMouseEntered(e -> boton.setStyle("-fx-background-color: blue; -fx-text-fill: white;"));
        boton.setOnMouseExited(e -> boton.setStyle("-fx-background-color: black; -fx-text-fill: blue;"));
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/TiendaVirtual";
            String user = "root";
            String password = "password";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            System.out.println("Error de conexión: " + ex.getMessage());
        }
    }

    private void cargarDetalleProducto() {
        try {
            String query = "SELECT imagen, nombre, descripcion, precio FROM Productos WHERE producto_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, productoId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String imagen = resultSet.getString("imagen");
                String nombre = resultSet.getString("nombre");
                String descripcion = resultSet.getString("descripcion");
                double precio = resultSet.getDouble("precio");

                imagenProducto.setImage(new Image(imagen));
                nombreLabel.setText("Nombre: " + nombre);
                descripcionLabel.setText("Descripción: " + descripcion);
                precioLabel.setText("Precio: $" + precio);
            } else {
                System.out.println("No se encontraron detalles del producto.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar detalles del producto: " + ex.getMessage());
        }
    }

    private void cargarProductosRelacionados() {
        try {
            String query = "SELECT producto_id, imagen FROM Productos WHERE categoria_id = (SELECT categoria_id FROM Productos WHERE producto_id = ?) AND producto_id != ? LIMIT 4";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, productoId);
            statement.setInt(2, productoId);
            ResultSet resultSet = statement.executeQuery();

            HBox relacionadosBox = new HBox(10);
            relacionadosBox.setAlignment(Pos.CENTER);

            while (resultSet.next()) {
                int id = resultSet.getInt("producto_id");
                String imagen = resultSet.getString("imagen");

                ImageView relacionadoImageView = new ImageView(new Image(imagen));
                relacionadoImageView.setFitHeight(100);
                relacionadoImageView.setPreserveRatio(true);

                Button relacionadoButton = new Button();
                relacionadoButton.setGraphic(relacionadoImageView);
                configurarBoton(relacionadoButton);

                relacionadoButton.setOnAction(e -> {
                    DetalleProductoAppFX nuevaVentana = new DetalleProductoAppFX();
                    nuevaVentana.setProductoId(id);
                    nuevaVentana.start(new Stage());
                });

                relacionadosBox.getChildren().add(relacionadoButton);
            }
            relacionadosPanel.getChildren().add(relacionadosBox);
        } catch (SQLException ex) {
            System.out.println("Error al cargar productos relacionados: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
