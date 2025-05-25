package co.edu.uniquindio.Actually.modelo;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    // Agrega un estudiante con sus intereses (y lo agrega a ambos grafos)
    public void agregarEstudiante(String id, Set<TEMA> intereses) {
        grafoAmistades.agregarEstudiante(id);
        grafoIntereses.agregarEstudiante(id, intereses);
    }

    // Actualiza solo los intereses del estudiante
    public void actualizarIntereses(String id, Set<TEMA> nuevosIntereses) {
        grafoIntereses.actualizarIntereses(id, nuevosIntereses);
    }

    // Crea una amistad entre dos estudiantes
    public void agregarAmistad(String id1, String id2) {
        grafoAmistades.agregarAmistad(id1, id2);
    }

    // Elimina al estudiante de ambos grafos
    public void eliminarEstudiante(String id) {
        grafoAmistades.eliminarEstudiante(id);
        grafoIntereses.eliminarEstudiante(id);
    }

    /*
    Método para generar el grafo entero, combinando el grafo de intereses y el grafo de amistades, dando prioridad a las
    aristas del grafo de intereses.
     */

    public Graph generarGrafoCombinado() {
        Graph grafoCombinado = new MultiGraph("GrafoCombinado");

        // Añadir todos los nodos únicos (estudiantes)
        Set<String> todosLosNodos = new HashSet<>();
        grafoIntereses.getGrafo().nodes().forEach(n -> todosLosNodos.add(n.getId()));
        grafoAmistades.getGrafo().nodes().forEach(n -> todosLosNodos.add(n.getId()));

        for (String id : todosLosNodos) {
            if (grafoCombinado.getNode(id) == null) {
                grafoCombinado.addNode(id);
            }
        }

        Set<String> aristasAgregadas = new HashSet<>();

        // ➤ 1. Agregar aristas de intereses primero (prioridad)
        for (Edge e : grafoIntereses.getGrafo().edges().toList()) {
            String source = e.getNode0().getId();
            String target = e.getNode1().getId();
            String idArista = generarIdArista(source, target);

            if (!aristasAgregadas.contains(idArista)) {
                Edge nueva = grafoCombinado.addEdge(idArista, source, target, false);
                nueva.setAttribute("tipo", "interes");
                nueva.setAttribute("tema", e.getAttribute("tema"));
                aristasAgregadas.add(idArista);
            }
        }

        // ➤ 2. Agregar aristas de amistad solo si no están ya
        for (Edge e : grafoAmistades.getGrafo().edges().toList()) {
            String source = e.getNode0().getId();
            String target = e.getNode1().getId();
            String idArista = generarIdArista(source, target);

            if (!aristasAgregadas.contains(idArista)) {
                Edge nueva = grafoCombinado.addEdge(idArista, source, target, false);
                nueva.setAttribute("tipo", "amistad");
                aristasAgregadas.add(idArista);
            }
        }

        return grafoCombinado;
    }

    public void sincronizarGrafosConEstudiantes(List<String> estudiantesActuales) {
        Set<String> idsActuales = new HashSet<>(estudiantesActuales);

        // Eliminar nodos que no están en la lista en grafo de amistades
        Set<String> nodosAmistades = new HashSet<>();
        grafoAmistades.getGrafo().nodes().forEach(n -> nodosAmistades.add(n.getId()));
        for (String id : nodosAmistades) {
            if (!idsActuales.contains(id)) {
                grafoAmistades.eliminarEstudiante(id);
            }
        }

        // Eliminar nodos que no están en la lista en grafo de intereses
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

    // Accesores para los grafos
    public GrafoAmistades getGrafoAmistades() {
        return grafoAmistades;
    }

    public GrafoIntereses getGrafoIntereses() {
        return grafoIntereses;
    }
}