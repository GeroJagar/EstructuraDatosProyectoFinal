package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.Actually;
import co.edu.uniquindio.Actually.modelo.ContenidoAcademico;
import co.edu.uniquindio.Actually.modelo.TIPOCONTENIDO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

public class HomepageController {

    @FXML public VBox contenedorContenido;
    @FXML public TextField searchField;
    @FXML public ComboBox<String> cbCriterioBusqueda;

    private final Actually actually = Actually.getInstance();

    @FXML
    public void initialize(){
        cbCriterioBusqueda.getItems().addAll("titulo", "autor", "tema");
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

        footer.getChildren().addAll(tituloLabel);

        // Botones de interacción (Like, Comentar, Compartir)
        HBox interactionBar = new HBox(15);
        interactionBar.setAlignment(Pos.CENTER_LEFT);

        Button likeBtn = createInteractionButton("❤ Me gusta", "Me gusta");
        likeBtn.setCursor(Cursor.HAND);
        Button commentBtn = createInteractionButton("💬 Comentar", "Comentar");
        commentBtn.setCursor(Cursor.HAND);
        Button shareBtn = createInteractionButton("↪ Compartir", "Compartir");
        shareBtn.setCursor(Cursor.HAND);

        interactionBar.getChildren().addAll(likeBtn, commentBtn, shareBtn);

        // Ensamblar la tarjeta
        card.getChildren().addAll(header, contentPane, footer, interactionBar);
        contenedorContenido.getChildren().add(card);
    }

    private Button createInteractionButton(String emoji, String tooltipText) {
        Button btn = new Button(emoji);
        btn.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-font-family: \"SansSerif\";");
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

    private void mostrarMensaje(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setContentText(mensaje);
        alert.show();
    }

    @FXML
    public void buscarContenido(ActionEvent event) throws Exception {
        String criterio = cbCriterioBusqueda.getValue();
        String clave = searchField.getText();
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

    @FXML
    public void onLoginButtonClick(ActionEvent event){
        actually.loadStage("/ventanas/common/login.fxml", event);
    }

}
