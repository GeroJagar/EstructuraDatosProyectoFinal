package co.edu.uniquindio.Actually;

import co.edu.uniquindio.Actually.modelo.Estudiante;
import co.edu.uniquindio.Actually.modelo.Usuario;
import co.edu.uniquindio.Actually.excepciones.*;
import co.edu.uniquindio.Actually.utilidades.ArchivoUtilidades;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Actually {

    private static Actually actually;
    private Map<String, Usuario> usuarios = new HashMap<>();
    private final String RUTA_USUARIOS = "src/main/resources/serializacion/usuarios.data"; // Ruta donde se almacenan los usuarios serializados

    public static Actually getInstance() {
        if (actually == null) {
            actually = new Actually();
        }
        return actually;
    }

    public void inicializar() {
        try {
            // Intentamos cargar los usuarios serializados al inicio
            this.usuarios = (Map<String, Usuario>) ArchivoUtilidades.deserializarObjeto(RUTA_USUARIOS);
            for (Usuario usuario : this.usuarios.values()) {
                System.out.println(usuario);
            }
        } catch (IOException | ClassNotFoundException e) {
            // Si no se encuentran los usuarios, manejamos la excepción (puedes agregar un log si lo deseas)
            System.out.println("No se encontraron usuarios serializados, iniciando con una lista vacía.");
        }
    }

    public void registrarEstudiante(String nombre, String id, String correo, String contrasena)
            throws CampoObligatorioException, CampoVacioException, CampoRepetidoException {

        if (nombre == null || nombre.isBlank()) {
            throw new CampoObligatorioException("El nombre es obligatorio");
        }
        if (id == null || id.isBlank()) {
            throw new CampoVacioException("El ID es obligatorio");
        }
        if (correo == null || correo.isBlank()) {
            throw new CampoVacioException("El correo es obligatorio");
        }
        if (contrasena == null || contrasena.isBlank()) {
            throw new CampoVacioException("La contraseña es obligatoria");
        }

        // Verificamos si ya existe un usuario con este correo
        for (Usuario usuario : usuarios.values()) {
            if (usuario.getCorreo().equals(correo)) {
                throw new CampoRepetidoException("Ya existe un usuario con este correo");
            }
        }

        // Verificamos si ya existe un usuario con este ID
        if (usuarios.containsKey(id)) {
            throw new CampoRepetidoException("Ya existe un usuario con este ID");
        }

        // Creamos el estudiante y lo agregamos al mapa usando el ID como clave
        Estudiante estudiante = Estudiante.builder()
                .nombre(nombre)
                .id(id)
                .correo(correo)
                .contrasena(contrasena)
                .build();

        usuarios.put(id, estudiante); // Usamos el ID como clave en el mapa

        // Guardamos el archivo con la nueva lista de usuarios
        try {
            ArchivoUtilidades.serializarObjeto(RUTA_USUARIOS, usuarios);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensaje(Alert.AlertType.ERROR, "Error al guardar los usuarios: " + e.getMessage());
        }

        mostrarMensaje(Alert.AlertType.INFORMATION, "Registro exitoso");
    }

    public Usuario obtenerUsuarioPorId(String id) {
        for (Usuario usuario : usuarios.values()) {
            if (usuario.getId().equals(id)) {
                return usuario;
            }
        }
        return null;
    }

    public void loadStage(String url, Event event) {
        try {
            if (event != null) {
                ((Node) (event.getSource())).getScene().getWindow().hide(); // Cerrar la ventana actual
            }
            Parent root = FXMLLoader.load(Objects.requireNonNull(Actually.class.getResource(url)));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Actually");
            newStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            mostrarMensaje(Alert.AlertType.ERROR, "Error al cargar la ventana: " + ex.getMessage());
        }
    }

    public void mostrarMensaje(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setContentText(mensaje);
        alert.show();
    }
}
