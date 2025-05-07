package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.Actually;
import co.edu.uniquindio.Actually.excepciones.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegistroControlador {

    private final Actually actually = Actually.getInstance();

    @FXML private TextField nombreField;
    @FXML private TextField idField;
    @FXML private TextField correoField;
    @FXML private PasswordField contrasenaField;
    @FXML private Label mensajeLabel;

    @FXML
    public void registrarse() {
        try {
            actually.registrarEstudiante(
                    nombreField.getText(),
                    idField.getText(),
                    correoField.getText(),
                    contrasenaField.getText()
            );
            mensajeLabel.setText("Registro exitoso");

        } catch (CampoVacioException | CampoObligatorioException | CampoRepetidoException e) {
            mensajeLabel.setText(e.getMessage());
            actually.mostrarMensaje(Alert.AlertType.ERROR, e.getMessage());  // Mostrar alerta de error
        } catch (Exception e) {
            mensajeLabel.setText("Error inesperado: " + e.getMessage());
            actually.mostrarMensaje(Alert.AlertType.ERROR, "Error inesperado: " + e.getMessage());  // Mostrar alerta de error inesperado
        } finally {
            limpiarCampos();
        }
    }

    @FXML
    public void volverAlLogin(ActionEvent event) {
        Actually.getInstance().loadStage("/ventanas/common/login.fxml", event);
    }

    private void limpiarCampos() {
        nombreField.clear();
        idField.clear();
        correoField.clear();
        contrasenaField.clear();
    }
}
