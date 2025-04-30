package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.Actually;
import co.edu.uniquindio.Actually.modelo.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginControlador {

    private final Actually actually = Actually.getInstance();

    @FXML private TextField idField;
    @FXML private PasswordField contrasenaField;
    @FXML private Label mensajeLabel;

    @FXML
    public void ingresar(ActionEvent event) {
        String id = idField.getText();
        String contrasena = contrasenaField.getText();

        Usuario usuario = actually.obtenerUsuarioPorId(id);

        if (usuario != null && usuario.getContrasena().equals(contrasena)) {
            mensajeLabel.setText("Ingreso exitoso");
            actually.loadStage("/ventanas/principal.fxml", event);
        } else {
            mensajeLabel.setText("ID o contraseña incorrectos");
            actually.mostrarMensaje(Alert.AlertType.ERROR, "ID o contraseña incorrectos");
        }
    }

    @FXML
    public void irARegistro(ActionEvent event) {
        actually.loadStage("/ventanas/registro.fxml", event);
    }
}
