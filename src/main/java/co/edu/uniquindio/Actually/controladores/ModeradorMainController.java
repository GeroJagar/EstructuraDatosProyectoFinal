package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.Actually;
import co.edu.uniquindio.Actually.excepciones.CampoObligatorioException;
import co.edu.uniquindio.Actually.excepciones.CampoRepetidoException;
import co.edu.uniquindio.Actually.excepciones.CampoVacioException;
import co.edu.uniquindio.Actually.modelo.*;
import co.edu.uniquindio.Actually.utilidades.ArchivoUtilidades;
import co.edu.uniquindio.Actually.utilidades.FileUploader;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class ModeradorMainController {

    @FXML public VBox panelUsuarios;
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
    @FXML public TextField txtBuscarIdEliminar;
    @FXML public Button btnBuscarEliminar;
    @FXML public GridPane gridDatosEliminar;
    @FXML public TextField txtNombreEliminar;
    @FXML public TextField txtIdEliminar;
    @FXML public TextField txtCorreoEliminar;
    @FXML public PasswordField txtContrasenaEliminar;
    @FXML public HBox boxBotonesEliminar;
    @FXML public TableColumn<Estudiante, String> colPuntaje;
    @FXML public VBox panelContenidos;
    @FXML public TableView<ContenidoAcademico> tablaContenidos;
    @FXML public TableColumn<ContenidoAcademico, String> colTituloContenido;
    @FXML public TableColumn<ContenidoAcademico, String> colTipoContenido;
    @FXML public TableColumn<ContenidoAcademico, String> colAutorContenido;
    @FXML public TableColumn<ContenidoAcademico, String> colTemaContenido;
    @FXML public TableColumn<ContenidoAcademico, Double> colPuntajeContenido;
    @FXML public TextField txtBusquedaContenido;
    @FXML public HBox boxBotonesCrud;
    @FXML public VBox contenidoPanel;
    @FXML public TextField txtTitulo;
    @FXML public ComboBox<TEMA> cbTema;
    @FXML public ComboBox<TIPOCONTENIDO> cbTipoContenido;
    @FXML public TextField txtAutor;
    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML public TableColumn<Usuario, String> colNombre;
    @FXML public TableColumn<Usuario, String> colId;
    @FXML public TableColumn<Usuario, String> colCorreo;
    @FXML public TableColumn<Usuario, String> colDetalle;
    @FXML public TableColumn<Usuario, String> colParticipacion;

    Map<String, Usuario> mapaUsuarios = Actually.getInstance().getUsuarios();
    Map<String, ContenidoAcademico> mapaContenidos = Actually.getInstance().getContenidos();
    Actually actually = Actually.getInstance();
    private File archivoSeleccionado;
    private final FileUploader fileUploader = new FileUploader();
    private final ArchivoUtilidades archivoUtil = new ArchivoUtilidades();

    @FXML
    public void initialize() {
        configurarTablaUsuarios();
        configurarTablaContenidos();

        tablaContenidos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean haySeleccion = newSelection != null;
            boxBotonesCrud.setVisible(true);
            boxBotonesCrud.setManaged(true);
        });
    }

    private void configurarTablaContenidos() {
        // Configurar columnas
        colTituloContenido.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colTemaContenido.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTema().getName()));
        colAutorContenido.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colTipoContenido.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoContenido().name()));
        colPuntajeContenido.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().calcularPuntuacion()).asObject());
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Formatear columna de puntaje
        colPuntajeContenido.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%.2f", item));
            }
        });

        ObservableList<ContenidoAcademico> listaContenidos = FXCollections.observableArrayList(mapaContenidos.values());
        tablaContenidos.setItems(listaContenidos);
    }

    private void configurarTablaContenidos(FilteredList<ContenidoAcademico> listaContenidos) {
        // Configurar columnas
        colTituloContenido.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colTemaContenido.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTema().getName()));
        colAutorContenido.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colTipoContenido.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoContenido().name()));
        colPuntajeContenido.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().calcularPuntuacion()).asObject());
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Formatear columna de puntaje
        colPuntajeContenido.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%.2f", item));
            }
        });
        tablaContenidos.setItems(listaContenidos);
    }

    private void configurarTablaUsuarios() {
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

    private void configurarTablaUsuarios(FilteredList<Usuario> listaUsuarios) {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));

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
        txtBusqueda.clear();
        ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList(mapaUsuarios.values());
        tablaUsuarios.setItems(listaUsuarios);
    }

    @FXML
    public void performSearch(KeyEvent keyEvent){
        String searchText = txtBusqueda.getText().toLowerCase();
        if(searchText.isEmpty()){
            configurarTablaUsuarios();
        } else {
            ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList(mapaUsuarios.values());
            FilteredList<Usuario> usuariosFiltrados = listaUsuarios.filtered(event -> event.getNombre().toLowerCase().contains(searchText));
            configurarTablaUsuarios(usuariosFiltrados);
        }
    }

    @FXML
    public void performSearchContenido(KeyEvent keyEvent) {
        String searchText = txtBusquedaContenido.getText().toLowerCase();
        if(searchText.isEmpty()){
            configurarTablaContenidos();
        } else {
            ObservableList<ContenidoAcademico> listaContenidos = FXCollections.observableArrayList(mapaContenidos.values());
            FilteredList<ContenidoAcademico> contenidosFiltrados = listaContenidos.filtered(event -> event.getTema().getName().toLowerCase().contains(searchText));
            configurarTablaContenidos(contenidosFiltrados);
        }
    }

    @FXML
    public void abrirPanelUsuarios(ActionEvent event) {
        agregarFormulario.setVisible(false);
        agregarFormulario.setManaged(false);
        editarFormulario.setVisible(false);
        editarFormulario.setManaged(false);
        panelContenidos.setVisible(false);
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
            configurarTablaUsuarios();
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
        configurarTablaUsuarios();
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

    @FXML
    public void abrirPanelContenidos(ActionEvent actionEvent) {
        panelUsuarios.setVisible(false);
        agregarFormulario.setVisible(false);
        editarFormulario.setVisible(false);
        eliminarFormulario.setVisible(false);
        contenidoPanel.setVisible(false);
        
        panelContenidos.setVisible(true);
        panelContenidos.setManaged(true);
    }

    @FXML
    public void vistaFormularioContenido(ActionEvent actionEvent) {
        panelUsuarios.setVisible(false);
        agregarFormulario.setVisible(false);
        editarFormulario.setVisible(false);
        eliminarFormulario.setVisible(false);
        panelContenidos.setVisible(false);

        contenidoPanel.setVisible(true);
        contenidoPanel.setManaged(true);

        cbTema.getItems().setAll(TEMA.values());
        cbTipoContenido.getItems().setAll(TIPOCONTENIDO.values());
    }

    @FXML
    public void DeleteContenido(ActionEvent actionEvent) throws IOException {
        // Obtener el contenido seleccionado en la tabla
        ContenidoAcademico contenidoSeleccionado = tablaContenidos.getSelectionModel().getSelectedItem();

        if (contenidoSeleccionado != null) {
            // Mostrar confirmación antes de eliminar
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Eliminar el contenido '" + contenidoSeleccionado.getTitulo() + "'?");
            confirmacion.setContentText("Esta acción no se puede deshacer.");

            Optional<ButtonType> resultado = confirmacion.showAndWait();

            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                // Llamar al método de eliminación en tu capa de negocio/servicio
                actually.eliminarContenido(contenidoSeleccionado.getId()); // Asume que tienes un método similar a deleteStudent

                // Mostrar mensaje de éxito
                mostrarMensaje(Alert.AlertType.INFORMATION,
                        "El contenido '" + contenidoSeleccionado.getTitulo() + "' ha sido eliminado.");

                // Actualizar la tabla (opcional: recargar datos o remover el item)
                tablaContenidos.getItems().remove(contenidoSeleccionado);
            }
        } else {
            mostrarMensaje(Alert.AlertType.WARNING, "Selecciona un contenido de la tabla primero.");
        }
    }

    @FXML
    public void seleccionarArchivo(ActionEvent actionEvent) {
        if (!validarCampos()) {
            mostrarMensaje(Alert.AlertType.WARNING, "Por favor complete todos los campos obligatorios");
            return;
        }

        Stage stage = (Stage) contenidoPanel.getScene().getWindow();
        archivoSeleccionado = fileUploader.abrirSelectorDeArchivo(
                stage,
                cbTipoContenido.getValue().toString().toLowerCase()
        );

        if (archivoSeleccionado != null) {
            subirContenido();
        }
    }

    private void subirContenido() {
        try {
            // Crear el objeto contenido con los metadatos
            ContenidoAcademico contenido = new ContenidoAcademico();
            contenido.setTitulo(txtTitulo.getText());
            contenido.setTema(cbTema.getValue());
            contenido.setAutor(txtAutor.getText());
            contenido.setTipoContenido(cbTipoContenido.getValue());
            contenido.setId(archivoUtil.generarId());

            // Procesar el archivo según su tipo
            switch (contenido.getTipoContenido()) {
                case TEXTO:
                    contenido.setContenido(archivoUtil.leerArchivoComoTexto(archivoSeleccionado));
                    break;
                case PDF:
                    contenido.setContenido("PDF:" + archivoSeleccionado.getAbsolutePath());
                    break;
                case VIDEO:
                    contenido.setContenido("VIDEO:" + archivoSeleccionado.getAbsolutePath());
                    break;
            }

            // Guardar el contenido en el sistema
            actually.subirContenidoModerador(contenido);
            mostrarMensaje(Alert.AlertType.INFORMATION, "Contenido subido correctamente");

            // Limpiar y actualizar la vista
            limpiarFormulario();
            abrirPanelContenidos(null);
            configurarTablaContenidos();
            cbTema.getItems().setAll(TEMA.values());
            cbTipoContenido.getItems().setAll(TIPOCONTENIDO.values());

        } catch (Exception e) {
            mostrarMensaje(Alert.AlertType.ERROR, "Error al subir el contenido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void limpiarFormulario() {
        txtTitulo.clear();
        cbTema.getSelectionModel().clearSelection();
        cbTipoContenido.getSelectionModel().clearSelection();
        archivoSeleccionado = null;
        cbTema.getItems().setAll(TEMA.values());
        cbTipoContenido.getItems().setAll(TIPOCONTENIDO.values());
    }

    private boolean validarCampos() {
        return txtTitulo.getText() != null && !txtTitulo.getText().isBlank() &&
                cbTema.getValue() != null &&
                cbTipoContenido.getValue() != null;
    }

    @FXML
    public void onCancelarButtonClick(ActionEvent actionEvent) {
        abrirPanelContenidos(null);
        configurarTablaContenidos();
    }
}

