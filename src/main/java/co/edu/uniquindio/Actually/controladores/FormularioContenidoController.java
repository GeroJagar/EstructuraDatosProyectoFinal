package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.modelo.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class FormularioContenidoController {

    @FXML private TextField txtTitulo;
    @FXML private ComboBox<TEMA> cbTema;
    @FXML private TextField txtAutor;
    @FXML private ComboBox<TIPOCONTENIDO> cbTipo;
    @FXML private TextArea txtContenido;
    @FXML private TextField txtId;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private ContenidoAcademico contenido;
    private boolean esNuevo;

    @FXML
    private void initialize() {
        // Configurar comboboxes
        cbTema.getItems().setAll(TEMA.values());
        cbTipo.getItems().setAll(TIPOCONTENIDO.values());
    }

    public void setContenido(ContenidoAcademico contenido, boolean esNuevo) {
        this.contenido = contenido;
        this.esNuevo = esNuevo;

        if (!esNuevo) {
            txtTitulo.setText(contenido.getTitulo());
            cbTema.setValue(contenido.getTema());
            txtAutor.setText(contenido.getAutor());
            cbTipo.setValue(contenido.getTipoContenido());
            txtContenido.setText(contenido.getContenido());
            txtId.setText(contenido.getId());
            txtId.setEditable(false); // El ID no se puede modificar
        }
    }

    @FXML
    private ContenidoAcademico guardar() {
        // Validar campos
        if (validarCampos()) {
            contenido.setTitulo(txtTitulo.getText());
            contenido.setTema(cbTema.getValue());
            contenido.setAutor(txtAutor.getText());
            contenido.setTipoContenido(cbTipo.getValue());
            contenido.setContenido(txtContenido.getText());

            if (esNuevo) {
                contenido.setId(txtId.getText());
            }

            // Cerrar la ventana
            txtTitulo.getScene().getWindow().hide();
        }
        return contenido;
    }

    @FXML
    private void cancelar() {
        txtTitulo.getScene().getWindow().hide();
    }

    private boolean validarCampos() {
        // Implementar validaciones según tus requisitos
        return true;
    }

    public ContenidoAcademico getContenido() {
        // Solo retornamos el contenido si se guardó exitosamente
        if (contenido != null && contenido.getTitulo() != null && !contenido.getTitulo().isEmpty()) {
            return contenido;
        } else {
            return null;
        }
    }
}
