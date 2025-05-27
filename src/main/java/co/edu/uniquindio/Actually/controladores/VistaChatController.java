package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.Actually;
import co.edu.uniquindio.Actually.modelo.Chat;
import co.edu.uniquindio.Actually.modelo.Estudiante;
import co.edu.uniquindio.Actually.modelo.Mensaje;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class VistaChatController {

    @FXML
    private VBox contenedorMensajes;

    @FXML
    private TextField campoMensaje;

    @FXML
    private ScrollPane scrollPane;

    private Estudiante usuarioActivo;
    private Chat chat;

    public void inicializar(Chat chat, Estudiante usuarioActivo) {
        this.chat = chat;
        this.usuarioActivo = usuarioActivo;
        mostrarMensajes();
    }

    private void mostrarMensajes() {
        contenedorMensajes.getChildren().clear();

        for (Mensaje mensaje : chat.getMensajes()) {
            agregarMensajeAVista(mensaje);
        }

        scrollPane.layout();
        scrollPane.setVvalue(1.0);
    }

    private void agregarMensajeAVista(Mensaje mensaje) {
        boolean esRemitente = mensaje.getRemitente().equals(usuarioActivo);

        Label labelMensaje = new Label(mensaje.getContenido());
        labelMensaje.setWrapText(true);
        labelMensaje.setMaxWidth(350); // Para que no se alargue demasiado en horizontal

        // Estilos personalizados para cada tipo de mensaje
        String estiloFondo = esRemitente ? "#88A8BC" : "#05242F";
        String estiloTexto = esRemitente ? "#000000" : "#f5f5f5";

        labelMensaje.setStyle(
                "-fx-background-color: " + estiloFondo + ";" +
                        "-fx-text-fill: " + estiloTexto + ";" +
                        "-fx-padding: 10px 14px;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-family: 'SansSerif';"
        );

        HBox hbox = new HBox(labelMensaje);
        hbox.setPadding(new Insets(5));
        hbox.setAlignment(esRemitente ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        contenedorMensajes.getChildren().add(hbox);
    }

    @FXML
    private void enviarMensaje() throws IOException {
        String contenido = campoMensaje.getText().trim();

        if (!contenido.isEmpty()) {
            chat.enviarMensaje(usuarioActivo, contenido);

            Mensaje ultimoMensaje = chat.getMensajes().get(chat.getMensajes().size() - 1);
            agregarMensajeAVista(ultimoMensaje);

            campoMensaje.clear();

            scrollPane.layout();
            scrollPane.setVvalue(1.0);

            Actually.getInstance().guardarChats();
        }
    }
}
