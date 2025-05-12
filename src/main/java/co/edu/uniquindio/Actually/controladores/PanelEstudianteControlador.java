package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.Actually;
import co.edu.uniquindio.Actually.modelo.ContenidoAcademico;
import co.edu.uniquindio.Actually.modelo.Estudiante;
import co.edu.uniquindio.Actually.utilidades.ArchivoUtilidades;
import co.edu.uniquindio.Actually.utilidades.FileUploader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class PanelEstudianteControlador {

    private final Actually actually = Actually.getInstance();
    private final FileUploader fileUploader = new FileUploader();
    private final ArchivoUtilidades parser = new ArchivoUtilidades();

    @FXML
    private VBox contenidoPanel;

    @FXML
    private VBox contenedorContenido;

    @FXML
    private ScrollPane scrollContenidos;

    @FXML
    private TextField txtClaveBusqueda;

    @FXML
    private ComboBox<String> cbCriterioBusqueda;

    @FXML
    private HBox contenedorBusqueda;

    @FXML
    public void initialize() {
        cbCriterioBusqueda.getItems().addAll("titulo", "autor", "tema");
        mostrarTodoElContenido(null);
    }

    @FXML
    public void mostrarSubirContenido(MouseEvent event) {
        contenidoPanel.setVisible(true);
        contenidoPanel.setManaged(true);
        scrollContenidos.setVisible(false);
        scrollContenidos.setManaged(false);
        contenedorBusqueda.setVisible(false);
        contenedorBusqueda.setManaged(false);
    }

    @FXML
    public void mostrarContenidoEstudiante(MouseEvent event) {
        contenidoPanel.setVisible(false);
        contenidoPanel.setManaged(false);
        scrollContenidos.setVisible(true);
        scrollContenidos.setManaged(true);
        contenedorBusqueda.setVisible(true);
        contenedorBusqueda.setManaged(true);

        cargarContenidosDelEstudiante();
    }

    @FXML
    public void mostrarTodoElContenido(MouseEvent event) {
        contenidoPanel.setVisible(false);
        contenidoPanel.setManaged(false);
        scrollContenidos.setVisible(true);
        scrollContenidos.setManaged(true);
        contenedorBusqueda.setVisible(true);
        contenedorBusqueda.setManaged(true);

        cargarTodosLosContenidos();
    }


    @FXML
    public void cerrarSesion(ActionEvent event) {
        actually.cerrarSesion(event); // Pasa el evento al método loadStage
    }

    @FXML
    public void subirContenido(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        File archivoSeleccionado = fileUploader.abrirSelectorDeArchivo(stage);

        if (archivoSeleccionado != null) {
            try {
                ContenidoAcademico contenido = parser.procesarArchivoTxt(archivoSeleccionado);
                actually.subirContenidoAcademico(contenido);
                mostrarMensaje(Alert.AlertType.INFORMATION, "Contenido subido correctamente");

                mostrarTodoElContenido(null);

            } catch (IOException e) {
                mostrarMensaje(Alert.AlertType.ERROR, "Error al leer el archivo: " + e.getMessage());
            } catch (Exception e) {
                mostrarMensaje(Alert.AlertType.ERROR, "Error al subir el contenido: " + e.getMessage());
            }
        }
    }

    private void cargarContenidosDelEstudiante() {
        contenedorContenido.getChildren().clear();

        if (!(actually.getUsuarioActivo() instanceof Estudiante estudiante)) {
            mostrarMensaje(Alert.AlertType.ERROR, "No hay un estudiante activo");
            return;
        }

        if (estudiante.getContenidosSubidos().isEmpty()) {
            Label vacio = new Label("Aún no has subido contenidos.");
            vacio.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
            contenedorContenido.getChildren().add(vacio);
            return;
        }

        for (ContenidoAcademico contenido : estudiante.getContenidosSubidos()) {
            agregarVistaDeContenido(contenido);
        }
    }

    private void cargarTodosLosContenidos() {
        contenedorContenido.getChildren().clear();

        if (actually.getContenidos().isEmpty()) {
            Label vacio = new Label("No hay contenidos disponibles.");
            vacio.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
            contenedorContenido.getChildren().add(vacio);
            return;
        }

        for (ContenidoAcademico contenido : actually.getContenidos().values()) {
            agregarVistaDeContenido(contenido);
        }
    }

    private void agregarVistaDeContenido(ContenidoAcademico contenido) {
        VBox card = new VBox(5);
        card.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0; -fx-border-color: #ccc;");

        Label label = new Label("Título: " + contenido.getTitulo()
                + "\nTema: " + contenido.getTema()
                + "\nAutor: " + contenido.getAutor()
                + "\nPuntuación promedio: " + String.format("%.2f", contenido.calcularPuntuacion()));
        TextArea area = new TextArea(contenido.getContenido());
        area.setWrapText(true);
        area.setEditable(false);
        area.setPrefRowCount(5);

        Button btnValorar = new Button("Valorar contenido");
        btnValorar.setOnAction(e -> mostrarDialogoValoracion(contenido));

        String estudianteId = actually.getUsuarioActivo().getId();
        boolean yaValorado = contenido.getValoraciones().stream()
                .anyMatch(v -> v.getEstudianteId().equals(estudianteId));

        boolean esAutor = false;
        if (actually.getUsuarioActivo() instanceof Estudiante estudiante) {
            esAutor = estudiante.getContenidosSubidos().stream()
                    .anyMatch(c -> c.getId().equals(contenido.getId()));
        }

        if (yaValorado || esAutor) {
            btnValorar.setDisable(true);
            btnValorar.setText(esAutor ? "Este contenido es tuyo" : "Ya valorado");
        }

        card.getChildren().addAll(label, area, btnValorar);
        contenedorContenido.getChildren().add(card);
    }

    private void mostrarDialogoValoracion(ContenidoAcademico contenido) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Valorar Contenido");
        dialog.setHeaderText("Ingresa un puntaje del 1 al 5");
        dialog.setContentText("Puntaje:");

        dialog.showAndWait().ifPresent(input -> {
            try {
                int puntaje = Integer.parseInt(input);
                String estudianteId = actually.getUsuarioActivo().getId();
                actually.valorarContenido(estudianteId, contenido.getId(), puntaje);
                mostrarMensaje(Alert.AlertType.INFORMATION, "Valoración registrada con éxito");
                mostrarTodoElContenido(null);
            } catch (NumberFormatException e) {
                mostrarMensaje(Alert.AlertType.ERROR, "Ingresa un número válido entre 1 y 5");
            } catch (Exception e) {
                mostrarMensaje(Alert.AlertType.ERROR, e.getMessage());
            }
        });
    }

    @FXML
    public void buscarContenido(ActionEvent event) {
        String criterio = cbCriterioBusqueda.getValue();
        String clave = txtClaveBusqueda.getText();
        contenedorContenido.getChildren().clear();
        try {
            ContenidoAcademico resultado = actually.buscarContenido(criterio, clave);
            if (resultado != null) {
                agregarVistaDeContenido(resultado);
            } else {
                mostrarMensaje(Alert.AlertType.WARNING, "No se encontraron resultados.");
            }
        } catch (Exception e) {
            mostrarMensaje(Alert.AlertType.ERROR, "Error en la búsqueda: " + e.getMessage());
        }
    }


    private void mostrarMensaje(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setContentText(mensaje);
        alert.show();
    }
}