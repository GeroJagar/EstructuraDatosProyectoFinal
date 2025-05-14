package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.Actually;
import co.edu.uniquindio.Actually.modelo.*;
import co.edu.uniquindio.Actually.utilidades.ArchivoUtilidades;
import co.edu.uniquindio.Actually.utilidades.FileUploader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

public class PanelEstudianteControlador {

    private final Actually actually = Actually.getInstance();
    private final FileUploader fileUploader = new FileUploader();
    private final ArchivoUtilidades archivoUtil = new ArchivoUtilidades();

    @FXML private VBox contenidoPanel;
    @FXML private VBox contenedorContenido;
    @FXML private ScrollPane scrollContenidos;
    @FXML private TextField txtClaveBusqueda;
    @FXML private ComboBox<String> cbCriterioBusqueda;
    @FXML private HBox contenedorBusqueda;

    // Campos del formulario de metadatos
    @FXML private TextField txtTitulo;
    @FXML private ComboBox<TEMA> cbTema;
    @FXML private ComboBox<TIPOCONTENIDO> cbTipoContenido;
    @FXML private TextField txtAutor;

    private File archivoSeleccionado;

    @FXML
    public void initialize() {
        // Configuración inicial
        cbCriterioBusqueda.getItems().addAll("titulo", "autor", "tema");
        cbTema.getItems().setAll(TEMA.values());
        cbTipoContenido.getItems().setAll(TIPOCONTENIDO.values());

        mostrarTodoElContenido(null);
    }

    @FXML
    public void mostrarSubirContenido(MouseEvent event) {
        contenidoPanel.setVisible(true);
        contenidoPanel.setManaged(true);
        scrollContenidos.setVisible(false);
        scrollContenidos.setManaged(false);
        contenedorBusqueda.setVisible(false);
        contenedorBusqueda.setManaged(false);

        // Limpiar formulario al mostrar
        limpiarFormulario();
    }

    @FXML
    public void mostrarContenidoEstudiante(MouseEvent event) {
        contenidoPanel.setVisible(false);
        contenidoPanel.setManaged(false);
        scrollContenidos.setVisible(true);
        scrollContenidos.setManaged(true);
        contenedorBusqueda.setVisible(true);
        contenedorBusqueda.setManaged(true);
        cargarContenidosDelEstudiante();
    }

    @FXML
    public void mostrarTodoElContenido(MouseEvent event) {
        contenidoPanel.setVisible(false);
        contenidoPanel.setManaged(false);
        scrollContenidos.setVisible(true);
        scrollContenidos.setManaged(true);
        contenedorBusqueda.setVisible(true);
        contenedorBusqueda.setManaged(true);
        cargarTodosLosContenidos();
    }

    @FXML
    public void cerrarSesion(ActionEvent event) {
        actually.cerrarSesion(event);
    }

    @FXML
    public void seleccionarArchivo() {
        if (!validarCampos()) {
            mostrarMensaje(Alert.AlertType.WARNING, "Por favor complete todos los campos obligatorios");
            return;
        }

        Stage stage = (Stage) contenidoPanel.getScene().getWindow();
        archivoSeleccionado = fileUploader.abrirSelectorDeArchivo(
                stage,
                cbTipoContenido.getValue().toString().toLowerCase()
        );

        if (archivoSeleccionado != null) {
            subirContenido();
        }
    }

    private boolean validarCampos() {
        return txtTitulo.getText() != null && !txtTitulo.getText().isBlank() &&
                cbTema.getValue() != null &&
                cbTipoContenido.getValue() != null;
    }

    private void subirContenido() {
        try {
            // Crear el objeto contenido con los metadatos
            ContenidoAcademico contenido = new ContenidoAcademico();
            contenido.setTitulo(txtTitulo.getText());
            contenido.setTema(cbTema.getValue());
            contenido.setAutor(txtAutor.getText());
            contenido.setTipoContenido(cbTipoContenido.getValue());
            contenido.setId(archivoUtil.generarId());

            // Procesar el archivo según su tipo
            switch (contenido.getTipoContenido()) {
                case TEXTO:
                    contenido.setContenido(archivoUtil.leerArchivoComoTexto(archivoSeleccionado));
                    break;
                case PDF:
                    contenido.setContenido("PDF:" + archivoSeleccionado.getAbsolutePath());
                    break;
                case VIDEO:
                    contenido.setContenido("VIDEO:" + archivoSeleccionado.getAbsolutePath());
                    break;
            }

            // Guardar el contenido en el sistema
            actually.subirContenidoAcademico(contenido);
            mostrarMensaje(Alert.AlertType.INFORMATION, "Contenido subido correctamente");

            // Limpiar y actualizar la vista
            limpiarFormulario();
            mostrarTodoElContenido(null);

        } catch (Exception e) {
            mostrarMensaje(Alert.AlertType.ERROR, "Error al subir el contenido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void limpiarFormulario() {
        txtTitulo.clear();
        cbTema.getSelectionModel().clearSelection();
        cbTipoContenido.getSelectionModel().clearSelection();
        archivoSeleccionado = null;
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
            agregarVistaDeContenido(contenido);
        }
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

    private void mostrarMensaje(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setContentText(mensaje);
        alert.show();
    }
}