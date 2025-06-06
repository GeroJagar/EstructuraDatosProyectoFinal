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
import java.util.stream.Collectors;

public class Actually {

    private static Actually actually;
    private Usuario usuarioActivo;
    private GestorGrafos gestorGrafos;
    private Moderador moderador = new Moderador();

    private Map<String, Usuario> usuarios = new HashMap<>();
    private Map<String, ContenidoAcademico> contenidos = new HashMap<>();
    private ColaPrioridad<SolicitudAyuda> colaSolicitudes;
    private Map<String, SolicitudAyuda> solicitudesMap;
    private List<GrupoEstudio> gruposEstudio = new ArrayList<>();
    private GestorGruposEstudio gestorGruposEstudio;

    public final String RUTA_USUARIOS = "src/main/resources/serializacion/usuarios.data";
    private final String RUTA_CONTENIDOS = "src/main/resources/serializacion/contenidos.data";
    private final String RUTA_SOLICITUDES = "src/main/resources/serializacion/solicitudes.data";
    private final String RUTA_GRUPOS = "src/main/resources/serializacion/gruposEstudio.data";
    private final String RUTA_CHATS = "src/main/resources/serializacion/chats.data";


    public static Actually getInstance() {
        if (actually == null) {
            actually = new Actually();
        }
        return actually;
    }

    public void inicializar() {
        try {
            // Cargar usuarios
            this.usuarios = (Map<String, Usuario>) ArchivoUtilidades.deserializarObjeto(RUTA_USUARIOS);
            for (Usuario usuario : this.usuarios.values()) {
                System.out.println(usuario);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se encontraron usuarios serializados, iniciando con una lista vacía.");
        }

        try {
            // Cargar contenidos académicos
            this.contenidos = (Map<String, ContenidoAcademico>) ArchivoUtilidades.deserializarObjeto(RUTA_CONTENIDOS);
            for (ContenidoAcademico contenido : this.contenidos.values()) {
                System.out.println(contenido);
            }
            System.out.println("Contenidos cargados: " + contenidos.size());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se encontraron contenidos serializados, iniciando con una lista vacía.");
        }

        // Inicializar cola de prioridad personalizada para solicitudes
        this.colaSolicitudes = new ColaPrioridad<>();
        this.solicitudesMap = new HashMap<>();

        try {
            // Cargar solicitudes existentes
            List<SolicitudAyuda> solicitudesCargadas = (List<SolicitudAyuda>) ArchivoUtilidades.deserializarObjeto(RUTA_SOLICITUDES);
            for (SolicitudAyuda solicitud : solicitudesCargadas) {
                // Solo agregamos a la cola las solicitudes pendientes
                if (solicitud.getEstado() == SolicitudAyuda.EstadoSolicitud.PENDIENTE) {
                    colaSolicitudes.encolar(solicitud);
                }
                // Todas las solicitudes van al mapa para referencia rápida
                solicitudesMap.put(solicitud.getId(), solicitud);
            }
            System.out.println("Solicitudes cargadas: " + solicitudesMap.size());
            System.out.println("Solicitudes pendientes en cola: " + colaSolicitudes.getTamaño());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se encontraron solicitudes serializadas, iniciando con cola vacía.");
        }

        this.gestorGrafos = GestorGrafos.getInstance();// Inicialización
        inicializarGrafos();
        GrafoIntereses grafoIntereses = GestorGrafos.getInstance().getGrafoIntereses();
        grafoIntereses.verificarAtributosAristas();
        this.gestorGruposEstudio = GestorGruposEstudio.getInstance();

        try {
            this.gruposEstudio = (List<GrupoEstudio>) ArchivoUtilidades.deserializarObjeto(RUTA_GRUPOS);
            System.out.println("Grupos de estudio disponibles: " + imprimirGrupos());
            imprimirMiembrosGrupos();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se encontraron grupos serializados. Iniciando nueva lista.");
            this.gruposEstudio = new ArrayList<>();
        }

        try {
            GestorChats.getInstance().cargarChats(RUTA_CHATS);
            System.out.println("Chats cargados: " + GestorChats.getInstance().getChats().size());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se encontraron chats serializados, iniciando con lista vacía.");
        }

    }

    public void imprimirMiembrosGrupos(){
        for(GrupoEstudio g : gruposEstudio){
            List<Estudiante> miembros = g.getParticipantes();
            System.out.println("Los miembros del grupo " + g.getNombre() + " son: " + miembros.toString());
        }
    }

    public String imprimirGrupos(){
        StringBuilder msj = new StringBuilder();
        for(GrupoEstudio g : gruposEstudio){
            msj.append(", ").append(g.getNombre());
        }
        return msj.toString();
    }

    public GestorGruposEstudio getGestorGruposEstudio() {
        return gestorGruposEstudio;
    }

    public GrupoEstudio obtenerGrupoPorId(String id) {
        return gruposEstudio.stream()
                .filter(g -> g.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Map<String, Usuario> getUsuarios(){
        return this.usuarios;
    }

    public Usuario getUsuarioActivo() {
        return usuarioActivo;
    }

    public void setUsuarioActivo(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
    }

    public void guardarGrupos() throws IOException {
        System.out.println("[DEBUG] Guardando " + this.gruposEstudio.size() + " grupos"); // Verificación
        ArchivoUtilidades.serializarObjeto(RUTA_GRUPOS, this.gruposEstudio);
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

    public Estudiante obtenerEstudiantePorId(String id) {
        return (Estudiante) usuarios.get(id);
    }

    public void agregarGrupo(GrupoEstudio grupo) {
        if (grupo != null && !this.gruposEstudio.contains(grupo)) {
            this.gruposEstudio.add(grupo);
            System.out.println("[DEBUG] Grupo añadido: " + grupo.getNombre()); // Verificación
        }
    }


    public GrupoEstudio obtenerGrupoPorTema(TEMA tema) {
        return gruposEstudio.stream()
                .filter(g -> g.getTema() == tema)
                .findFirst() // Retorna el primer grupo que coincida con el tema
                .orElse(null); // O null si no existe
    }

    public List<GrupoEstudio> getGruposEstudio() {
        return this.gruposEstudio; // Asegúrate de que tienes este atributo declarado
    }

    public List<GrupoEstudio> obtenerGruposDeEstudiante(String idEstudiante) {
        return gruposEstudio.stream()
                .filter(g -> g.getParticipantes().stream().anyMatch(e -> e.getId().equals(idEstudiante)))
                .collect(Collectors.toList());
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

        Set<TEMA> nuevosIntereses = obtenerInteresesEstudiante(estudiante);
        gestorGrafos.actualizarIntereses(estudiante.getId(), nuevosIntereses);
        gestorGrafos.guardarGrafos();

        // Persistencia
        ArchivoUtilidades.serializarObjeto(RUTA_CONTENIDOS, contenidos);
        ArchivoUtilidades.serializarObjeto(RUTA_USUARIOS, usuarios);

    }

    public void subirContenidoModerador(ContenidoAcademico contenido) throws Exception {
        if (contenido == null || contenido.getId() == null || contenido.getId().isBlank()) {
            throw new Exception("Contenido inválido");
        }

        if (contenidos.containsKey(contenido.getId())) {
            throw new Exception("Ya existe contenido con ese ID");
        }

        // Asignar el autor si está vacío
        if (contenido.getAutor() == null || contenido.getAutor().isBlank()) {
            contenido.setAutor("Administrador");  // Usar el ID del estudiante que está subiendo el contenido
        }

        moderador.subirContenido(contenido);
        contenidos.put(contenido.getId(), contenido);

        // Persistencia
        ArchivoUtilidades.serializarObjeto(RUTA_CONTENIDOS, contenidos);
    }

    public void changePassword(String estudianteId, String newPassword) throws Exception{
        if (!usuarios.containsKey(estudianteId)) {
            throw new Exception("El estudiante no existe");
        }
        Estudiante estudiante = (Estudiante) usuarios.get(estudianteId);
        estudiante.setContrasena(newPassword);
        ArchivoUtilidades.serializarObjeto(RUTA_USUARIOS, usuarios);
    }

    public void deleteStudent(String studentID) throws IOException {
        if(usuarios.containsKey(studentID)){
            usuarios.remove(studentID);
            ArchivoUtilidades.serializarObjeto(RUTA_USUARIOS, usuarios);
            gestorGrafos.eliminarEstudiante(studentID);
            gestorGrafos.guardarGrafos();
        }
    }

    public void editStudentIfo(String estudianteId, String nuevoId, String nombre, String correo, String password) throws Exception {
        if(!usuarios.containsKey(estudianteId)){
            throw new Exception("El estudiante no existe: primero debes registrarlo");
        }
        if (usuarios.containsKey(nuevoId) && !nuevoId.equals(estudianteId)) {
            throw new Exception("El nuevo ID ya está en uso por otro estudiante");
        }
        Estudiante estudiante = (Estudiante) usuarios.remove(estudianteId);
        estudiante.setId(nuevoId);
        estudiante.setNombre(nombre);
        estudiante.setCorreo(correo);
        estudiante.setContrasena(password);
        usuarios.put(nuevoId, estudiante);
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

        Estudiante student = (Estudiante) usuarios.get(estudianteId);
        student.agregarPuntos(3);

        // Persistencia
        ArchivoUtilidades.serializarObjeto(RUTA_CONTENIDOS, contenidos);
        ArchivoUtilidades.serializarObjeto(RUTA_USUARIOS, usuarios);
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

    public List<ContenidoAcademico> buscarContenido(String criterio, String clave) throws Exception {
        if (criterio == null || clave == null || criterio.isBlank() || clave.isBlank()) {
            throw new Exception("El criterio y la clave no pueden estar vacíos.");
        }

        List<ContenidoAcademico> resultados = new ArrayList<>();

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

            if (valorClave.equalsIgnoreCase(clave)) {
                resultados.add(contenido);
            }
        }

        if (resultados.isEmpty()) {
            throw new Exception("No se encontraron contenidos que coincidan con la búsqueda.");
        }

        return resultados;
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

    public void crearSolicitudAyuda(TEMA tema, int urgencia, String descripcion) throws Exception {
        if (usuarioActivo == null || !(usuarioActivo instanceof Estudiante)) {
            throw new Exception("Debe haber un estudiante logueado para crear solicitudes");
        }

        if (urgencia < 1 || urgencia > 5) {
            throw new Exception("La urgencia debe estar entre 1 (más urgente) y 5 (menos urgente)");
        }

        SolicitudAyuda solicitud = new SolicitudAyuda();
        solicitud.setTema(tema);
        solicitud.setUrgencia(urgencia);
        solicitud.setSolicitante(usuarioActivo);
        solicitud.setDescripcion(descripcion);
        solicitud.setId(ArchivoUtilidades.generarId());

        // Cambio: Usamos encolar() en lugar de add()
        colaSolicitudes.encolar(solicitud);
        solicitudesMap.put(solicitud.getId(), solicitud);

        guardarSolicitudes();
    }

    public SolicitudAyuda atenderSolicitud() throws Exception {
        if (colaSolicitudes.estaVacia()) {
            throw new Exception("No hay solicitudes pendientes");
        }

        SolicitudAyuda solicitud = colaSolicitudes.desencolar();
        solicitud.setEstado(SolicitudAyuda.EstadoSolicitud.EN_PROCESO);
        solicitudesMap.put(solicitud.getId(), solicitud);

        guardarSolicitudes();
        return solicitud;
    }

    public void marcarSolicitudResuelta(String idSolicitud) throws Exception {
        SolicitudAyuda solicitud = solicitudesMap.get(idSolicitud);
        if (solicitud == null) {
            throw new Exception("Solicitud no encontrada");
        }

        solicitud.setEstado(SolicitudAyuda.EstadoSolicitud.RESUELTA);

        if (solicitud.getEstado() == SolicitudAyuda.EstadoSolicitud.PENDIENTE) {
            colaSolicitudes.eliminar(solicitud);
        }

        guardarSolicitudes();
    }

    public List<SolicitudAyuda> listarSolicitudesPendientes() {
        return colaSolicitudes.obtenerTodosElementos();
    }

    public List<SolicitudAyuda> listarTodasLasSolicitudes() {
        return new ArrayList<>(solicitudesMap.values());
    }

    private void guardarSolicitudes() throws IOException {
        List<SolicitudAyuda> todasSolicitudes = new ArrayList<>(solicitudesMap.values());
        ArchivoUtilidades.serializarObjeto(RUTA_SOLICITUDES, todasSolicitudes);
    }

    public void resolverSolicitud(String idSolicitud, ContenidoAcademico contenidoResuelto) throws Exception {
        if (!solicitudesMap.containsKey(idSolicitud)) {
            throw new Exception("Solicitud no encontrada");
        }

        SolicitudAyuda solicitud = solicitudesMap.get(idSolicitud);

        // Verificar que el usuario activo no sea el solicitante
        if (usuarioActivo != null && usuarioActivo.getId().equals(solicitud.getSolicitante().getId())) {
            throw new Exception("No puedes resolver tus propias solicitudes");
        }

        // Subir el contenido que resuelve la solicitud
        subirContenidoAcademico(contenidoResuelto);

        // Marcar la solicitud como resuelta y vincular el contenido
        solicitud.setEstado(SolicitudAyuda.EstadoSolicitud.RESUELTA);
        solicitud.setIdContenidoResuelto(contenidoResuelto.getId());

        // Eliminar de la cola de prioridad si aún está allí
        colaSolicitudes.eliminar(solicitud);

        // Actualizar el mapa
        solicitudesMap.put(idSolicitud, solicitud);

        guardarSolicitudes();

        if (usuarioActivo instanceof Estudiante) {
            Estudiante resolutor = (Estudiante) usuarioActivo;
            resolutor.agregarPuntos(10); // 10 puntos por resolver una solicitud
        }

        guardarSolicitudes();
        ArchivoUtilidades.serializarObjeto(RUTA_USUARIOS, usuarios);
    }

    // Nuevo metodo para obtener estudiantes ordenados por participación
    public List<Estudiante> obtenerEstudiantesPorParticipacion() {
        return usuarios.values().stream()
                .filter(u -> u instanceof Estudiante)
                .map(u -> (Estudiante) u)
                .sorted((e1, e2) -> Integer.compare(e2.getPuntosParticipacion(), e1.getPuntosParticipacion()))
                .collect(Collectors.toList());
    }

    public SolicitudAyuda obtenerSolicitud(String idSolicitud) throws Exception {
        if (!solicitudesMap.containsKey(idSolicitud)) {
            throw new Exception("Solicitud no encontrada");
        }
        return solicitudesMap.get(idSolicitud);
    }

    private void inicializarGrafos() {
        for (Usuario usuario : usuarios.values()) {
            if (usuario instanceof Estudiante estudiante) {
                try {
                    Set<TEMA> intereses = obtenerInteresesEstudiante(estudiante);
                    gestorGrafos.agregarEstudiante(estudiante.getId(), intereses);

                    if (estudiante.getAmigos() != null) {
                        for (Estudiante amigo : estudiante.getAmigos()) {
                            if (amigo != null && amigo.getId() != null) {
                                gestorGrafos.agregarAmistad(estudiante.getId(), amigo.getId());
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error al inicializar grafos para el estudiante " + estudiante.getId() + ": " + e.getMessage());
                }
            }
        }
    }


    private Set<TEMA> obtenerInteresesEstudiante(Estudiante estudiante) {
        Set<TEMA> intereses = new HashSet<>();
        for (ContenidoAcademico contenido : estudiante.getContenidosSubidos()) {
            intereses.add(contenido.getTema());
        }
        return intereses;
    }

    public List<Estudiante> obtenerSugerenciasAmistades(String idEstudiante) {
        List<String> idsSugerencias = gestorGrafos.getGrafoAmistades().recomendarAmigos(idEstudiante);
        List<String> sugerenciaContenido = gestorGrafos.getGrafoIntereses().recomendarContenido(idEstudiante);

        // Usar un Set para evitar duplicados
        Set<String> idsCombinados = new HashSet<>();
        idsCombinados.addAll(idsSugerencias);
        idsCombinados.addAll(sugerenciaContenido);

        List<Estudiante> sugerencias = new ArrayList<>();

        for (String id : idsCombinados) {
            Usuario usuario = usuarios.get(id);
            if (usuario instanceof Estudiante) {
                sugerencias.add((Estudiante) usuario);
            }
        }

        return sugerencias;
    }


    public void actualizarInteresesEstudiante(String idEstudiante) {
        if (usuarios.containsKey(idEstudiante) && usuarios.get(idEstudiante) instanceof Estudiante) {
            Estudiante estudiante = (Estudiante) usuarios.get(idEstudiante);
            Set<TEMA> nuevosIntereses = obtenerInteresesEstudiante(estudiante);
            gestorGrafos.actualizarIntereses(idEstudiante, nuevosIntereses);
        }
    }

    // Metodo para agregar amistades
    public void agregarAmistad(String idEstudiante1, String idEstudiante2) throws Exception {
        // Validaciones iniciales
        if (idEstudiante1 == null || idEstudiante2 == null) {
            throw new IllegalArgumentException("Los IDs no pueden ser nulos");
        }

        if (idEstudiante1.equals(idEstudiante2)) {
            throw new IllegalArgumentException("Un estudiante no puede ser amigo de sí mismo");
        }

        Estudiante estudiante1 = (Estudiante) usuarios.get(idEstudiante1);
        Estudiante estudiante2 = (Estudiante) usuarios.get(idEstudiante2);

        if (estudiante1 == null || estudiante2 == null) {
            throw new Exception("Uno o ambos estudiantes no existen");
        }

        // Verificar si ya son amigos (evitar duplicados)
        if (estudiante1.getAmigos().contains(estudiante2)) {
            throw new Exception("Los estudiantes ya son amigos");
        }

        try {
            // 1. Crear backup (solo en memoria)
            Map<String, Usuario> backup = new HashMap<>(usuarios);

            // 2. Se actualiza el grafo de amistades antes que la lista de amigos.
            gestorGrafos.agregarAmistad(idEstudiante1, idEstudiante2);

            // 3. Actualizar lista de amigos en los estudiantes.
            estudiante1.agregarAmigo(estudiante2);
            estudiante2.agregarAmigo(estudiante1);

            // 3. Serializar.
            ArchivoUtilidades.serializarObjeto(RUTA_USUARIOS, usuarios);
            gestorGrafos.guardarGrafos();

            // Notificar a los observadores.
            notificarCambiosAmistades(estudiante1, estudiante2);

            System.out.println("Amistad establecida entre " + idEstudiante1 + " y " + idEstudiante2);

        } catch (IOException e) {
            // Revertir cambios en memoria
            gestorGrafos.getGrafoAmistades().eliminarAmistad(idEstudiante1, idEstudiante2);
            if (estudiante1 != null) estudiante1.getAmigos().remove(estudiante2);
            if (estudiante2 != null) estudiante2.getAmigos().remove(estudiante1);

            throw new Exception("Error al guardar la amistad: " + e.getMessage());
        }
    }

    private void notificarCambiosAmistades(Estudiante estudiante1, Estudiante estudiante2) {
        // Puedes implementar un sistema de observadores aquí
        System.out.println("Amistad actualizada: " + estudiante1.getNombre() + " y " + estudiante2.getNombre());
    }

    public GestorGrafos getGestorGrafos() {
        return gestorGrafos;
    }

    public void guardarChats() throws IOException {
        GestorChats gestorChats = GestorChats.getInstance();
        gestorChats.guardarChats(RUTA_CHATS);
    }

    public void eliminarContenido(String idContenido) throws IOException {
        if(contenidos.containsKey(idContenido)){
            contenidos.remove(idContenido);
            ArchivoUtilidades.serializarObjeto(RUTA_CONTENIDOS, contenidos);
        }
    }
}