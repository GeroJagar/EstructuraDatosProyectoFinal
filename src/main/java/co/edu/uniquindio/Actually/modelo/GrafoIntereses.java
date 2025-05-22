package co.edu.uniquindio.Actually.modelo;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.*;

import java.util.*;

public class GrafoIntereses {

    private final MultiGraph grafoIntereses;
    private final Map<String, Set<TEMA>> interesesEstudiantes; //mapa que asocia cada estudian con los temas que le interesan

    public GrafoIntereses() {
        this.grafoIntereses = new MultiGraph("Intereses");
        this.interesesEstudiantes = new HashMap<>();

        // Habilitar etiquetas en los nodos y aristas
        grafoIntereses.setAttribute("ui.stylesheet", "edge { text-alignment: above; }");
    }

    public void agregarEstudiante(String id, Set<TEMA> temas){
        if(grafoIntereses.getNode(id) == null){
            grafoIntereses.addNode(id).setAttribute("ui-label", id);
        }

        interesesEstudiantes.put(id, new HashSet<>(temas));

        // Comparar con otros estudiantes para crear/actualizar aristas
        for (Map.Entry<String, Set<TEMA>> entry : interesesEstudiantes.entrySet()) {
            String otroId = entry.getKey();
            if (!otroId.equals(id)) {
                Set<TEMA> temasEnComun = new HashSet<>(entry.getValue());
                temasEnComun.retainAll(temas);

                if (!temasEnComun.isEmpty()) {
                    String edgeId = generarIdArista(id, otroId);
                    Edge edge = grafoIntereses.getEdge(edgeId);

                    if (edge == null) {
                        edge = grafoIntereses.addEdge(edgeId, id, otroId, false);
                        edge.setAttribute("temas", temasEnComun);
                    } else {
                        Set<TEMA> existentes = new HashSet<>((Collection<TEMA>) edge.getAttribute("temas"));
                        existentes.addAll(temasEnComun);
                        edge.setAttribute("temas", existentes);
                    }

                    // Actualizar etiqueta visual con los temas
                    edge.setAttribute("ui.label", ((Set<TEMA>) edge.getAttribute("temas")).toString());
                }
            }
        }
    }

    public void actualizarIntereses(String id, Set<TEMA> nuevosTemas) {
        eliminarAristasDeEstudiante(id);
        interesesEstudiantes.put(id, new HashSet<>(nuevosTemas));
        agregarEstudiante(id, nuevosTemas);
    }

    public void eliminarEstudiante(String id) {
        eliminarAristasDeEstudiante(id);
        grafoIntereses.removeNode(id);
        interesesEstudiantes.remove(id);
    }

    private void eliminarAristasDeEstudiante(String id) {
        List<String> aristasAEliminar = new ArrayList<>();
        for (Edge edge : grafoIntereses.edges().toList()) {
            if (edge.getNode0().getId().equals(id) || edge.getNode1().getId().equals(id)) {
                aristasAEliminar.add(edge.getId());
            }
        }

        for (String edgeId : aristasAEliminar) {
            grafoIntereses.removeEdge(edgeId);
        }
    }

    private String generarIdArista(String id1, String id2) {
        // Asegurar orden consistente para aristas no dirigidas
        String nodoA = id1.compareTo(id2) < 0 ? id1 : id2;
        String nodoB = id1.compareTo(id2) < 0 ? id2 : id1;
        return nodoA + "_" + nodoB;
    }

    public Graph getGrafo() {
        return grafoIntereses;
    }

}

