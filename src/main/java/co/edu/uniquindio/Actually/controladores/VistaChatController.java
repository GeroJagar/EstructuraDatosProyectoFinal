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

    /*
    Controller que permite visualizar y enviar mensajes de un chat seleccionado.
    */

    @FXML
    private VBox contenedorMensajes; // Contenedor vertical para mostrar los mensajes en la interfaz

    @FXML
    private TextField campoMensaje; // Campo de texto donde el usuario escribe el mensaje

    @FXML
    private ScrollPane scrollPane; // ScrollPane para permitir el scroll en la vista de mensajes

    private Estudiante usuarioActivo; // Estudiante que está usando la interfaz (remitente)
    private Chat chat; // Chat actual entre dos estudiantes

    /**
     * Inicializa el controlador con el chat seleccionado y el usuario activo.
     *
     * @param chat Chat que se va a visualizar y administrar
     * @param usuarioActivo Estudiante que está enviando o recibiendo mensajes
     */
    public void inicializar(Chat chat, Estudiante usuarioActivo) {
        this.chat = chat;
        this.usuarioActivo = usuarioActivo;

        // Muestra los mensajes existentes en la interfaz
        mostrarMensajes();
    }

    /**
     * Limpia el contenedor de mensajes y agrega todos los mensajes del chat actual.
     * También ajusta el scroll para que muestre el último mensaje.
     */
    private void mostrarMensajes() {
        contenedorMensajes.getChildren().clear(); // Limpia mensajes previos

        // Itera sobre todos los mensajes y los agrega a la vista
        for (Mensaje mensaje : chat.getMensajes()) {
            agregarMensajeAVista(mensaje);
        }

        scrollPane.layout(); // Fuerza la actualización del layout de la UI
        scrollPane.setVvalue(1.0); // Auto scroll hacia abajo para mostrar el mensaje más reciente
    }

    /**
     * Crea un nodo visual (HBox con Label) para un mensaje y lo agrega al contenedor.
     * Los mensajes del remitente activo se alinean a la derecha y con color azul,
     * mientras que los mensajes del destinatario se alinean a la izquierda y con color gris.
     *
     * @param mensaje Mensaje que se desea mostrar en la interfaz
     */
    private void agregarMensajeAVista(Mensaje mensaje) {
        // Determina si el mensaje fue enviado por el usuario activo
        boolean esRemitente = mensaje.getRemitente().equals(usuarioActivo);

        // Crea la etiqueta con el texto del mensaje
        Label labelMensaje = new Label(mensaje.getContenido());
        labelMensaje.setWrapText(true); // Permite que el texto se ajuste en varias líneas

        // Aplica estilos según si es remitente o destinatario
        labelMensaje.setStyle("-fx-background-color: " + (esRemitente ? "#cce5ff" : "#e2e2e2") +
                "; -fx-padding: 8px 12px; -fx-background-radius: 10;");

        // Contenedor horizontal para alinear el mensaje a derecha o izquierda
        HBox hbox = new HBox(labelMensaje);
        hbox.setPadding(new Insets(5)); // Espaciado interno
        hbox.setAlignment(esRemitente ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT); // Alineación del mensaje

        // Agrega el mensaje al contenedor principal de mensajes
        contenedorMensajes.getChildren().add(hbox);
    }

    /**
     * Método asociado al evento de enviar mensaje (por ejemplo, al presionar Enter o un botón).
     * Envía el mensaje al chat, actualiza la vista y guarda los chats en disco.
     *
     * @throws IOException Si hay un error al guardar los chats
     */
    @FXML
    private void enviarMensaje() throws IOException {
        String contenido = campoMensaje.getText().trim(); // Obtiene el texto escrito y elimina espacios al inicio/final

        if (!contenido.isEmpty()) { // Solo envía si el texto no está vacío
            chat.enviarMensaje(usuarioActivo, contenido); // Envía el mensaje al chat

            // Obtiene el último mensaje enviado para agregarlo a la vista
            Mensaje ultimoMensaje = chat.getMensajes().get(chat.getMensajes().size() - 1);
            agregarMensajeAVista(ultimoMensaje);

            campoMensaje.clear(); // Limpia el campo de texto

            scrollPane.layout(); // Actualiza la UI para el scroll
            scrollPane.setVvalue(1.0); // Auto scroll para mostrar el mensaje recién enviado

            Actually.getInstance().guardarChats(); // Guarda los chats actualizados en disco
        }
    }
}
