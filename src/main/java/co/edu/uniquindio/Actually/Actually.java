package co.edu.uniquindio.Actually;

import co.edu.uniquindio.Actually.excepciones.*;
import co.edu.uniquindio.Actually.modelo.*;
import co.edu.uniquindio.Actually.utilidades.ArchivoUtilidades;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class Actually {

    private static Actually actually;
    private Usuario usuarioActivo;

    private Map<String, Usuario> usuarios = new HashMap<>();
    private Map<String, ContenidoAcademico> contenidos = new HashMap<>();

    private final String RUTA_USUARIOS = "src/main/resources/serializacion/usuarios.data";
    private final String RUTA_CONTENIDOS = "src/main/resources/serializacion/contenidos.data";

    public static Actually getInstance() {
        if (actually == null) {
            actually = new Actually();
        }
        return actually;
    }

    public void inicializar() {
        try {
            this.usuarios = (Map<String, Usuario>) ArchivoUtilidades.deserializarObjeto(RUTA_USUARIOS);
            for (Usuario usuario : this.usuarios.values()) {
                System.out.println(usuario);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se encontraron usuarios serializados, iniciando con una lista vacía.");
        }

        try {
            this.contenidos = (Map<String, ContenidoAcademico>) ArchivoUtilidades.deserializarObjeto(RUTA_CONTENIDOS);
            for (ContenidoAcademico contenido : this.contenidos.values()) {
                System.out.println(contenido);
            }
            System.out.println("Contenidos cargados: " + contenidos.size());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se encontraron contenidos serializados, iniciando con una lista vacía.");
        }
    }

    public Usuario getUsuarioActivo() {
        return usuarioActivo;
    }

    public void setUsuarioActivo(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
    }

    public void registrarEstudiante(String nombre, String id, String correo, String contrasena)
            throws CampoObligatorioException, CampoVacioException, CampoRepetidoException {

        if (nombre == null || nombre.isBlank()) {
            throw new CampoObligatorioException("El nombre es obligatorio");
        }
        if (id == null || id.isBlank()) {
            throw new CampoVacioException("El ID es obligatorio");
        }
        if (correo == null || correo.isBlank()) {
            throw new CampoVacioException("El correo es obligatorio");
        }
        if (contrasena == null || contrasena.isBlank()) {
            throw new CampoVacioException("La contraseña es obligatoria");
        }

        for (Usuario usuario : usuarios.values()) {
            if (usuario.getCorreo().equals(correo)) {
                throw new CampoRepetidoException("Ya existe un usuario con este correo");
            }
        }

        if (usuarios.containsKey(id)) {
            throw new CampoRepetidoException("Ya existe un usuario con este ID");
        }

        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(nombre);
        estudiante.setId(id);
        estudiante.setCorreo(correo);
        estudiante.setContrasena(contrasena);

        usuarios.put(id, estudiante);

        try {
            ArchivoUtilidades.serializarObjeto(RUTA_USUARIOS, usuarios);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarMensaje(Alert.AlertType.ERROR, "Error al guardar los usuarios: " + e.getMessage());
        }

        mostrarMensaje(Alert.AlertType.INFORMATION, "Registro exitoso");
    }

    public Usuario obtenerUsuarioPorId(String id) {
        return usuarios.get(id);
    }

    public void subirContenidoAcademico(ContenidoAcademico contenido) throws Exception {
        if (!(usuarioActivo instanceof Estudiante)) {
            throw new Exception("El usuario activo no es un estudiante válido.");
        }

        Estudiante estudiante = (Estudiante) usuarioActivo;

        if (contenido == null || contenido.getId() == null || contenido.getId().isBlank()) {
            throw new Exception("Contenido inválido");
        }

        if (contenidos.containsKey(contenido.getId())) {
            throw new Exception("Ya existe contenido con ese ID");
        }

        // Asignar el autor si está vacío
        if (contenido.getAutor() == null || contenido.getAutor().isBlank()) {
            contenido.setAutor(estudiante.getId());  // Usar el ID del estudiante que está subiendo el contenido
        }

        estudiante.subirContenido(contenido);
        contenidos.put(contenido.getId(), contenido);

        // Persistencia
        ArchivoUtilidades.serializarObjeto(RUTA_CONTENIDOS, contenidos);
        ArchivoUtilidades.serializarObjeto(RUTA_USUARIOS, usuarios);

    }

    public void changePassword(String estudianteId, String newPassword) throws Exception{
        if (!usuarios.containsKey(estudianteId)) {
            throw new Exception("El estudiante no existe");
        }
        Estudiante estudiante = (Estudiante) usuarios.get(estudianteId);
        estudiante.setContrasena(newPassword);
        ArchivoUtilidades.serializarObjeto(RUTA_USUARIOS, usuarios);
    }

    public void valorarContenido(String estudianteId, String contenidoId, int puntaje) throws Exception {
        if (!contenidos.containsKey(contenidoId)) {
            throw new Exception("El contenido no existe");
        }

        if (!usuarios.containsKey(estudianteId)) {
            throw new Exception("El estudiante no existe");
        }

        if (puntaje < 1 || puntaje > 5) {
            throw new Exception("La puntuación debe estar entre 1 y 5");
        }

        ContenidoAcademico contenido = contenidos.get(contenidoId);
        Estudiante estudiante = (Estudiante) usuarios.get(estudianteId);

        // Validar que no sea su propio contenido
        if (estudiante.getContenidosSubidos().stream().anyMatch(c -> c.getId().equals(contenidoId))) {
            throw new Exception("No puedes valorar tu propio contenido");
        }

        // Validar que no haya valorado antes
        for (Valoracion v : contenido.getValoraciones()) {
            if (v.getEstudianteId().equals(estudianteId)) {
                throw new Exception("Ya has valorado este contenido");
            }
        }

        Valoracion valoracion = new Valoracion(estudianteId, puntaje);
        contenido.agregarValoracion(valoracion);

        // Persistencia
        ArchivoUtilidades.serializarObjeto(RUTA_CONTENIDOS, contenidos);

    }


    public Map<String, ContenidoAcademico> getContenidos() {
        return contenidos;
    }

    public void loadStage(String url, Event event) {
        try {
            if (event != null) {
                ((Node) (event.getSource())).getScene().getWindow().hide();
            }
            Parent root = FXMLLoader.load(Objects.requireNonNull(Actually.class.getResource(url)));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("Actually");
            newStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            mostrarMensaje(Alert.AlertType.ERROR, "Error al cargar la ventana: " + ex.getMessage());
        }
    }

    public void cerrarSesion(ActionEvent event) {
        // Limpiar el usuario activo
        usuarioActivo = null;

        // Aquí llamamos a loadStage para abrir la pantalla de inicio
        loadStage("/ventanas/common/login.fxml", event); // Modifica esta ruta por la del login real
    }

    public void mostrarMensaje(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setContentText(mensaje);
        alert.show();
    }

    public ContenidoAcademico buscarContenido(String criterio, String clave) throws Exception {
        if (criterio == null || clave == null || criterio.isBlank() || clave.isBlank()) {
            throw new Exception("El criterio y la clave no pueden estar vacíos.");
        }
        ABBContenido arbolContenido = new ABBContenido();
        for (ContenidoAcademico contenido : contenidos.values()) {
            String valorClave;
            switch (criterio.toLowerCase()) {
                case "titulo":
                    valorClave = contenido.getTitulo();
                    break;
                case "autor":
                    valorClave = contenido.getAutor();
                    break;
                case "tema":
                    valorClave = contenido.getTema().name();
                    break;
                default:
                    throw new Exception("Criterio no válido.");
            }
            arbolContenido.insertar(contenido, valorClave);
        }
        ContenidoAcademico resultado = arbolContenido.buscar(clave);
        if (resultado == null) throw new Exception("No se encontró contenido.");
        return resultado;
    }

    public List<ContenidoAcademico> buscarContenido(String clave) throws Exception {
        if (clave == null || clave.isBlank()) {
            throw new Exception("La clave no puede estar vacía.");
        }

        List<ContenidoAcademico> resultados = new ArrayList<>();
        String claveLower = clave.toLowerCase();

        for (ContenidoAcademico contenido : contenidos.values()) {
            // Convertimos los campos a minúsculas para búsqueda insensible a mayúsculas
            String titulo = contenido.getTitulo().toLowerCase();
            String autor = contenido.getAutor().toLowerCase();
            String tema = contenido.getTema().name().toLowerCase();

            if (titulo.contains(claveLower) || autor.contains(claveLower) || tema.contains(claveLower)) {
                resultados.add(contenido);
            }
        }

        if (resultados.isEmpty()) {
            throw new Exception("No se encontró contenido que coincida con la clave.");
        }

        return resultados;
    }
}
