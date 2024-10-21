import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox mainContent = createMainContent();
        VBox footer = createFooter();
        
        VBox root = new VBox(20, mainContent, footer);
        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("Aplicación JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createMainContent() {
        VBox mainContent = new VBox(10);
        mainContent.setAlignment(Pos.CENTER);
        Label offerTitle = new Label("100% Original compralo ya");
        Button buyButton = new Button("COMPRAR AHORA");

        mainContent.getChildren().addAll(offerTitle, buyButton);

        return mainContent;
    }

    private VBox createFooter() {
        VBox footer = new VBox(10);
        footer.setAlignment(Pos.CENTER);
        footer.setStyle("-fx-background-color: #00bcd4; -fx-padding: 20px;");

        Label contactLabel = new Label("Dirección: 71 Pennington Lane, CT 06066");
        contactLabel.setStyle("-fx-text-fill: white;");

        HBox links = new HBox(20);
        links.setAlignment(Pos.CENTER);
        Label link1 = new Label("Acerca de Nosotros");
        Label link2 = new Label("Políticas de Privacidad");
        Label link3 = new Label("Términos y condiciones");
        links.getChildren().addAll(link1, link2, link3);

        footer.getChildren().addAll(contactLabel, links);

        return footer;
    }
}
