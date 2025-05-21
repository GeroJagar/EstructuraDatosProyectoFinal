package co.edu.uniquindio.Actually.controladores;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class ModeradorMainController {

    @FXML
    private Text titulo;

    @FXML
    private Button btnContenidos;

    @FXML
    private Button btnUsuarios;

    @FXML
    private Button btnReportes;

    @FXML
    private Button btnGrafos;

    @FXML
    private Button cerrarSesion;

    @FXML
    public void initialize() {

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
}

