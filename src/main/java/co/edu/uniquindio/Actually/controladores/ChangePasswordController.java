package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.Actually;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ChangePasswordController {
    @FXML public TextField idField;
    @FXML public PasswordField newPasswordField;

    private final Actually actually = Actually.getInstance();

    @FXML
    public void onChangeButtonClick(ActionEvent event) throws Exception {
        String id = idField.getText();
        String newPassword = newPasswordField.getText();
        actually.changePassword(id, newPassword);
        actually.mostrarMensaje(Alert.AlertType.INFORMATION, "Cambio de contraseña exitoso.");
    }

    @FXML
    public void volverAlLogin(ActionEvent event) {
        Actually.getInstance().loadStage("/ventanas/common/login.fxml", event);
    }
}
