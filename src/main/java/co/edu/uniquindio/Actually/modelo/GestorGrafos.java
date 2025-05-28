package co.edu.uniquindio.Actually.modelo;

import co.edu.uniquindio.Actually.utilidades.ArchivoUtilidades;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GestorGrafos {

    private static GestorGrafos instance;

    private final GrafoAmistades grafoAmistades;
    private final GrafoIntereses grafoIntereses;

    public GestorGrafos() {
        this.grafoAmistades = GrafoAmistades.getInstance();
        this.grafoIntereses = GrafoIntereses.getInstance();
    }

    public static GestorGrafos getInstance() {
        if (instance == null) {
            instance = new GestorGrafos();
        }
        return instance;
    }

    public void agregarEstudiante(String id, Set<TEMA> intereses) {
        grafoAmistades.agregarEstudiante(id);
        grafoIntereses.agregarEstudiante(id, intereses);
    }

    public void actualizarIntereses(String id, Set<TEMA> nuevosIntereses) {
        grafoIntereses.actualizarIntereses(id, nuevosIntereses);
    }

    public void agregarAmistad(String id1, String id2) {
        grafoAmistades.agregarAmistad(id1, id2);
    }

    public void eliminarEstudiante(String id) {
        grafoAmistades.eliminarEstudiante(id);
        grafoIntereses.eliminarEstudiante(id);
    }

    public void guardarGrafos() throws IOException {
        Map<String, List<String>> amistades = new HashMap<>();
        grafoAmistades.getGrafo().nodes().forEach(nodo -> {
            List<String> amigos = nodo.edges()
                    .map(e -> e.getOpposite(nodo).getId())
                    .collect(Collectors.toList());
            amistades.put(nodo.getId(), amigos);
        });

        Map<String, List<String>> intereses = new HashMap<>();
        grafoIntereses.getGrafo().nodes().forEach(nodo -> {
            if (nodo.getId().startsWith("EST_")) {
                List<String> temas = nodo.edges()
                        .map(e -> e.getOpposite(nodo).getId())
                        .collect(Collectors.toList());
                intereses.put(nodo.getId(), temas);
            }
        });

        // Aquí puedes guardar usando ArchivoUtilidades si lo necesitas
    }

    /**
     * Genera un grafo combinado desde los grafos de intereses y amistades.
     * Se priorizan las aristas de interés.
     * No se agregan lazos (aristas entre el mismo nodo).
     */
    public Graph generarGrafoCombinado() {
        Graph grafoCombinado = new MultiGraph("GrafoCombinado");

        Set<String> todosLosNodos = new HashSet<>();
        grafoIntereses.getGrafo().nodes().forEach(n -> todosLosNodos.add(n.getId()));
        grafoAmistades.getGrafo().nodes().forEach(n -> todosLosNodos.add(n.getId()));

        for (String id : todosLosNodos) {
            if (grafoCombinado.getNode(id) == null) {
                grafoCombinado.addNode(id);
            }
        }

        Set<String> aristasAgregadas = new HashSet<>();

        // ➤ 1. Aristas de intereses
        for (Edge e : grafoIntereses.getGrafo().edges().toList()) {
            String source = e.getNode0().getId();
            String target = e.getNode1().getId();

            if (source.equals(target)) continue; // ❌ Evita lazo

            String idArista = generarIdArista(source, target);
            if (!aristasAgregadas.contains(idArista)) {
                Edge nueva = grafoCombinado.addEdge(idArista, source, target, false);
                if (nueva != null) {
                    nueva.setAttribute("tipo", "interes");
                    nueva.setAttribute("temas", e.getAttribute("temas"));
                    aristasAgregadas.add(idArista);
                }
            }
        }

        // ➤ 2. Aristas de amistad (solo si no existe ya)
        for (Edge e : grafoAmistades.getGrafo().edges().toList()) {
            String source = e.getNode0().getId();
            String target = e.getNode1().getId();

            if (source.equals(target)) continue; // ❌ Evita lazo

            String idArista = generarIdArista(source, target);
            if (!aristasAgregadas.contains(idArista)) {
                Edge nueva = grafoCombinado.addEdge(idArista, source, target, false);
                if (nueva != null) {
                    nueva.setAttribute("tipo", "amistad");
                    aristasAgregadas.add(idArista);
                }
            }
        }

        return grafoCombinado;
    }

    public void sincronizarGrafosConEstudiantes(List<String> estudiantesActuales) {
        Set<String> idsActuales = new HashSet<>(estudiantesActuales);

        Set<String> nodosAmistades = new HashSet<>();
        grafoAmistades.getGrafo().nodes().forEach(n -> nodosAmistades.add(n.getId()));
        for (String id : nodosAmistades) {
            if (!idsActuales.contains(id)) {
                grafoAmistades.eliminarEstudiante(id);
            }
        }

        Set<String> nodosIntereses = new HashSet<>();
        grafoIntereses.getGrafo().nodes().forEach(n -> nodosIntereses.add(n.getId()));
        for (String id : nodosIntereses) {
            if (!idsActuales.contains(id)) {
                grafoIntereses.eliminarEstudiante(id);
            }
        }
    }

    private String generarIdArista(String id1, String id2) {
        return id1.compareTo(id2) < 0 ? id1 + "_" + id2 : id2 + "_" + id1;
    }

    public GrafoAmistades getGrafoAmistades() {
        return grafoAmistades;
    }

    public GrafoIntereses getGrafoIntereses() {
        return grafoIntereses;
    }
}
