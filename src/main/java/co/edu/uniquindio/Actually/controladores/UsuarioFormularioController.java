package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.modelo.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class UsuarioFormularioController {

    @FXML private ComboBox<String> cbTipoUsuario;
    @FXML private TextField txtNombre;
    @FXML private TextField txtId;
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtContrasena;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private Usuario usuario;
    private boolean modoCreacion;

    @FXML
    private void initialize() {
        cbTipoUsuario.getItems().addAll("Estudiante", "Moderador");
        cbTipoUsuario.getSelectionModel().selectFirst();
    }

    public void setModoCreacion(boolean modoCreacion) {
        this.modoCreacion = modoCreacion;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        cargarDatosUsuario();
    }

    private void cargarDatosUsuario() {
        if (usuario != null) {
            txtNombre.setText(usuario.getNombre());
            txtId.setText(usuario.getId());
            txtCorreo.setText(usuario.getCorreo());
            txtContrasena.setText(usuario.getContrasena());

            if (usuario instanceof Estudiante) {
                cbTipoUsuario.getSelectionModel().select("Estudiante");
            } else {
                cbTipoUsuario.getSelectionModel().select("Moderador");
            }

            txtId.setEditable(false); // No se puede editar el ID
        }
    }

    @FXML
    private void guardar() {
        if (validarCampos()) {
            if (modoCreacion || usuario == null) {
                usuario = crearUsuario();
            } else {
                actualizarUsuario();
            }

            cerrarVentana();
        }
    }

    private Usuario crearUsuario() {
        String tipo = cbTipoUsuario.getValue();

        if ("Estudiante".equals(tipo)) {
            Estudiante est = new Estudiante();
            configurarUsuario(est);
            return est;
        } else {
            Moderador mod = new Moderador();
            configurarUsuario(mod);
            return mod;
        }
    }

    private void actualizarUsuario() {
        configurarUsuario(usuario);
    }

    private void configurarUsuario(Usuario usuario) {
        usuario.setNombre(txtNombre.getText());
        usuario.setId(txtId.getText());
        usuario.setCorreo(txtCorreo.getText());
        usuario.setContrasena(txtContrasena.getText());
    }

    @FXML
    private void cancelar() {
        usuario = null;
        cerrarVentana();
    }

    private boolean validarCampos() {
        // Implementar validaciones según tus requisitos
        return true;
    }

    private void cerrarVentana() {
        txtNombre.getScene().getWindow().hide();
    }

    public Usuario getUsuarioCreado() {
        return usuario;
    }
}
