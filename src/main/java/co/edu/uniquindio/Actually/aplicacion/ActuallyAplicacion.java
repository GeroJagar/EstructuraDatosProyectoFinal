package co.edu.uniquindio.Actually.aplicacion;

import co.edu.uniquindio.Actually.Actually;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * La clase Actually es la clase principal de la aplicación Actually.
 * Extiende la clase Application de JavaFX y proporciona el método de inicio
 * para iniciar la aplicación.
 */
public class ActuallyAplicacion extends Application {

    /**
     * El metodo start es el punto de entrada de la aplicación JavaFX.
     * Carga la interfaz de usuario desde un archivo FXML y muestra la ventana principal de la aplicación.
     *
     * @param stage El escenario principal de la aplicación.
     * @throws Exception Si ocurre un error al cargar o mostrar la interfaz de usuario.
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(ActuallyAplicacion.class.getResource("/ventanas/common/login.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Actually Application");
        stage.show();
    }

    /**
     * El método main es el punto de entrada principal de la aplicación.
     * Se invoca cuando se ejecuta la aplicación y se encarga de inicializar la instancia
     * de Actually y lanzar la aplicación JavaFX.
     *
     * @param args Los argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        // Inicializar la instancia de Storify
        Actually.getInstance().inicializar();

        // Lanzar la aplicación JavaFX
        launch(ActuallyAplicacion.class, args);
    }
}