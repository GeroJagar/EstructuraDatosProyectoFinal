package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.Actually;
import co.edu.uniquindio.Actually.modelo.ContenidoAcademico;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class InicioControlador {

    private final Actually actually = Actually.getInstance();

    @FXML
    private VBox contenedorContenido; // Vínculo con el VBox del FXML

    @FXML
    public void initialize() {
        contenedorContenido.getChildren().clear();

        if (actually.getContenidos().isEmpty()) {
            Label vacio = new Label("No hay contenidos disponibles.");
            vacio.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
            contenedorContenido.getChildren().add(vacio);
            return;
        }

        for (ContenidoAcademico contenido : actually.getContenidos().values()) {
            VBox card = new VBox(5);
            card.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0; -fx-border-color: #ccc;");

            Label label = new Label("Título: " + contenido.getTitulo()
                    + "\nTema: " + contenido.getTema()
                    + "\nAutor: " + contenido.getAutor()
                    + "\nPuntuación promedio: " + String.format("%.2f", contenido.calcularPuntuacion()));
            label.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

            TextArea area = new TextArea(contenido.getContenido());
            area.setWrapText(true);
            area.setEditable(false);
            area.setPrefRowCount(5);

            card.getChildren().addAll(label, area);
            contenedorContenido.getChildren().add(card);
        }
    }

    @FXML
    public void irAlLogin(javafx.event.ActionEvent event) {
        actually.loadStage("/ventanas/login.fxml", event);
    }
}
