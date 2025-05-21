package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.modelo.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class UsuarioCrudController {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colId;
    @FXML private TableColumn<Usuario, String> colCorreo;
    @FXML private TableColumn<Usuario, String> colTipo;
    @FXML private TableColumn<Usuario, String> colDetalle;

    @FXML private ComboBox<String> cbTipoUsuario;
    @FXML private TextField txtBusqueda;
    @FXML private Button btnBuscar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;

    private ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        configurarTabla();
        configurarFiltros();
        configurarEventos();
        cargarDatosEjemplo();
    }

    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));

        colTipo.setCellValueFactory(cellData -> {
            Usuario usuario = cellData.getValue();
            return new SimpleStringProperty(usuario instanceof Estudiante ? "Estudiante" : "Moderador");
        });

        colDetalle.setCellValueFactory(cellData -> {
            Usuario usuario = cellData.getValue();
            if (usuario instanceof Estudiante) {
                Estudiante est = (Estudiante) usuario;
                return new SimpleStringProperty(
                        String.format("Amigos: %d, Grupos: %d",
                                est.getAmigos().size(),
                                est.getGruposEstudio().size())
                );
            }
            return new SimpleStringProperty("Moderador");
        });

        tablaUsuarios.setItems(listaUsuarios);
    }

    private void configurarFiltros() {
        cbTipoUsuario.getItems().addAll("Todos", "Estudiante", "Moderador");
        cbTipoUsuario.getSelectionModel().selectFirst();
    }

    private void configurarEventos() {
        btnAgregar.setOnAction(e -> mostrarFormularioAgregar());
        btnEditar.setOnAction(e -> mostrarFormularioEditar());
        btnEliminar.setOnAction(e -> eliminarUsuario());
        btnBuscar.setOnAction(e -> buscarUsuarios());
        btnLimpiar.setOnAction(e -> limpiarBusqueda());
    }

    private void mostrarFormularioAgregar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/usuarioFormulario.fxml"));
            Parent root = loader.load();

            UsuarioFormularioController controller = loader.getController();
            controller.setModoCreacion(true);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Agregar Nuevo Usuario");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (controller.getUsuarioCreado() != null) {
                listaUsuarios.add(controller.getUsuarioCreado());
            }

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar el formulario");
            e.printStackTrace();
        }
    }

    private void mostrarFormularioEditar() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Error", "Seleccione un usuario");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/usuarioFormulario.fxml"));
            Parent root = loader.load();

            UsuarioFormularioController controller = loader.getController();
            controller.setUsuario(seleccionado);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Editar Usuario");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            tablaUsuarios.refresh();

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar el formulario");
            e.printStackTrace();
        }
    }

    private void eliminarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Error", "Seleccione un usuario");
            return;
        }

        if (mostrarConfirmacion("¿Eliminar usuario " + seleccionado.getNombre() + "?")) {
            listaUsuarios.remove(seleccionado);
        }
    }

    private void buscarUsuarios() {
        // Implementar lógica de búsqueda
    }

    private void limpiarBusqueda() {
        cbTipoUsuario.getSelectionModel().selectFirst();
        txtBusqueda.clear();
        tablaUsuarios.setItems(listaUsuarios);
    }

    private void cargarDatosEjemplo() {
        // Datos de ejemplo (eliminar en producción)
        Estudiante est1 = new Estudiante();
        est1.setNombre("Juan Pérez");
        est1.setId("EST001");
        est1.setCorreo("juan@email.com");

        Estudiante est2 = new Estudiante();
        est2.setNombre("María Gómez");
        est2.setId("EST002");
        est2.setCorreo("maria@email.com");

        Moderador mod1 = new Moderador();
        mod1.setNombre("Admin Principal");
        mod1.setId("MOD001");
        mod1.setCorreo("admin@email.com");

        listaUsuarios.addAll(est1, est2, mod1);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private boolean mostrarConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        return alert.showAndWait().get() == ButtonType.OK;
    }
}
