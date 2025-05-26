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

    @FXML
    public void ingresar(ActionEvent event) {
        String id = idField.getText();
        String contrasena = contrasenaField.getText();
        if(id.equals("999999") && contrasena.equals("999999")){
            actually.loadStage("/ventanas/moderador/moderadorMain.fxml", event);
            return;
        }
        Usuario usuario = actually.obtenerUsuarioPorId(id);
        if (usuario != null && usuario.getContrasena().equals(contrasena)) {
            actually.setUsuarioActivo(usuario);
            actually.loadStage("/ventanas/students/chats.fxml", event);
        } else {
            actually.mostrarMensaje(Alert.AlertType.ERROR, "ID o contraseña incorrectos");
        }
    }

    @FXML
    public void forgotPassword(ActionEvent event){
        actually.loadStage("/ventanas/common/changePassword.fxml", event);
    }

    @FXML
    public void irARegistro(ActionEvent event) {
        actually.loadStage("/ventanas/students/registro.fxml", event);
    }

    public void onBackButtonClick(ActionEvent event) {
        actually.loadStage("/ventanas/common/homepage.fxml", event);
    }
}
