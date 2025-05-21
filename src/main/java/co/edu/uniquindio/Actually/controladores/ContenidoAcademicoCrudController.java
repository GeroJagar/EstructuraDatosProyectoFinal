package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.modelo.*;
import javafx.beans.property.SimpleDoubleProperty;
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

public class ContenidoAcademicoCrudController {

    // Elementos de la tabla
    @FXML private TableView<ContenidoAcademico> tablaContenidos;
    @FXML private TableColumn<ContenidoAcademico, String> colTitulo;
    @FXML private TableColumn<ContenidoAcademico, String> colTema;
    @FXML private TableColumn<ContenidoAcademico, String> colAutor;
    @FXML private TableColumn<ContenidoAcademico, String> colTipo;
    @FXML private TableColumn<ContenidoAcademico, Double> colPuntaje;
    @FXML private TableColumn<ContenidoAcademico, String> colId;

    // Barra de búsqueda
    @FXML private ComboBox<String> comboCriterio;
    @FXML private TextField campoBusqueda;
    @FXML private Button btnBuscar;
    @FXML private Button btnLimpiarBusqueda;

    // Botones CRUD
    @FXML private Button btnAgregar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;

    // Datos
    private ObservableList<ContenidoAcademico> listaContenidos = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        configurarTabla();
        configurarBusqueda();
        configurarEventos();
    }

    private void configurarTabla() {
        // Configurar columnas
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colTema.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTema().getName()));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colTipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoContenido().name()));
        colPuntaje.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().calcularPuntuacion()).asObject());
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Formatear columna de puntaje
        colPuntaje.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%.2f", item));
            }
        });

        tablaContenidos.setItems(listaContenidos);
    }

    private void configurarBusqueda() {
        comboCriterio.getItems().addAll(
                "Título",
                "Tema",
                "Autor",
                "Tipo",
                "ID"
        );
    }

    private void configurarEventos() {
        btnBuscar.setOnAction(e -> buscarContenido());
        btnLimpiarBusqueda.setOnAction(e -> limpiarBusqueda());
        btnAgregar.setOnAction(e -> mostrarFormularioAgregar());
        btnModificar.setOnAction(e -> mostrarFormularioModificar());
        btnEliminar.setOnAction(e -> eliminarContenido());
    }

    private void buscarContenido() {
        String criterio = comboCriterio.getValue();
        String texto = campoBusqueda.getText().toLowerCase();

        if (criterio == null || texto.isEmpty()) {
            mostrarAlerta("Error", "Seleccione un criterio e ingrese texto");
            return;
        }

        listaContenidos.setAll(listaContenidos.filtered(c -> {
            switch (criterio) {
                case "Título": return c.getTitulo().toLowerCase().contains(texto);
                case "Tema": return c.getTema().getName().toLowerCase().contains(texto);
                case "Autor": return c.getAutor().toLowerCase().contains(texto);
                case "Tipo": return c.getTipoContenido().name().toLowerCase().contains(texto);
                case "ID": return c.getId().toLowerCase().contains(texto);
                default: return false;
            }
        }));
    }

    private void limpiarBusqueda() {
        comboCriterio.getSelectionModel().clearSelection();
        campoBusqueda.clear();
        // Aquí deberías recargar todos los datos originales
        tablaContenidos.setItems(listaContenidos);
    }

    private void mostrarFormularioAgregar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/formularioContenido.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Agregar Nuevo Contenido");
            stage.initModality(Modality.APPLICATION_MODAL);

            FormularioContenidoController controller = loader.getController();
            controller.setContenido(new ContenidoAcademico(), true);

            stage.showAndWait();

            // Si se guardó el contenido (deberías implementar un método en el controlador del formulario para obtenerlo)
            if (controller.getContenido() != null) {
                listaContenidos.add(controller.getContenido());
            }

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar el formulario");
            e.printStackTrace();
        }
    }

    private void mostrarFormularioModificar() {
        ContenidoAcademico seleccionado = tablaContenidos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Error", "Seleccione un contenido");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/formularioContenido.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modificar Contenido");
            stage.initModality(Modality.APPLICATION_MODAL);

            FormularioContenidoController controller = loader.getController();
            controller.setContenido(new ContenidoAcademico(seleccionado), false);

            stage.showAndWait();

            // Refrescar la tabla para mostrar cambios
            tablaContenidos.refresh();

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar el formulario");
            e.printStackTrace();
        }
    }

    private void eliminarContenido() {
        ContenidoAcademico seleccionado = tablaContenidos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Error", "Seleccione un contenido");
            return;
        }

        if (mostrarConfirmacion("¿Eliminar contenido " + seleccionado.getTitulo() + "?")) {
            listaContenidos.remove(seleccionado);
        }
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
