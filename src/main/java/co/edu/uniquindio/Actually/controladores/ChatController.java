package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.Actually;
import co.edu.uniquindio.Actually.modelo.Chat;
import co.edu.uniquindio.Actually.modelo.Estudiante;
import co.edu.uniquindio.Actually.modelo.GestorChats;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ChatController {

    @FXML
    private ListView<Estudiante> ListAmigosChats;
    // Lista que muestra los amigos del usuario para seleccionar y abrir un chat

    @FXML
    private AnchorPane chatPane;
    // Panel donde se cargará la vista del chat seleccionado dinámicamente

    private Estudiante usuarioActivo;
    // El usuario actualmente logueado (remitente de los mensajes)

    private Actually actually = Actually.getInstance();
    // Instancia singleton principal para acceder a datos y funcionalidades globales

    /**
     * Método llamado automáticamente al cargar el controlador (por JavaFX).
     * Inicializa la variable usuarioActivo y carga la lista de amigos.
     */
    @FXML
    private void initialize() {
        this.usuarioActivo = (Estudiante) actually.getUsuarioActivo();

        if (usuarioActivo != null) {
            cargarAmigos();
        }
    }

    /**
     * Carga los amigos del usuario activo en la lista ListAmigosChats.
     * También configura la apariencia de cada celda para mostrar solo el nombre del amigo.
     */
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

    /**
     * Método asociado a la acción de seleccionar un amigo y abrir el chat.
     * Obtiene el amigo seleccionado y abre la ventana de chat correspondiente.
     */
    @FXML
    public void abrirChat() {
        Estudiante amigoSeleccionado = ListAmigosChats.getSelectionModel().getSelectedItem();
        if (amigoSeleccionado != null) {
            // Obtiene o crea el chat entre el usuario activo y el amigo seleccionado
            Chat chat = GestorChats.getInstance().obtenerChat(usuarioActivo, amigoSeleccionado);
            // Carga la interfaz del chat en el panel principal
            cargarVistaChat(chat);
        }
    }

    /**
     * Carga la vista del chat seleccionado dentro del AnchorPane chatPane.
     * Configura el controlador del chat con el chat y usuario activo para mostrar mensajes.
     *
     * @param chat Chat que se va a mostrar en la interfaz
     */
    private void cargarVistaChat(Chat chat) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/students/chat_view.fxml"));
            Parent vistaChat = loader.load();

            // Obtiene el controlador de la vista cargada y lo inicializa con el chat y usuario
            VistaChatController controlador = loader.getController();
            controlador.inicializar(chat, usuarioActivo);

            // Limpia y añade la nueva vista del chat al panel principal
            chatPane.getChildren().setAll(vistaChat);

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
