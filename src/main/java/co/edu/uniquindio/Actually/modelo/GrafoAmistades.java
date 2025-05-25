package co.edu.uniquindio.Actually.modelo;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class GrafoAmistades implements Serializable {

    private static GrafoAmistades instance;

    private final MultiGraph grafoAmistades;
    private final Map<String, Set<String>> amistades;

    public GrafoAmistades() {
        this.grafoAmistades = new MultiGraph("Amistades");
        this.amistades = new HashMap<>();

        // Estilos para mostrar etiquetas
        grafoAmistades.setAttribute("ui.stylesheet", "edge { text-alignment: above; }");
    }

    public static GrafoAmistades getInstance() {
        if (instance == null) {
            instance = new GrafoAmistades();
        }
        return instance;
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

    public List<String> recomendarAmigos(String idEstudiante) {
        List<String> recomendaciones = new ArrayList<>();

        Node nodo = grafoAmistades.getNode(idEstudiante);
        if (nodo == null) return recomendaciones;

        // Obtener amigos directos
        Set<String> amigosDirectos = new HashSet<>();
        for (Edge e : nodo.edges().toList()) {
            Node amigo = e.getOpposite(nodo);
            amigosDirectos.add(amigo.getId());
        }

        // Buscar amigos de amigos
        for (String amigoId : amigosDirectos) {
            Node nodoAmigo = grafoAmistades.getNode(amigoId);
            if (nodoAmigo == null) continue;

            for (Edge e : nodoAmigo.edges().toList()) {
                Node amigoDeAmigo = e.getOpposite(nodoAmigo);
                String idAmigoDeAmigo = amigoDeAmigo.getId();

                if (!idAmigoDeAmigo.equals(idEstudiante) && !amigosDirectos.contains(idAmigoDeAmigo)) {
                    recomendaciones.add(idAmigoDeAmigo);
                }
            }
        }

        return recomendaciones;
    }

    public void cargarDesdePersistencia(Graph grafoGuardado) {
        // 1. Limpiar el grafo actual
        this.grafoAmistades.clear();  // Elimina todos los nodos y aristas

        // 2. Copiar nodos del grafo guardado
        grafoGuardado.nodes().forEach(nodo -> {
            Node nuevoNodo = this.grafoAmistades.addNode(nodo.getId());
            nuevoNodo.setAttribute("ui.label", nodo.getId());
        });

        // 3. Copiar aristas
        grafoGuardado.edges().forEach(arista -> {
            Edge nuevaArista = this.grafoAmistades.addEdge(
                    arista.getId(),
                    arista.getNode0().getId(),
                    arista.getNode1().getId(),
                    false
            );
            nuevaArista.setAttribute("tipo", arista.getAttribute("tipo"));
        });
    }

}