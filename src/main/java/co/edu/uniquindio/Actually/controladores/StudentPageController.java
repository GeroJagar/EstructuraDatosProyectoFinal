package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.Actually;
import co.edu.uniquindio.Actually.modelo.ContenidoAcademico;
import co.edu.uniquindio.Actually.modelo.Estudiante;
import co.edu.uniquindio.Actually.modelo.TEMA;
import co.edu.uniquindio.Actually.modelo.TIPOCONTENIDO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

public class StudentPageController {

    @FXML public ComboBox<String> cbCriterioBusqueda;
    @FXML public TextField txtClaveBusqueda;
    @FXML public ScrollPane scrollContenidos;
    @FXML public VBox contenedorContenido;
    @FXML public VBox contenidoPanel;
    @FXML public HBox searchHBox;

    // Campos del formulario de metadatos
    @FXML private TextField txtTitulo;
    @FXML private ComboBox<TEMA> cbTema;
    @FXML private ComboBox<TIPOCONTENIDO> cbTipoContenido;
    @FXML private TextField txtAutor;

    private final Actually actually = Actually.getInstance();
    private File archivoSeleccionado;

    @FXML
    public void initialize() {
        // Configuración inicial
        cbCriterioBusqueda.getItems().addAll("titulo", "autor", "tema");
        mostrarTodoElContenido(null);
    }

    @FXML
    public void mostrarTodoElContenido(MouseEvent event) {
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
        VBox card = new VBox(10);
        card.setStyle("-fx-padding: 15; -fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5;");

        Label label = new Label(String.format(
                "Título: %s\nTema: %s\nAutor: %s\nTipo: %s\nPuntuación: %.2f",
                contenido.getTitulo(),
                contenido.getTema(),
                contenido.getAutor(),
                contenido.getTipoContenido(),
                contenido.calcularPuntuacion()
        ));
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Mostrar contenido según su tipo
        if (contenido.getTipoContenido() == TIPOCONTENIDO.TEXTO) {
            TextArea area = new TextArea(contenido.getContenido());
            area.setWrapText(true);
            area.setEditable(false);
            area.setPrefRowCount(5);
            area.setStyle("-fx-font-size: 13px;");
            card.getChildren().add(area);
        }
        else if (contenido.getTipoContenido() == TIPOCONTENIDO.PDF) {
            Button btnVerPdf = new Button("Ver PDF");
            btnVerPdf.setStyle("-fx-background-color: #4a6baf; -fx-text-fill: white;");
            btnVerPdf.setOnAction(e -> mostrarPdf(contenido.getContenido().replace("PDF:", "")));
            card.getChildren().add(btnVerPdf);
        }
        else if (contenido.getTipoContenido() == TIPOCONTENIDO.VIDEO) {
            Media media = new Media(new File(contenido.getContenido().replace("VIDEO:", "")).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);
            mediaView.setFitWidth(400);
            mediaView.setFitHeight(225);

            HBox controles = new HBox(10);
            controles.setStyle("-fx-alignment: center;");

            Button playBtn = new Button("▶");
            playBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
            playBtn.setOnAction(e -> mediaPlayer.play());

            Button pauseBtn = new Button("⏸");
            pauseBtn.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black;");
            pauseBtn.setOnAction(e -> mediaPlayer.pause());

            Button stopBtn = new Button("⏹");
            stopBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
            stopBtn.setOnAction(e -> mediaPlayer.stop());

            controles.getChildren().addAll(playBtn, pauseBtn, stopBtn);
            card.getChildren().addAll(mediaView, controles);
        }

        // Botón de valoración
        Button btnValorar = new Button("Valorar contenido");
        btnValorar.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white;");
        btnValorar.setOnAction(e -> mostrarDialogoValoracion(contenido));

        String estudianteId = actually.getUsuarioActivo().getId();
        boolean yaValorado = contenido.getValoraciones().stream()
                .anyMatch(v -> v.getEstudianteId().equals(estudianteId));

        boolean esAutor = false;
        if (actually.getUsuarioActivo() instanceof Estudiante estudiante) {
            esAutor = estudiante.getContenidosSubidos().stream()
                    .anyMatch(c -> c.getId().equals(contenido.getId()));
        }

        if (yaValorado || esAutor) {
            btnValorar.setDisable(true);
            btnValorar.setText(esAutor ? "Este contenido es tuyo" : "Ya valorado");
            btnValorar.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");
        }

        card.getChildren().addAll(label, btnValorar);
        contenedorContenido.getChildren().add(card);
    }

    private void mostrarPdf(String rutaPdf) {
        try {
            // Normalizar la ruta y verificar existencia
            File archivoPdf = new File(rutaPdf.replace("PDF:", ""));
            if (!archivoPdf.exists()) {
                mostrarMensaje(Alert.AlertType.ERROR, "El archivo PDF no existe en la ruta:\n" + archivoPdf.getAbsolutePath());
                return;
            }

            // Intentar abrir con aplicación nativa primero
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(archivoPdf);
                    return;
                } catch (IOException e) {
                    System.out.println("No se pudo abrir con visor nativo, intentando con WebView...");
                }
            }

            // Si falla, usar WebView con PDF.js
            WebView webView = new WebView();
            Stage stage = new Stage();

            try {
                String pdfJsViewerUrl = "https://mozilla.github.io/pdf.js/web/viewer.html?file=" +
                        URLEncoder.encode(archivoPdf.toURI().toString(), "UTF-8");
                webView.getEngine().load(pdfJsViewerUrl);
            } catch (Exception e) {
                webView.getEngine().load(archivoPdf.toURI().toString());
            }

            stage.setScene(new Scene(webView, 900, 650));
            stage.setTitle("PDF: " + archivoPdf.getName());
            stage.show();

        } catch (Exception e) {
            mostrarMensaje(Alert.AlertType.ERROR,
                    "Error al mostrar el PDF:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarDialogoValoracion(ContenidoAcademico contenido) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Valorar Contenido");
        dialog.setHeaderText("Ingresa un puntaje del 1 al 5");
        dialog.setContentText("Puntaje:");

        dialog.showAndWait().ifPresent(input -> {
            try {
                int puntaje = Integer.parseInt(input);
                if (puntaje < 1 || puntaje > 5) {
                    throw new NumberFormatException();
                }
                String estudianteId = actually.getUsuarioActivo().getId();
                actually.valorarContenido(estudianteId, contenido.getId(), puntaje);
                mostrarMensaje(Alert.AlertType.INFORMATION, "Valoración registrada con éxito");
                mostrarTodoElContenido(null);
            } catch (NumberFormatException e) {
                mostrarMensaje(Alert.AlertType.ERROR, "Ingresa un número válido entre 1 y 5");
            } catch (Exception e) {
                mostrarMensaje(Alert.AlertType.ERROR, e.getMessage());
            }
        });
    }

    private void mostrarMensaje(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setContentText(mensaje);
        alert.show();
    }

    @FXML
    public void buscarContenido(ActionEvent event) {
        String criterio = cbCriterioBusqueda.getValue();
        String clave = txtClaveBusqueda.getText();
        contenedorContenido.getChildren().clear();
        try {
            if (criterio == null || criterio.isEmpty()) {
                List<ContenidoAcademico> resultados = actually.buscarContenido(clave);
                for (ContenidoAcademico contenido : resultados) {
                    agregarVistaDeContenido(contenido);
                }
            } else {
                ContenidoAcademico resultado = actually.buscarContenido(criterio, clave);
                if (resultado != null) {
                    agregarVistaDeContenido(resultado);
                } else {
                    mostrarMensaje(Alert.AlertType.WARNING, "No se encontraron resultados.");
                }
            }
        } catch (Exception e) {
            mostrarMensaje(Alert.AlertType.ERROR, "Error en la búsqueda: " + e.getMessage());
        }
    }

    @FXML
    public void mostrarSubirContenido(MouseEvent event) {
        searchHBox.setVisible(false);
        searchHBox.setManaged(false);
        contenidoPanel.setVisible(true);
        contenidoPanel.setManaged(true);
        scrollContenidos.setVisible(false);
        scrollContenidos.setManaged(false);

        // Limpiar formulario al mostrar
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        txtTitulo.clear();
        cbTema.getSelectionModel().clearSelection();
        cbTipoContenido.getSelectionModel().clearSelection();
        archivoSeleccionado = null;
    }

    public void seleccionarArchivo(ActionEvent event) {
    }
}
