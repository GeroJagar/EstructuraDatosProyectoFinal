package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.Actually;
import co.edu.uniquindio.Actually.excepciones.CampoObligatorioException;
import co.edu.uniquindio.Actually.excepciones.CampoRepetidoException;
import co.edu.uniquindio.Actually.excepciones.CampoVacioException;
import co.edu.uniquindio.Actually.modelo.Estudiante;
import co.edu.uniquindio.Actually.modelo.Usuario;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class ModeradorMainController {

    @FXML public VBox panelUsuarios;
    @FXML public ComboBox<String> cbTipoUsuario;
    @FXML public TextField txtBusqueda;
    @FXML public VBox agregarFormulario;
    @FXML public TextField txtNombre;
    @FXML public TextField txtId;
    @FXML public TextField txtCorreo;
    @FXML public PasswordField txtContrasena;
    @FXML public VBox editarFormulario;
    @FXML public GridPane gridDatosEstudiante;
    @FXML public TextField txtNombreEdit;
    @FXML public TextField txtIdEdit;
    @FXML public TextField txtCorreoEdit;
    @FXML public PasswordField txtContrasenaEdit;
    @FXML public TextField txtBuscarId;
    @FXML public HBox boxBotonesEdicion;
    @FXML public Button btnGuardarEdit;
    @FXML public Button btnCancelarEdit;
    @FXML public VBox eliminarFormulario;
    public TextField txtBuscarIdEliminar;
    public Button btnBuscarEliminar;
    public GridPane gridDatosEliminar;
    public TextField txtNombreEliminar;
    public TextField txtIdEliminar;
    public TextField txtCorreoEliminar;
    public PasswordField txtContrasenaEliminar;
    public HBox boxBotonesEliminar;
    @FXML public TableColumn<Estudiante, String> colPuntaje;
    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML public TableColumn<Usuario, String> colNombre;
    @FXML public TableColumn<Usuario, String> colId;
    @FXML public TableColumn<Usuario, String> colCorreo;
    @FXML public TableColumn<Usuario, String> colTipo;
    @FXML public TableColumn<Usuario, String> colDetalle;
    @FXML public TableColumn<Usuario, String> colParticipacion;
    @FXML private Text titulo;
    @FXML private Button btnContenidos;
    @FXML private Button btnUsuarios;
    @FXML private Button btnReportes;
    @FXML private Button btnGrafos;
    @FXML private Button cerrarSesion;

    Map<String, Usuario> mapaUsuarios = Actually.getInstance().getUsuarios();
    Actually actually = Actually.getInstance();

    @FXML
    public void initialize() {
        configurarTabla();
    }

    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));

        // Ya no se verifica si es Estudiante, se asume que todos lo son
        colDetalle.setCellValueFactory(cellData -> {
            Estudiante est = (Estudiante) cellData.getValue();
            return new SimpleStringProperty(
                    String.format("Amigos: %d, Grupos: %d",
                            est.getAmigos().size(),
                            est.getGruposEstudio().size())
            );
        });

        colPuntaje.setCellValueFactory(new PropertyValueFactory<>("puntosParticipacion"));
        colParticipacion.setCellValueFactory(new PropertyValueFactory<>("nivel"));

        ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList(mapaUsuarios.values());
        tablaUsuarios.setItems(listaUsuarios);
    }

    private void configurarTabla(FilteredList<Usuario> listaUsuarios) {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));

        // Ya no se verifica si es Estudiante, se asume que todos lo son
        colDetalle.setCellValueFactory(cellData -> {
            Estudiante est = (Estudiante) cellData.getValue();
            return new SimpleStringProperty(
                    String.format("Amigos: %d, Grupos: %d",
                            est.getAmigos().size(),
                            est.getGruposEstudio().size())
            );
        });
        tablaUsuarios.setItems(listaUsuarios);
    }

    @FXML
    public void limpiarBusqueda(ActionEvent event) {
        cbTipoUsuario.getSelectionModel().selectFirst();
        txtBusqueda.clear();
        ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList(mapaUsuarios.values());
        tablaUsuarios.setItems(listaUsuarios);
    }

    @FXML
    public void performSearch(){
        String searchText = txtBusqueda.getText().toLowerCase();
        if(searchText.isEmpty()){
            configurarTabla();
        } else {
            ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList(mapaUsuarios.values());
            FilteredList<Usuario> usuariosFiltrados = listaUsuarios.filtered(event -> event.getNombre().toLowerCase().contains(searchText));
            configurarTabla(usuariosFiltrados);
        }
    }

    @FXML
    public void abrirPanelUsuarios(ActionEvent event) {
        agregarFormulario.setVisible(false);
        agregarFormulario.setManaged(false);
        editarFormulario.setVisible(false);
        editarFormulario.setManaged(false);
        panelUsuarios.setVisible(true);
        panelUsuarios.setManaged(true);
    }

    @FXML
    public void vistaFormularioEstudiante(ActionEvent event) {
        panelUsuarios.setVisible(false);
        panelUsuarios.setManaged(false);
        editarFormulario.setVisible(false);
        editarFormulario.setManaged(false);
        agregarFormulario.setVisible(true);
        agregarFormulario.setManaged(true);
    }

    @FXML
    public void onSaveStudentButtonClick(ActionEvent event) {
        try {
            actually.registrarEstudiante(
                    txtNombre.getText(),
                    txtId.getText(),
                    txtCorreo.getText(),
                    txtContrasena.getText()
            );

        } catch (CampoVacioException | CampoObligatorioException | CampoRepetidoException e) {
            actually.mostrarMensaje(Alert.AlertType.ERROR, e.getMessage());  // Mostrar alerta de error
        } catch (Exception e) {
            actually.mostrarMensaje(Alert.AlertType.ERROR, "Error inesperado: " + e.getMessage());  // Mostrar alerta de error inesperado
        } finally {
            limpiarCampos();
            configurarTabla();
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtId.clear();
        txtCorreo.clear();
        txtContrasena.clear();
    }

    @FXML
    public void buscarEstudiante(ActionEvent event) {
        String id = txtBuscarId.getText();
        if (!mapaUsuarios.containsKey(id)) {
            mostrarMensaje(Alert.AlertType.ERROR, "El estudiante no existe.");
        }
        Estudiante estudiante = (Estudiante) mapaUsuarios.get(id);
        // Rellenar campos:
        txtNombreEdit.setText(estudiante.getNombre());
        txtIdEdit.setText(estudiante.getId());
        txtCorreoEdit.setText(estudiante.getCorreo());
        txtContrasenaEdit.setText(estudiante.getContrasena());
        // Mostrar secciones:
        gridDatosEstudiante.setVisible(true);
        boxBotonesEdicion.setVisible(true);
    }

    @FXML
    public void guardarEdicionEstudiante(ActionEvent event) {
        try {
            String idOriginal = txtBuscarId.getText();
            String nuevoId = txtIdEdit.getText();
            String nombre = txtNombreEdit.getText();
            String correo = txtCorreoEdit.getText();
            String password = txtContrasenaEdit.getText();

            actually.editStudentIfo(idOriginal, nuevoId, nombre, correo, password);
            mostrarMensaje(Alert.AlertType.CONFIRMATION, "Se editó la información del estudiante correctamente.");
        } catch (Exception e) {
            mostrarMensaje(Alert.AlertType.ERROR, "No se pudo editar la información del estudiante.");
        }
    }

    public void cerrarEdicion(ActionEvent event) {
        abrirPanelUsuarios(null);
    }

    @FXML
    public void editarEstudiante(ActionEvent event) {
        panelUsuarios.setVisible(false);
        panelUsuarios.setManaged(false);
        agregarFormulario.setVisible(false);
        agregarFormulario.setManaged(false);
        editarFormulario.setVisible(true);
        editarFormulario.setManaged(true);
    }

    private void mostrarMensaje(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setContentText(mensaje);
        alert.show();
    }

    @FXML
    public void buscarEstudianteAEliminar(ActionEvent actionEvent) {
        String id = txtBuscarIdEliminar.getText();
        if (mapaUsuarios.containsKey(id)) {
            Estudiante estudiante = (Estudiante) mapaUsuarios.get(id);
            // Mostrar datos (no editables)
            txtNombreEliminar.setText(estudiante.getNombre());
            txtIdEliminar.setText(estudiante.getId());
            txtCorreoEliminar.setText(estudiante.getCorreo());
            txtContrasenaEliminar.setText(estudiante.getContrasena());
            // Mostrar secciones
            gridDatosEliminar.setVisible(true);
            boxBotonesEliminar.setVisible(true);
        } else {
            mostrarMensaje(Alert.AlertType.ERROR, "El estudiante no existe.");
        }
    }

    @FXML
    public void eliminarEstudiante(ActionEvent actionEvent) throws IOException {
        actually.deleteStudent(txtBuscarIdEliminar.getText());
        mostrarMensaje(Alert.AlertType.CONFIRMATION, "El estudiante " + txtNombreEliminar.getText()
                + " ha sido eliminado del sistema.");
    }

    @FXML
    public void cerrarEliminacion(ActionEvent actionEvent) {
        panelUsuarios.setVisible(true);
        panelUsuarios.setManaged(true);
        configurarTabla();
        agregarFormulario.setVisible(false);
        agregarFormulario.setManaged(false);
        editarFormulario.setVisible(false);
        editarFormulario.setManaged(false);
        eliminarFormulario.setVisible(false);
        eliminarFormulario.setManaged(false);
    }

    @FXML
    public void openDeleteStudent(ActionEvent actionEvent) {
        panelUsuarios.setVisible(false);
        panelUsuarios.setManaged(false);
        agregarFormulario.setVisible(false);
        agregarFormulario.setManaged(false);
        editarFormulario.setVisible(false);
        editarFormulario.setManaged(false);
        eliminarFormulario.setVisible(true);
        eliminarFormulario.setManaged(true);
    }

    @FXML
    public void cerrarSesion(ActionEvent actionEvent) {
        actually.loadStage("/ventanas/common/homepage.fxml", actionEvent);
    }
}

