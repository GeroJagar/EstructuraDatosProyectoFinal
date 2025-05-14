package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.Actually;
import co.edu.uniquindio.Actually.modelo.ContenidoAcademico;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.util.List;

public class HomepageController {

    @FXML public VBox contenedorContenido;
    @FXML public TextField searchField;

    private final Actually actually = Actually.getInstance();

    @FXML
    public void initialize(){
        cargarTodosLosContenidos();
    }

    private void cargarTodosLosContenidos() {
        contenedorContenido.getChildren().clear();

        if (actually.getContenidos().isEmpty()) {
            Label vacio = new Label("No hay contenidos disponibles.");
            vacio.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
            contenedorContenido.getChildren().add(vacio);
            return;
        }

        for (ContenidoAcademico contenido : actually.getContenidos().values()) {
            agregarVistaDeContenido(contenido);
        }
    }

    private void agregarVistaDeContenido(ContenidoAcademico contenido) {
        VBox card = new VBox(5);
        card.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0; -fx-border-color: #ccc;");

        Label label = new Label("Título: " + contenido.getTitulo()
                + "\nTema: " + contenido.getTema()
                + "\nAutor: " + contenido.getAutor()
                + "\nPuntuación promedio: " + String.format("%.2f", contenido.calcularPuntuacion()));
        TextArea area = new TextArea(contenido.getContenido());
        area.setWrapText(true);
        area.setEditable(false);
        area.setPrefRowCount(5);

        card.getChildren().addAll(label, area);
        contenedorContenido.getChildren().add(card);
    }

    @FXML
    public void buscarContenido(KeyEvent event) throws Exception {
        String clave = searchField.getText().toLowerCase();
        contenedorContenido.getChildren().clear();
        if(clave.isEmpty()){
            cargarTodosLosContenidos();
        } else {
            List<ContenidoAcademico> resultado = actually.buscarContenido(clave);
            if (resultado != null) {
                for (ContenidoAcademico contenidoAcademico : resultado) {
                    agregarVistaDeContenido(contenidoAcademico);
                }
            }
        }
    }

    private void mostrarMensaje(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setContentText(mensaje);
        alert.show();
    }

}
