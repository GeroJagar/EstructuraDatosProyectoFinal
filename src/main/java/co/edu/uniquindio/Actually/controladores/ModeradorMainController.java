package co.edu.uniquindio.Actually.controladores;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class ModeradorMainController {

    @FXML public VBox panelUsuarios;
    @FXML public ComboBox<String> cbTipoUsuario;
    @FXML public TextField txtBusqueda;
    @FXML private Text titulo;
    @FXML private Button btnContenidos;
    @FXML private Button btnUsuarios;
    @FXML private Button btnReportes;
    @FXML private Button btnGrafos;
    @FXML private Button cerrarSesion;

    @FXML
    public void initialize() {
        configurarFiltros();
    }

    private void configurarFiltros() {
        cbTipoUsuario.getItems().addAll("Todos", "Estudiante", "Moderador");
        cbTipoUsuario.getSelectionModel().selectFirst();
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

