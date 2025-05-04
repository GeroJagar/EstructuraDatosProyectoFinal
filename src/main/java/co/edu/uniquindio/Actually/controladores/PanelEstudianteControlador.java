package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.Actually;
import co.edu.uniquindio.Actually.modelo.ContenidoAcademico;
import co.edu.uniquindio.Actually.modelo.Estudiante;
import co.edu.uniquindio.Actually.utilidades.ArchivoUtilidades;
import co.edu.uniquindio.Actually.utilidades.FileUploader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;

public class PanelEstudianteControlador {

    private final Actually actually = Actually.getInstance();
    private final FileUploader fileUploader = new FileUploader();
    private final ArchivoUtilidades parser = new ArchivoUtilidades();

    @FXML
    private VBox contenidoPanel;

    @FXML
    private VBox contenedorContenido;

    @FXML
    private ScrollPane scrollContenidos; // Nueva referencia al scroll para visibilidad

    @FXML
    public void initialize() {
        mostrarContenidoEstudiante(null); // Por defecto carga los contenidos
    }

    @FXML
    public void mostrarSubirContenido(MouseEvent event) {
        contenidoPanel.setVisible(true);
        scrollContenidos.setVisible(false);
    }

    @FXML
    public void mostrarContenidoEstudiante(MouseEvent event) {
        contenidoPanel.setVisible(false);
        scrollContenidos.setVisible(true);
        cargarContenidosDelEstudiante();
    }

    @FXML
    public void cerrarSesion(ActionEvent event) {
        actually.cerrarSesion();
    }

    @FXML
    public void subirContenido(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        File archivoSeleccionado = fileUploader.abrirSelectorDeArchivo(stage);

        if (archivoSeleccionado != null) {
            try {
                ContenidoAcademico contenido = parser.procesarArchivoTxt(archivoSeleccionado);
                actually.subirContenidoAcademico(contenido);
                mostrarMensaje(Alert.AlertType.INFORMATION, "Contenido subido correctamente");

                mostrarContenidoEstudiante(null); // Volver a mostrar la lista actualizada

            } catch (IOException e) {
                mostrarMensaje(Alert.AlertType.ERROR, "Error al leer el archivo: " + e.getMessage());
            } catch (Exception e) {
                mostrarMensaje(Alert.AlertType.ERROR, "Error al subir el contenido: " + e.getMessage());
            }
        }
    }

    private void cargarContenidosDelEstudiante() {
        contenedorContenido.getChildren().clear();

        if (!(actually.getUsuarioActivo() instanceof Estudiante estudiante)) {
            mostrarMensaje(Alert.AlertType.ERROR, "No hay un estudiante activo");
            return;
        }

        if (estudiante.getContenidosSubidos().isEmpty()) {
            Label vacio = new Label("Aún no has subido contenidos.");
            vacio.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
            contenedorContenido.getChildren().add(vacio);
            return;
        }

        for (ContenidoAcademico contenido : estudiante.getContenidosSubidos()) {
            Label label = new Label("Título: " + contenido.getTitulo()
                    + "\nTema: " + contenido.getTema()
                    + "\nAutor: " + contenido.getAutor()
                    + "\n" + contenido.getContenido() + "\n");
            label.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0; -fx-border-color: #ccc;");
            contenedorContenido.getChildren().add(label);
        }
    }

    private void mostrarMensaje(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setContentText(mensaje);
        alert.show();
    }

}
