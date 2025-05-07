package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.Actually;
import co.edu.uniquindio.Actually.modelo.ContenidoAcademico;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class InicioControlador {

    private final Actually actually = Actually.getInstance();

    @FXML
    private VBox contenedorContenido; // Vínculo con el VBox del FXML

    @FXML
    public void initialize() {
        // Cargar contenidos al iniciar la vista
        for (ContenidoAcademico contenido : actually.getContenidos().values()) {
            Label label = new Label("Título: " + contenido.getTitulo() + "\nTema: " + contenido.getTema()
                    + "\nAutor: " + contenido.getAutor() + "\n" + contenido.getContenido() + "\n");
            label.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0; -fx-border-color: #ccc;");
            contenedorContenido.getChildren().add(label);
        }
    }

    @FXML
    public void irAlLogin(javafx.event.ActionEvent event) {
        actually.loadStage("/ventanas/login.fxml", event);
    }
}
