package org.proyectofinal.frontend;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar la vista desde un archivo FXML (si usas FXML)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/proyectofinal/frontend/vista.fxml"));
        StackPane root = loader.load();

        // Crear la escena y mostrarla
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Mi Aplicación JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
