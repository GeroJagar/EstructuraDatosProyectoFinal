package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.Actually;
import co.edu.uniquindio.Actually.modelo.*;
import co.edu.uniquindio.Actually.utilidades.ArchivoUtilidades;
import co.edu.uniquindio.Actually.utilidades.FileUploader;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class PanelEstudianteControlador {

    private final Actually actually = Actually.getInstance();
    private final FileUploader fileUploader = new FileUploader();
    private final ArchivoUtilidades archivoUtil = new ArchivoUtilidades();

    @FXML public Label nivelLabel;
    @FXML public Label puntosLabel;
    @FXML public ProgressBar progresoBar;
    @FXML public Label progresoLabel;
    @FXML public VBox panelSugerenciasGrupos;
    @FXML public VBox panelChats;
    @FXML public AnchorPane chatPanel;


    @FXML private VBox contenidoPanel;
    @FXML private VBox contenedorContenido;
    @FXML private ScrollPane scrollContenidos;
    @FXML private TextField txtClaveBusqueda;
    @FXML private ComboBox<String> cbCriterioBusqueda;
    @FXML private HBox searchHBox;
    @FXML public VBox panelSolicitudes;
    @FXML private VBox contenedorSugerenciasGrupos;

    // Panel Mi Perfil.
    @FXML public VBox panelMiPerfil;
    @FXML public Label nombreLabel;
    @FXML public Label idLabel;
    @FXML public Label correoLabel;

    @FXML
    private VBox contenedorSugerencias;
    @FXML
    private VBox panelSugerencias;
    @FXML
    private Button btnSugerencias;
    @FXML
    private ScrollPane scrollSugerencias;

    @FXML
    private ListView<Estudiante> ListAmigosChats;

    // Campos del formulario de metadatos
    @FXML private TextField txtTitulo;
    @FXML private ComboBox<TEMA> cbTema;
    @FXML private ComboBox<TIPOCONTENIDO> cbTipoContenido;
    @FXML private TextField txtAutor;

    @FXML private VBox panelAyuda;
    @FXML private ComboBox<TEMA> cbTemaAyuda;
    @FXML private ComboBox<Integer> cbUrgencia;
    @FXML private TextArea txtDescripcionAyuda;
    @FXML private ScrollPane scrollSolicitudes;
    @FXML private VBox contenedorSolicitudes;

    private File archivoSeleccionado;
    private Estudiante usuarioActivo;
    private final Set<GrupoEstudio> sugerenciasMostradas = new HashSet<>();


    @FXML
    public void initialize() {
        // Configuración inicial
        cbCriterioBusqueda.getItems().addAll("titulo", "autor", "tema");
        cbTema.getItems().setAll(TEMA.values());
        cbTipoContenido.getItems().setAll(TIPOCONTENIDO.values());

        // Configurar combobox de ayuda
        cbTemaAyuda.getItems().setAll(TEMA.values());
        cbUrgencia.getItems().addAll(1, 2, 3, 4, 5);

        mostrarTodoElContenido(null);

        // Labels en el panel "Mi Perfil"
        nombreLabel.setText("❥ Nombre: " + actually.getUsuarioActivo().getNombre());
        idLabel.setText("❥ ID: " + actually.getUsuarioActivo().getId());
        correoLabel.setText("❥ Correo: " + actually.getUsuarioActivo().getCorreo());
    }

    @FXML
    public void mostrarSubirContenido(MouseEvent event) {
        contenidoPanel.setVisible(true);
        contenidoPanel.setManaged(true);
        scrollContenidos.setVisible(false);
        scrollContenidos.setManaged(false);
        searchHBox.setVisible(false);
        searchHBox.setManaged(false);
        panelAyuda.setVisible(false);
        panelAyuda.setManaged(false);
        panelSolicitudes.setVisible(false);
        panelSolicitudes.setManaged(false);
        panelSugerencias.setVisible(false);
        panelSugerencias.setManaged(false);
        panelMiPerfil.setVisible(false);
        panelMiPerfil.setManaged(false);
        panelSugerenciasGrupos.setVisible(false);
        panelSugerenciasGrupos.setManaged(false);
        panelChats.setVisible(false);
        panelChats.setManaged(false);

        // Limpiar formulario al mostrar
        limpiarFormulario();
    }

    @FXML
    public void mostrarContenidoEstudiante(MouseEvent event) {
        contenidoPanel.setVisible(false);
        contenidoPanel.setManaged(false);
        scrollContenidos.setVisible(true);
        scrollContenidos.setManaged(true);
        searchHBox.setVisible(true);
        searchHBox.setManaged(true);
        cargarContenidosDelEstudiante();
    }

    @FXML
    public void mostrarTodoElContenido(MouseEvent event) {
        contenidoPanel.setVisible(false);
        contenidoPanel.setManaged(false);
        scrollContenidos.setVisible(true);
        scrollContenidos.setManaged(true);
        searchHBox.setVisible(true);
        searchHBox.setManaged(true);
        panelSugerencias.setVisible(false);
        panelSugerencias.setManaged(false);
        panelMiPerfil.setVisible(false);
        panelMiPerfil.setManaged(false);
        panelSugerenciasGrupos.setVisible(false);
        panelSugerenciasGrupos.setManaged(false);
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
        // Contenedor principal de la publicación (tarjeta)
        VBox card = new VBox(10);
        card.setStyle("-fx-padding: 15; -fx-background-color: white; -fx-border-color: #e0e0e0; " +
                "-fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 1);");

        // Encabezado (Autor y Tema)
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Circle avatar = new Circle(20, Color.TRANSPARENT); // Puedes reemplazar con una imagen real
        avatar.setStroke(Color.LIGHTGRAY);

        Label autorLabel = new Label(contenido.getAutor());
        autorLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #333; -fx-font-family: \"SansSerif\";");

        Label temaLabel = new Label("#" + contenido.getTema());
        temaLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4a6baf; -fx-font-family: \"SansSerif\";");

        Label contenidoLabel = new Label("    |     "+"★ Puntuación promedio: " + String.format("%.2f", contenido.calcularPuntuacion()));
        contenidoLabel.setStyle("-fx-font-family: 'SansSerif'; -fx-font-size: 16px; -fx-text-fill: #333;");

        header.getChildren().addAll(avatar, autorLabel, temaLabel);

        // Contenido principal (dinámico según tipo)
        StackPane contentPane = new StackPane();
        contentPane.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 10; -fx-border-radius: 8;");

        if (contenido.getTipoContenido() == TIPOCONTENIDO.TEXTO) {
            TextArea area = new TextArea(contenido.getContenido());
            area.setWrapText(true);
            area.setEditable(false);
            area.setPrefHeight(600);
            area.setStyle("""
                -fx-font-size: 14px;
                -fx-background-color: #f8f9fa;  /* Fondo gris claro */
                -fx-border-color: #e0e0e0;     /* Borde sutil */
                -fx-border-radius: 5;
                -fx-padding: 10;
                -fx-font-family: "SansSerif";
            """);

            area.setPrefRowCount(20);           // Altura basada en 20 líneas de texto
            area.setPrefWidth(400);            // Ancho recomendado

            contentPane.getChildren().add(area);
        } else if (contenido.getTipoContenido() == TIPOCONTENIDO.PDF) {
            Button btnVerPdf = new Button("📄 Ver PDF");
            btnVerPdf.setStyle("-fx-background-color: #4a6baf; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-family: \"SansSerif\";");
            btnVerPdf.setCursor(Cursor.HAND);
            btnVerPdf.setOnAction(e -> mostrarPdf(contenido.getContenido().replace("PDF:", "")));
            contentPane.getChildren().add(btnVerPdf);
        } else if (contenido.getTipoContenido() == TIPOCONTENIDO.VIDEO) {
            Media media = new Media(new File(contenido.getContenido().replace("VIDEO:", "")).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);
            mediaView.setFitWidth(400);  // Ancho ajustable
            mediaView.setFitHeight(225); // Altura ajustable

            StackPane videoStackPane = new StackPane();
            videoStackPane.setAlignment(Pos.CENTER); // ¡MAGIA PARA CENTRAR!
            videoStackPane.setStyle("-fx-background-color: black;"); // Fondo opcional
            videoStackPane.getChildren().add(mediaView);

            // Controles de video (HBox con botones clásicos)
            HBox controles = new HBox(10); // Espaciado entre botones: 10px
            controles.setAlignment(Pos.CENTER);
            controles.setStyle("-fx-background-color: rgba(0,0,0,0.7); -fx-padding: 10; -fx-background-radius: 0 0 8 8;");

            // Botón de Reproducir (verde)
            Button playBtn = new Button("▶");
            playBtn.setStyle("-fx-background-color: #91B243; -fx-text-fill: #000000; -fx-font-weight: bold;");
            playBtn.setCursor(Cursor.HAND);
            playBtn.setOnAction(e -> mediaPlayer.play());

            // Botón de Pausa (amarillo)
            Button pauseBtn = new Button("⏸");
            pauseBtn.setStyle("-fx-background-color: #CFB360; -fx-text-fill: #000000; -fx-font-weight: bold;");
            pauseBtn.setCursor(Cursor.HAND);
            pauseBtn.setOnAction(e -> mediaPlayer.pause());

            // Botón de Reiniciar (rojo)
            Button stopBtn = new Button("⏹");
            stopBtn.setStyle("-fx-background-color: #AC5A61; -fx-text-fill: #000000; -fx-font-weight: bold;");
            stopBtn.setCursor(Cursor.HAND);
            stopBtn.setOnAction(e -> mediaPlayer.stop());

            controles.getChildren().addAll(playBtn, pauseBtn, stopBtn);

            // Contenedor del video + controles
            VBox videoContainer = new VBox();
            videoContainer.setAlignment(Pos.CENTER);
            videoContainer.getChildren().addAll(mediaView, controles);
            videoContainer.setStyle("-fx-border-radius: 8; -fx-background-radius: 8;");

            contentPane.getChildren().add(videoContainer);
        }

        // Pie de publicación (Título y Puntuación)
        HBox footer = new HBox();
        footer.setSpacing(10);
        footer.setAlignment(Pos.CENTER_LEFT);

        Label tituloLabel = new Label(contenido.getTitulo());
        tituloLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #222; -fx-font-family: \"SansSerif\";");

        footer.getChildren().addAll(tituloLabel, contenidoLabel);

        // Botones de interacción (Like, Comentar, Compartir)
        HBox interactionBar = new HBox(15);
        interactionBar.setAlignment(Pos.CENTER_LEFT);

        Button likeBtn = createInteractionButton("❤ Me gusta", "Me gusta");
        likeBtn.setCursor(Cursor.HAND);
        Button commentBtn = createInteractionButton("💬 Comentar", "Comentar");
        commentBtn.setCursor(Cursor.HAND);
        Button shareBtn = createInteractionButton("↪ Compartir", "Compartir");
        shareBtn.setCursor(Cursor.HAND);
        Button rateBtn = new Button("★ Valorar");
        rateBtn.setStyle(" -fx-background-color: #FFD700; -fx-text-fill: #333; -fx-font-family: 'SansSerif'; " +
                "-fx-font-size: 14px; -fx-background-radius: 15; -fx-padding: 5 10; -fx-pref-width: 150;");
        rateBtn.setCursor(Cursor.HAND);
        rateBtn.setOnAction(e -> mostrarDialogoValoracion(contenido));

        interactionBar.getChildren().addAll(likeBtn, commentBtn, shareBtn, rateBtn);

        // Ensamblar la tarjeta
        card.getChildren().addAll(header, contentPane, footer, interactionBar);
        contenedorContenido.getChildren().add(card);
    }

    private Button createInteractionButton(String emoji, String tooltipText) {
        Button btn = new Button(emoji);
        btn.setStyle("-fx-background-color: transparent; -fx-font-size: 16px;");
        btn.setTooltip(new Tooltip(tooltipText));
        return btn;
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
                // Búsqueda general
                List<ContenidoAcademico> resultados = actually.buscarContenido(clave);
                for (ContenidoAcademico contenido : resultados) {
                    agregarVistaDeContenido(contenido);
                }
            } else {
                // Búsqueda por criterio específico
                List<ContenidoAcademico> resultados = actually.buscarContenido(criterio, clave);
                for (ContenidoAcademico contenido : resultados) {
                    agregarVistaDeContenido(contenido);
                }
            }
        } catch (Exception e) {
            mostrarMensaje(Alert.AlertType.WARNING, e.getMessage());
        }
    }

    private void mostrarMensaje(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setContentText(mensaje);
        alert.show();
    }

    public void onCancelarButtonClick(ActionEvent event) {
        goBack();
    }

    @FXML
    private void goBack() {
        // Ocultar todos los paneles especiales
        panelAyuda.setVisible(false);
        panelAyuda.setManaged(false);
        contenidoPanel.setVisible(false);
        contenidoPanel.setManaged(false);
        panelSolicitudes.setVisible(false);
        panelSolicitudes.setManaged(false);
        panelSugerencias.setVisible(false);
        panelSugerencias.setManaged(false);
        panelMiPerfil.setVisible(false);
        panelMiPerfil.setManaged(false);
        panelSugerenciasGrupos.setVisible(false);
        panelSugerenciasGrupos.setManaged(false);
        panelChats.setVisible(false);
        panelChats.setManaged(false);

        // Mostrar la vista principal
        searchHBox.setVisible(true);
        searchHBox.setManaged(true);
        scrollContenidos.setVisible(true);
        scrollContenidos.setManaged(true);

        // Actualizar contenido
        mostrarTodoElContenido(null);
    }

    @FXML
    public void mostrarPanelAyuda(MouseEvent event) {
        contenidoPanel.setVisible(false);
        scrollContenidos.setVisible(false);
        scrollSolicitudes.setVisible(false);
        searchHBox.setVisible(false);
        searchHBox.setManaged(false);
        panelSolicitudes.setVisible(false);
        panelSolicitudes.setManaged(false);
        panelSugerencias.setVisible(false);
        panelSugerencias.setManaged(false);
        panelMiPerfil.setVisible(false);
        panelMiPerfil.setManaged(false);
        panelSugerenciasGrupos.setVisible(false);
        panelSugerenciasGrupos.setManaged(false);
        panelChats.setVisible(false);
        panelChats.setManaged(false);

        panelAyuda.setVisible(true);
        panelAyuda.setManaged(true);
    }

    @FXML
    public void enviarSolicitudAyuda(ActionEvent event) {
        try {
            TEMA tema = cbTemaAyuda.getValue();
            Integer urgencia = cbUrgencia.getValue();
            String descripcion = txtDescripcionAyuda.getText();

            if (tema == null || urgencia == null || descripcion == null || descripcion.isBlank()) {
                mostrarMensaje(Alert.AlertType.WARNING, "Por favor complete todos los campos");
                return;
            }

            actually.crearSolicitudAyuda(tema, urgencia, descripcion);
            mostrarMensaje(Alert.AlertType.INFORMATION, "Solicitud enviada correctamente");

            // Limpiar campos
            cbTemaAyuda.getSelectionModel().clearSelection();
            cbUrgencia.getSelectionModel().clearSelection();
            txtDescripcionAyuda.clear();

        } catch (Exception e) {
            mostrarMensaje(Alert.AlertType.ERROR, "Error al enviar solicitud: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void mostrarSolicitudesPendientes(ActionEvent event) {
        panelAyuda.setVisible(false);
        panelAyuda.setManaged(false);
        scrollContenidos.setVisible(false);
        scrollContenidos.setManaged(false);
        panelMiPerfil.setVisible(false);
        panelMiPerfil.setManaged(false);
        panelSugerenciasGrupos.setVisible(false);
        panelSugerenciasGrupos.setManaged(false);

        scrollSolicitudes.setVisible(true);
        scrollSolicitudes.setManaged(true);
        panelSolicitudes.setVisible(true);
        panelSolicitudes.setManaged(true);
        contenedorSolicitudes.getChildren().clear();

        try {
            List<SolicitudAyuda> solicitudes = actually.listarSolicitudesPendientes();

            if (solicitudes.isEmpty()) {
                Label vacio = new Label("No hay solicitudes pendientes.");
                vacio.setStyle("-fx-text-fill: gray; -fx-font-size: 20px;");
                contenedorSolicitudes.getChildren().add(vacio);
                return;
            }

            for (SolicitudAyuda solicitud : solicitudes) {
                agregarVistaSolicitud(solicitud);
            }
        } catch (Exception e) {
            mostrarMensaje(Alert.AlertType.ERROR, "Error al cargar solicitudes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void agregarVistaSolicitud(SolicitudAyuda solicitud) {
        VBox card = new VBox(10);
        card.setStyle("""
            -fx-padding: 15;
            -fx-background-color: #F5F5F5;
            -fx-border-color: #e0e0e0;
            -fx-border-radius: 10;
            -fx-border-width: 0px;
            -fx-background-radius: 10;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 1);""");

// Encabezado (Usuario + Urgencia)
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

// Avatar del solicitante (círculo o imagen)
        Circle avatar = new Circle(20, Color.TRANSPARENT);
        avatar.setStroke(Color.LIGHTGRAY);

// Etiqueta de urgencia con color dinámico
        String colorUrgencia = switch(solicitud.getUrgencia()) {
            case 1 -> "#EB0013";  // Alta: rojo
            case 2 -> "#EB7900";  // Media urgente: Naranja
            case 3 -> "#EBC800";  // Media: Amarillo
            default -> "#B0EB00"; // Baja: verde
        };

        Label lblUrgencia = new Label("Urgencia: " + obtenerPrioridadTexto(solicitud.getUrgencia()));
        lblUrgencia.setStyle("-fx-font-size: 18px; -fx-font-family: \"SansSerif\"; -fx-text-fill: " + colorUrgencia + ";");

        Label solicitante = new Label(solicitud.getSolicitante().getNombre() + "   | ");
        solicitante.setStyle("-fx-font-size: 18px; -fx-font-family: 'SansSerif'; -fx-text-fill: #05242F;");
        header.getChildren().addAll(avatar, solicitante, lblUrgencia);

// Cuerpo de la tarjeta
        VBox body = new VBox(5);
        Label lblTema = new Label("Tema: " + solicitud.getTema().getName());
        lblTema.setStyle("-fx-font-family: 'SansSerif'; -fx-font-size: 18px; -fx-text-fill: #2C4A59;");

        TextArea areaDescripcion = new TextArea(solicitud.getDescripcion());
        areaDescripcion.setEditable(false);
        areaDescripcion.setWrapText(true);
        areaDescripcion.setStyle("-fx-font-family: 'SansSerif'; -fx-font-size: 18px; " +
                "-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5; -fx-padding: 10;");
        areaDescripcion.setPrefRowCount(3); // Altura para 3 líneas

// Pie de tarjeta (botón de acción)
        Button btnAtender = new Button("Resolver Solicitud");
        btnAtender.setStyle("-fx-background-color: #31545E; -fx-text-fill: #F5F5F5; -fx-font-family: 'SansSerif'; " +
                "-fx-font-size: 18px; -fx-background-radius: 40; -fx-pref-width: 200; -fx-pref-height: 35; ");
        btnAtender.setCursor(Cursor.HAND);
        btnAtender.setOnAction(e -> mostrarDialogoResolverSolicitud(solicitud.getId()));

// Ensamblar la tarjeta
        body.getChildren().addAll(lblTema, areaDescripcion);
        card.getChildren().addAll(header, body, btnAtender);
        contenedorSolicitudes.getChildren().add(card);
    }

    private void mostrarDialogoResolverSolicitud(String idSolicitud) {
        // Crear un diálogo personalizado para resolver la solicitud
        Dialog<ContenidoAcademico> dialog = new Dialog<>();
        dialog.setTitle("Resolver Solicitud");
        dialog.setHeaderText("Sube contenido que resuelva esta solicitud");

        // Configurar botones
        ButtonType resolverButtonType = new ButtonType("Resolver", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(resolverButtonType, ButtonType.CANCEL);

        // Crear formulario para el contenido
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        TextField txtTituloContenido = new TextField();
        txtTituloContenido.setPromptText("Título del contenido");
        ComboBox<TIPOCONTENIDO> cbTipoContenido = new ComboBox<>();
        cbTipoContenido.getItems().setAll(TIPOCONTENIDO.values());
        TextField txtAutorContenido = new TextField();
        txtAutorContenido.setPromptText("Autor");

        // Obtener tema de la solicitud para asignarlo al contenido
        TEMA temaSolicitud = null;
        try {
            SolicitudAyuda solicitud = actually.obtenerSolicitud(idSolicitud);
            temaSolicitud = solicitud.getTema();

            // Verificar que el usuario no sea el solicitante
            if (actually.getUsuarioActivo().getId().equals(solicitud.getSolicitante().getId())) {
                mostrarMensaje(Alert.AlertType.ERROR, "No puedes resolver tus propias solicitudes");
                return;
            }
        } catch (Exception e) {
            mostrarMensaje(Alert.AlertType.ERROR, "Error al obtener la solicitud: " + e.getMessage());
            return;
        }

        grid.add(new Label("Título:"), 0, 0);
        grid.add(txtTituloContenido, 1, 0);
        grid.add(new Label("Tipo:"), 0, 1);
        grid.add(cbTipoContenido, 1, 1);
        grid.add(new Label("Autor:"), 0, 2);
        grid.add(txtAutorContenido, 1, 2);

        // Deshabilitar botón de resolver hasta que los campos estén completos
        Node resolverButton = dialog.getDialogPane().lookupButton(resolverButtonType);
        resolverButton.setDisable(true);

        // Validar campos en tiempo real
        ChangeListener<String> changeListener = (observable, oldValue, newValue) -> {
            boolean camposCompletos = !txtTituloContenido.getText().isEmpty() &&
                    cbTipoContenido.getValue() != null &&
                    !txtAutorContenido.getText().isEmpty();
            resolverButton.setDisable(!camposCompletos);
        };

        txtTituloContenido.textProperty().addListener(changeListener);
        txtAutorContenido.textProperty().addListener(changeListener);
        cbTipoContenido.valueProperty().addListener((observable, oldValue, newValue) -> {
            changeListener.changed(null, null, null);
        });

        dialog.getDialogPane().setContent(grid);

        // Convertir el resultado a un ContenidoAcademico cuando se presiona Resolver
        TEMA finalTemaSolicitud = temaSolicitud;
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == resolverButtonType) {
                ContenidoAcademico contenido = new ContenidoAcademico();
                contenido.setTitulo(txtTituloContenido.getText());
                contenido.setTipoContenido(cbTipoContenido.getValue());
                contenido.setAutor(txtAutorContenido.getText());
                contenido.setTema(finalTemaSolicitud); // Usar el tema de la solicitud
                contenido.setId(ArchivoUtilidades.generarId());
                return contenido;
            }
            return null;
        });

        Optional<ContenidoAcademico> result = dialog.showAndWait();

        result.ifPresent(contenido -> {
            try {
                // Seleccionar archivo para el contenido
                Stage stage = (Stage) panelAyuda.getScene().getWindow();
                File archivo = fileUploader.abrirSelectorDeArchivo(
                        stage,
                        contenido.getTipoContenido().toString().toLowerCase()
                );

                if (archivo != null) {
                    // Procesar el archivo según su tipo
                    switch (contenido.getTipoContenido()) {
                        case TEXTO:
                            contenido.setContenido(archivoUtil.leerArchivoComoTexto(archivo));
                            break;
                        case PDF:
                            contenido.setContenido("PDF:" + archivo.getAbsolutePath());
                            break;
                        case VIDEO:
                            contenido.setContenido("VIDEO:" + archivo.getAbsolutePath());
                            break;
                    }

                    // Resolver la solicitud con el contenido
                    actually.resolverSolicitud(idSolicitud, contenido);
                    mostrarMensaje(Alert.AlertType.INFORMATION, "Solicitud resuelta con éxito");
                    mostrarSolicitudesPendientes(null);
                }
            } catch (Exception e) {
                mostrarMensaje(Alert.AlertType.ERROR, "Error al resolver solicitud: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private String obtenerPrioridadTexto(int urgencia) {
        switch(urgencia) {
            case 1: return "Máxima";
            case 2: return "Alta";
            case 3: return "Media";
            case 4: return "Baja";
            case 5: return "Mínima";
            default: return "No definida";
        }
    }

    private void atenderSolicitud(String idSolicitud) {
        try {
            // Aquí podrías implementar lógica para conectar al estudiante que atiende
            // con el que hizo la solicitud (ej. abrir chat, etc.)

            actually.atenderSolicitud();
            mostrarMensaje(Alert.AlertType.INFORMATION, "Solicitud atendida con éxito");

            // Actualizar la vista
            mostrarSolicitudesPendientes(null);
        } catch (Exception e) {
            mostrarMensaje(Alert.AlertType.ERROR, "Error al atender solicitud: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void agregarAmigo(Estudiante estudiante) {
        try {
            Estudiante actual = (Estudiante) actually.getUsuarioActivo();

            // Verificar si ya son amigos
            if (actual.getAmigos().stream().anyMatch(a -> a.getId().equals(estudiante.getId()))) {
                mostrarMensaje(Alert.AlertType.WARNING, "Ya eres amigo de " + estudiante.getNombre());
                return;
            }

            // Agregar amistad
            actually.agregarAmistad(actual.getId(), estudiante.getId());

            cargarSugerenciasAmistades();

            mostrarMensaje(Alert.AlertType.INFORMATION,
                    "¡Ahora eres amigo de " + estudiante.getNombre() + "!");

        } catch (Exception e) {
            mostrarMensaje(Alert.AlertType.ERROR, "Error al agregar amigo: " + e.getMessage());
        }
    }

    @FXML
    private void mostrarSugerencias(MouseEvent event) {
        searchHBox.setVisible(false);
        searchHBox.setManaged(false);
        scrollContenidos.setVisible(false);
        scrollContenidos.setManaged(false);
        contenidoPanel.setVisible(false);
        contenidoPanel.setManaged(false);
        panelAyuda.setVisible(false);
        panelAyuda.setManaged(false);
        panelSolicitudes.setVisible(false);
        panelSolicitudes.setManaged(false);
        panelMiPerfil.setVisible(false);
        panelMiPerfil.setManaged(false);
        panelSugerenciasGrupos.setVisible(false);
        panelSugerenciasGrupos.setManaged(false);

        panelSugerencias.setVisible(true);
        panelSugerencias.setManaged(true);

        cargarSugerenciasAmistades();
    }

    private void cargarSugerenciasAmistades() {
        VBox contenedor = (VBox) scrollSugerencias.getContent();
        contenedor.getChildren().clear();

        if (!(actually.getUsuarioActivo() instanceof Estudiante estudiante)) {
            return;
        }

        List<Estudiante> sugerencias = actually.obtenerSugerenciasAmistades(estudiante.getId());
        System.out.println(sugerencias.size());

        if (sugerencias.isEmpty()) {
            Label vacio = new Label("No hay sugerencias disponibles. Sube más contenidos para mejorar las recomendaciones.");
            vacio.setStyle("-fx-font-size: 26px; -fx-text-fill: gray; -fx-padding: 20;");
            contenedor.getChildren().add(vacio);
            return;
        }

        // Separar sugerencias en dos categorías
        List<Estudiante> amigosDeAmigos = new ArrayList<>();
        List<Estudiante> porIntereses = new ArrayList<>();

        for (Estudiante sugerencia : sugerencias) {
            if (tieneAmigosEnComun(estudiante, sugerencia)) {
                amigosDeAmigos.add(sugerencia);
            } else {
                porIntereses.add(sugerencia);
            }
        }

        // Mostrar amigos de amigos primero
        if (!amigosDeAmigos.isEmpty()) {
            Label tituloAmigos = new Label("Amigos de amigos con intereses similares:");
            tituloAmigos.setStyle("-fx-font-family: 'SansSerif'; -fx-font-size: 22; -fx-text-fill: #05242F; -fx-padding: 10 0 5 0;");
            contenedor.getChildren().add(tituloAmigos);

            for (Estudiante sugerencia : amigosDeAmigos) {
                agregarTarjetaSugerencia(contenedor, sugerencia, true);
            }
        }

        // Mostrar sugerencias por intereses
        if (!porIntereses.isEmpty()) {
            Label tituloIntereses = new Label("Personas con intereses similares:");
            tituloIntereses.setStyle("-fx-font-family: 'SansSerif'; -fx-font-size: 22; -fx-text-fill: #05242F; -fx-padding: 20 0 5 0;");
            contenedor.getChildren().add(tituloIntereses);

            for (Estudiante sugerencia : porIntereses) {
                agregarTarjetaSugerencia(contenedor, sugerencia, false);
            }
        }
    }

    private boolean tieneAmigosEnComun(Estudiante estudiante1, Estudiante estudiante2) {
        Set<String> amigos1 = estudiante1.getAmigos().stream()
                .map(Estudiante::getId)
                .collect(Collectors.toSet());

        return estudiante2.getAmigos().stream()
                .anyMatch(amigo -> amigos1.contains(amigo.getId()));
    }

    private void agregarTarjetaSugerencia(VBox contenedor, Estudiante estudiante, boolean esAmigoDeAmigo) {
        Estudiante actual = (Estudiante) actually.getUsuarioActivo();
        boolean yaSonAmigos = actual.getAmigos().stream().anyMatch(a -> a.getId().equals(estudiante.getId()));

        HBox card = new HBox(15);
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
                "-fx-background-color: #F5F5F5;" +
                        "-fx-background-radius: 40;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 6, 0, 0, 2);"
        );

        // Avatar
        Circle avatarPlaceholder = new Circle(30, Color.LIGHTGRAY);
        Text inicial = new Text(estudiante.getNombre().substring(0, 1));
        inicial.setStyle("-fx-font-size: 36; -fx-fill: #000000;");
        StackPane avatar = new StackPane(avatarPlaceholder, inicial);

        // Información del estudiante
        VBox info = new VBox(6);
        info.setAlignment(Pos.CENTER_LEFT);

        Label nombre = new Label(estudiante.getNombre());
        nombre.setStyle("-fx-font-family: 'SansSerif'; -fx-text-fill: #000000; -fx-font-size: 20px;");

        Label tipo = new Label(esAmigoDeAmigo ? "Amigo de amigos" : "Intereses similares");
        tipo.setStyle("-fx-text-fill: " + (esAmigoDeAmigo ? "#1F4C59" : "#B0EB00") + "; -fx-font-family: 'SansSerif'; -fx-font-size: 16px;");

        Set<TEMA> interesesComunes = obtenerInteresesComunes(estudiante);
        Label intereses = new Label("Temas comunes: " + formatIntereses(interesesComunes));
        intereses.setStyle("-fx-text-fill: #777; -fx-font-size: 16px; -fx-font-family: 'SansSerif';");

        info.getChildren().addAll(nombre, tipo, intereses);

        // Contenedor derecho
        VBox contenedorDerecho = new VBox();
        contenedorDerecho.setAlignment(Pos.BOTTOM_RIGHT);
        contenedorDerecho.setPadding(new Insets(0, 0, 0, 20));

        if (yaSonAmigos) {
            // Mostrar mensaje en lugar del botón
            Label yaAmigo = new Label("Ya eres amigo de " + estudiante.getNombre());
            yaAmigo.setStyle("-fx-text-fill: #000000; -fx-font-size: 18px; -fx-font-family: 'SansSerif';");
            contenedorDerecho.getChildren().add(yaAmigo);
        } else {
            // Botón Agregar
            Button btnAgregar = new Button("Agregar");
            btnAgregar.setStyle(
                    "-fx-background-color: #2D94B0;" +
                            "-fx-text-fill: black;" +
                            "-fx-font-size: 18px;" +
                            "-fx-font-family: 'SansSerif';" +
                            "-fx-background-radius: 40;" +
                            "-fx-padding: 5 15;" +
                            "-fx-cursor: hand;"
            );

            btnAgregar.setOnMouseEntered(e -> btnAgregar.setStyle(
                    "-fx-background-color: #62AEC3;" +
                            "-fx-text-fill: black;" +
                            "-fx-font-size: 18px;" +
                            "-fx-font-family: 'SansSerif';" +
                            "-fx-background-radius: 40;" +
                            "-fx-padding: 5 15;" +
                            "-fx-cursor: hand;"
            ));

            btnAgregar.setOnMouseExited(e -> btnAgregar.setStyle(
                    "-fx-background-color: #2D94B0;" +
                            "-fx-text-fill: black;" +
                            "-fx-font-size: 18px;" +
                            "-fx-font-family: 'SansSerif';" +
                            "-fx-background-radius: 40;" +
                            "-fx-padding: 5 15;" +
                            "-fx-cursor: hand;"
            ));

            btnAgregar.setOnAction(e -> {
                agregarAmigo(estudiante);
                contenedor.getChildren().remove(card); // Eliminar tarjeta si se desea tras agregar
            });

            contenedorDerecho.getChildren().add(btnAgregar);
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(avatar, info, spacer, contenedorDerecho);
        contenedor.getChildren().add(card);
    }

    private Set<TEMA> obtenerInteresesComunes(Estudiante otroEstudiante) {
        Set<TEMA> interesesComunes = new HashSet<>();
        if (actually.getUsuarioActivo() instanceof Estudiante estudianteActual) {
            Set<TEMA> misIntereses = estudianteActual.getContenidosSubidos().stream()
                    .map(ContenidoAcademico::getTema)
                    .collect(Collectors.toSet());

            interesesComunes = otroEstudiante.getContenidosSubidos().stream()
                    .map(ContenidoAcademico::getTema)
                    .filter(misIntereses::contains)
                    .collect(Collectors.toSet());
        }
        return interesesComunes;
    }

    private String formatIntereses(Set<TEMA> intereses) {
        return intereses.isEmpty() ? "Ninguno aún" :
                intereses.stream()
                        .map(TEMA::name)
                        .collect(Collectors.joining(", "));
    }

    @FXML
    public void mostrarMiPerfil(MouseEvent event) {
        searchHBox.setVisible(false);
        searchHBox.setManaged(false);
        scrollContenidos.setVisible(false);
        scrollContenidos.setManaged(false);
        contenidoPanel.setVisible(false);
        contenidoPanel.setManaged(false);
        panelAyuda.setVisible(false);
        panelAyuda.setManaged(false);
        panelSugerencias.setVisible(false);
        panelSugerencias.setManaged(false);
        panelSugerenciasGrupos.setVisible(false);
        panelSugerenciasGrupos.setManaged(false);
        panelChats.setVisible(false);
        panelChats.setManaged(false);

        panelMiPerfil.setVisible(true);
        panelMiPerfil.setManaged(true);

        // Actualizar información básica
        nombreLabel.setText("❥ Nombre: " + actually.getUsuarioActivo().getNombre());
        idLabel.setText("❥ ID: " + actually.getUsuarioActivo().getId());
        correoLabel.setText("❥ Correo: " + actually.getUsuarioActivo().getCorreo());

        // Actualizar información de participación si es estudiante
        if (actually.getUsuarioActivo() instanceof Estudiante) {
            Estudiante estudiante = (Estudiante) actually.getUsuarioActivo();

            nivelLabel.setText("❥ Nivel: " + estudiante.getNivel().getNombre());
            puntosLabel.setText("❥ Puntos: " + estudiante.getPuntosParticipacion());

            double progreso = calcularProgresoNivel(estudiante);
            progresoBar.setProgress(progreso);
            progresoLabel.setText(String.format("%.0f%% completado hacia el siguiente nivel", progreso * 100));
        }
    }

    private double calcularProgresoNivel(Estudiante estudiante) {
        NivelParticipacion nivelActual = estudiante.getNivel();
        int puntosActuales = estudiante.getPuntosParticipacion();

        if (nivelActual == NivelParticipacion.MAESTRO) {
            return 1.0;
        }

        int rangoNivel = nivelActual.getMaxPuntos() - nivelActual.getMinPuntos();
        int puntosEnNivel = puntosActuales - nivelActual.getMinPuntos();

        return (double) puntosEnNivel / rangoNivel;
    }

    @FXML
    public void mostrarSugerenciasGrupos(MouseEvent event) throws IOException {
        searchHBox.setVisible(false);
        searchHBox.setManaged(false);
        contenidoPanel.setVisible(false);
        scrollContenidos.setVisible(false);
        panelAyuda.setVisible(false);
        panelSolicitudes.setVisible(false);
        panelMiPerfil.setVisible(false);
        panelSugerencias.setVisible(false);
        panelChats.setVisible(false);
        panelChats.setManaged(false);

        // Mostrar panel de sugerencias de grupos
        panelSugerenciasGrupos.setVisible(true);
        panelSugerenciasGrupos.setManaged(true);

        cargarSugerenciasGrupos();
    }

    public void cargarSugerenciasGrupos() throws IOException {
        contenedorSugerenciasGrupos.getChildren().clear();

        if (!(actually.getUsuarioActivo() instanceof Estudiante estudiante)) {
            return;
        }

        // DEBUG: Verificar datos antes de generar sugerencias
        System.out.println("[DEBUG] Grupos existentes ANTES de generar sugerencias: " +
                actually.getGruposEstudio().size());

        actually.imprimirGrupos();
        actually.imprimirMiembrosGrupos();

        // 2. Generar sugerencias
        Map<TEMA, List<GrupoEstudio>> sugerenciasGrupos = actually.getGestorGruposEstudio()
                .generarSugerenciasGrupos();

        // DEBUG: Verificar sugerencias generadas
        System.out.println("[DEBUG] Total sugerencias generadas: " + sugerenciasGrupos.size());

        // 3. Obtener temas de interés del estudiante
        Set<TEMA> temasInteres = estudiante.getContenidosSubidos().stream()
                .map(ContenidoAcademico::getTema)
                .collect(Collectors.toSet());

        // 4. Mostrar sugerencias filtradas
        sugerenciasGrupos.entrySet().stream()
                .filter(entry -> temasInteres.contains(entry.getKey()))
                .forEach(entry -> {
                    entry.getValue().forEach(grupo -> {
                        // DEBUG por grupo
                        System.out.println("[DEBUG] Evaluando grupo: " + grupo.getNombre() +
                                " | ¿Pertenece? " + estudiante.perteneceAGrupo(grupo));

                        if (!estudiante.perteneceAGrupo(grupo) && !sugerenciasMostradas.contains(grupo)) {
                            sugerenciasMostradas.add(grupo); // Marcar como mostrado
                            agregarTarjetaGrupoSugerido(grupo);
                        } else if (!estudiante.perteneceAGrupo(grupo)) {
                            agregarTarjetaGrupoSugerido(grupo);
                        }
                    });
                });
    }

    private void agregarTarjetaGrupoSugerido(GrupoEstudio grupo) {
        VBox card = new VBox(10);
        card.getStyleClass().add("grupo-sugerido");

        // Información del grupo
        Label nombre = new Label(grupo.getNombre());
        nombre.getStyleClass().add("titulo-grupo");

        Label tema = new Label("Tema principal: " + grupo.getTema().getName());
        tema.getStyleClass().add("info-grupo");

        // Actualiza el texto de miembros (usa grupo.getParticipantes().size())
        Label miembrosLabel = new Label("Miembros: " + grupo.getParticipantes().size());
        miembrosLabel.setStyle("-fx-text-fill: #4a6baf;");

        // Muestra nombres de los primeros 3 miembros
        StringBuilder miembrosStr = new StringBuilder("Miembros: ");
        grupo.getParticipantes().stream()
                .limit(3)
                .forEach(e -> miembrosStr.append(e.getNombre()).append(", "));

        System.out.println("El grupo actual es: " + grupo.getNombre());
        List<Estudiante> miembros = grupo.getParticipantes();
        String miembro = "";
        for(Estudiante e : miembros){
            miembro = e.toString() + ", ";
        }
        System.out.println("Los miembros del grupo son: " + miembros);

        if (grupo.getParticipantes().size() > 3) {
            miembrosStr.append("... (+").append(grupo.getParticipantes().size() - 3).append(")");
        }

        Label listaMiembros = new Label(miembrosStr.toString());
        listaMiembros.setStyle("-fx-font-family: 'SansSerif'; -fx-font-size: 14px; -fx-text-fill: #4a6baf;");
        listaMiembros.setWrapText(true);

        // Botones de acción
        HBox botones = new HBox(15);
        botones.setAlignment(Pos.CENTER);

        Button btnUnirse = new Button("Unirse");
        btnUnirse.getStyleClass().add("boton-accion-grupo");
        btnUnirse.getStyleClass().add("boton-unirse");
        btnUnirse.setOnAction(e -> {
            if (actually.getUsuarioActivo() instanceof Estudiante estudiante) {
                GestorGruposEstudio gestor = GestorGruposEstudio.getInstance();
                try {
                    gestor.aceptarSugerenciaGrupo(estudiante, grupo);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                // Deshabilitar botón después de unirse
                btnUnirse.setDisable(true);
                btnUnirse.setText("¡Unido!");

                mostrarMensaje(Alert.AlertType.INFORMATION,
                        "Te has unido al grupo de " + grupo.getTema());
            }
        });

        botones.getChildren().addAll(btnUnirse);

        card.getChildren().addAll(nombre, tema, miembrosLabel, listaMiembros, botones);
        contenedorSugerenciasGrupos.getChildren().add(card);
    }

    @FXML
    public void mostrarPanelChats(MouseEvent mouseEvent) {
        searchHBox.setVisible(false);
        searchHBox.setManaged(false);
        scrollContenidos.setVisible(false);
        contenidoPanel.setVisible(false);
        panelAyuda.setVisible(false);
        panelSolicitudes.setVisible(false);
        panelSugerencias.setVisible(false);
        panelMiPerfil.setVisible(false);
        panelSugerenciasGrupos.setVisible(false);
        panelChats.setVisible(true);
        panelChats.setManaged(true);

        cargarPanelChats();
    }

    private void cargarPanelChats(){
        this.usuarioActivo = (Estudiante) actually.getUsuarioActivo();

        if (usuarioActivo != null) {
            cargarAmigos();
        }
    }

    private void cargarAmigos() {
        if (usuarioActivo != null && usuarioActivo.getAmigos() != null) {
            // Carga la lista de amigos en la ListView
            ListAmigosChats.getItems().setAll(usuarioActivo.getAmigos());

            // Define cómo se debe mostrar cada amigo en la lista: solo su nombre
            ListAmigosChats.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Estudiante item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getNombre());
                    }
                }
            });
        }
    }

    @FXML
    public void abrirChat(MouseEvent mouseEvent) {
        Estudiante amigoSeleccionado = ListAmigosChats.getSelectionModel().getSelectedItem();
        if (amigoSeleccionado != null) {
            // Obtiene o crea el chat entre el usuario activo y el amigo seleccionado
            Chat chat = GestorChats.getInstance().obtenerChat(usuarioActivo, amigoSeleccionado);
            // Carga la interfaz del chat en el panel principal
            cargarVistaChat(chat);
        }
    }

    private void cargarVistaChat(Chat chat) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/students/chat_view.fxml"));
            Parent vistaChat = loader.load();

            // Obtiene el controlador de la vista cargada y lo inicializa con el chat y usuario
            VistaChatController controlador = loader.getController();
            controlador.inicializar(chat, usuarioActivo);

            // Limpia y añade la nueva vista del chat al panel principal
            chatPanel.getChildren().setAll(vistaChat);

            // Ajusta los anclajes para que la vista ocupe todo el espacio del AnchorPane
            AnchorPane.setTopAnchor(vistaChat, 0.0);
            AnchorPane.setBottomAnchor(vistaChat, 0.0);
            AnchorPane.setLeftAnchor(vistaChat, 0.0);
            AnchorPane.setRightAnchor(vistaChat, 0.0);

        } catch (IOException e) {
            e.printStackTrace(); // Imprime error si la carga de la vista falla
        }
    }
}