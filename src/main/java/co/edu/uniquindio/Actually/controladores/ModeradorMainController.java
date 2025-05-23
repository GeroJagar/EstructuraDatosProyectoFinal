package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.Actually;
import co.edu.uniquindio.Actually.modelo.Estudiante;
import co.edu.uniquindio.Actually.modelo.Usuario;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

    @FXML
    public void initialize() {
        configurarFiltros();
        configurarTabla();
    }

    private void configurarFiltros() {
        cbTipoUsuario.getItems().addAll("Todos", "Estudiante", "Moderador");
        cbTipoUsuario.getSelectionModel().selectFirst();
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
        ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList(mapaUsuarios.values());
        tablaUsuarios.setItems(listaUsuarios);
    }

    private void configurarTabla(FilteredList<Usuario> listaUsuarios) {
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

    public void abrirPanelUsuarios(MouseEvent event) {
        panelUsuarios.setVisible(true);
        panelUsuarios.setManaged(true);
    }
}

