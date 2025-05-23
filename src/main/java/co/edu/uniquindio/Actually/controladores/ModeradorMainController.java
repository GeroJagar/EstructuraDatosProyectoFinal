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
    private void manejarContenidos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/moderador/contenidoAcademicoCrud.fxml"));
            Parent root = loader.load();

            // Obtener la ventana actual
            Stage stage = (Stage) btnContenidos.getScene().getWindow();

            // Reemplazar la escena
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gestión de Contenidos");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void manejarUsuarios() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/moderador/UsuariosCrud.fxml"));
            Parent root = loader.load();

            // Obtener la ventana actual
            Stage stage = (Stage) btnContenidos.getScene().getWindow();

            // Reemplazar la escena
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gestión de Usuarios");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void manejarReportes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/moderador/reportes.fxml"));
            Parent root = loader.load();

            // Obtener la ventana actual
            Stage stage = (Stage) btnContenidos.getScene().getWindow();

            // Reemplazar la escena
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Reportes");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void manejarGrafos() {
        /*
        hay que hacer ventana para los grafos
         */
    }

    @FXML
    private void manejarCerrarSesion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/moderador/contenidoAcademicoCrud.fxml"));
            Parent root = loader.load();

            // Obtener la ventana actual
            Stage stage = (Stage) btnContenidos.getScene().getWindow();

            // Reemplazar la escena
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Actually Application");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirPanelUsuarios(MouseEvent event) {
        agregarFormulario.setVisible(false);
        agregarFormulario.setManaged(false);
        panelUsuarios.setVisible(true);
        panelUsuarios.setManaged(true);
    }

    public void vistaFormularioEstudiante(ActionEvent event) {
        panelUsuarios.setVisible(false);
        panelUsuarios.setManaged(false);
        agregarFormulario.setVisible(true);
        agregarFormulario.setManaged(true);
    }

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
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtId.clear();
        txtCorreo.clear();
        txtContrasena.clear();
    }
}

