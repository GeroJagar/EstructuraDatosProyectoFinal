package co.edu.uniquindio.Actually.modelo;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import java.util.*;

public class GrafoAmistades {

    private final MultiGraph grafoAmistades;
    private final Map<String, Set<String>> amistades;

    public GrafoAmistades() {
        this.grafoAmistades = new MultiGraph("Amistades");
        this.amistades = new HashMap<>();

        // Estilos para mostrar etiquetas
        grafoAmistades.setAttribute("ui.stylesheet", "edge { text-alignment: above; }");
    }

    public void agregarEstudiante(String id) {
        if (grafoAmistades.getNode(id) == null) {
            grafoAmistades.addNode(id).setAttribute("ui.label", id);
        }
        amistades.putIfAbsent(id, new HashSet<>());
    }

    public void agregarAmistad(String id1, String id2) {
        if (id1.equals(id2)) return; // No se permite amistad consigo mismo

        agregarEstudiante(id1);
        agregarEstudiante(id2);

        amistades.get(id1).add(id2);
        amistades.get(id2).add(id1);

        String edgeId = generarIdArista(id1, id2);
        if (grafoAmistades.getEdge(edgeId) == null) {
            Edge edge = grafoAmistades.addEdge(edgeId, id1, id2, false);
            edge.setAttribute("tipo", "Amigos");
            edge.setAttribute("ui.label", "Amigos");
        }
    }

    public void eliminarAmistad(String id1, String id2) {
        amistades.getOrDefault(id1, new HashSet<>()).remove(id2);
        amistades.getOrDefault(id2, new HashSet<>()).remove(id1);

        String edgeId = generarIdArista(id1, id2);
        if (grafoAmistades.getEdge(edgeId) != null) {
            grafoAmistades.removeEdge(edgeId);
        }
    }

    public void eliminarEstudiante(String id) {
        if (!amistades.containsKey(id)) return;

        Set<String> amigos = new HashSet<>(amistades.get(id));
        for (String otro : amigos) {
            eliminarAmistad(id, otro);
        }

        amistades.remove(id);
        grafoAmistades.removeNode(id);
    }

    private String generarIdArista(String id1, String id2) {
        String nodoA = id1.compareTo(id2) < 0 ? id1 : id2;
        String nodoB = id1.compareTo(id2) < 0 ? id2 : id1;
        return nodoA + "_" + nodoB;
    }

    public Graph getGrafo() {
        return grafoAmistades;
    }
}