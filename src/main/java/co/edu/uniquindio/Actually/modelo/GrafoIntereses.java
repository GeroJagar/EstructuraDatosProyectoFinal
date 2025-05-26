package co.edu.uniquindio.Actually.modelo;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class GrafoIntereses implements Serializable {

    private static GrafoIntereses instance;

    private final MultiGraph grafoIntereses;
    private final Map<String, Set<TEMA>> interesesEstudiantes; //mapa que asocia cada estudian con los temas que le interesan

    public GrafoIntereses() {
        this.grafoIntereses = new MultiGraph("Intereses");
        this.interesesEstudiantes = new HashMap<>();

        // Habilitar etiquetas en los nodos y aristas
        grafoIntereses.setAttribute("ui.stylesheet", "edge { text-alignment: above; }");
    }

    public static GrafoIntereses getInstance() {
        if (instance == null) {
            instance = new GrafoIntereses();
        }
        return instance;
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

    public List<String> recomendarContenido(String idEstudiante) {
        List<String> recomendaciones = new ArrayList<>();

        Node nodo = grafoIntereses.getNode(idEstudiante);
        if (nodo == null) return recomendaciones;

        // Obtener nodos con arista directa (vecinos)
        for (Edge e : nodo.edges().toList()) {
            Node vecino = e.getOpposite(nodo);
            recomendaciones.add(vecino.getId());
        }

        return recomendaciones;
    }


    public List<Set<String>> detectarComunidades(double umbralMinimo) {
        List<Set<String>> comunidades = new ArrayList<>();
        Set<String> visitados = new HashSet<>();

        // Ordenar nodos por grado (mayor a menor)
        List<Node> nodosOrdenados = grafoIntereses.nodes()
                .sorted((n1, n2) -> Integer.compare(n2.getDegree(), n1.getDegree()))
                .collect(Collectors.toList());

        for (Node nodo : nodosOrdenados) {
            String idActual = nodo.getId();

            if (!visitados.contains(idActual)) {
                Set<String> comunidad = new HashSet<>();
                Stack<String> pila = new Stack<>();
                pila.push(idActual);

                while (!pila.isEmpty()) {
                    String actual = pila.pop();

                    if (!visitados.contains(actual)) {
                        visitados.add(actual);
                        comunidad.add(actual);

                        Node nodoActual = grafoIntereses.getNode(actual);
                        Set<TEMA> interesesActual = interesesEstudiantes.get(actual);

                        // Explorar vecinos ordenados por similitud
                        List<Edge> aristasOrdenadas = nodoActual.edges()
                                .sorted(compararAristasPorSimilitud(interesesActual))
                                .collect(Collectors.toList());

                        for (Edge arista : aristasOrdenadas) {
                            Node vecino = arista.getOpposite(nodoActual);
                            String idVecino = vecino.getId();

                            if (!visitados.contains(idVecino)) {
                                Set<TEMA> temasComunes = (Set<TEMA>) arista.getAttribute("temas");
                                double similitud = (double) temasComunes.size() /
                                        Math.max(interesesActual.size(), interesesEstudiantes.get(idVecino).size());

                                if (similitud >= umbralMinimo) {
                                    pila.push(idVecino);
                                }
                            }
                        }
                    }
                }

                if (comunidad.size() >= 2) { // Bajar el mínimo a 2 miembros
                    comunidades.add(comunidad);
                    System.out.println("Comunidad detectada: " + comunidad +
                            " | Tamaño: " + comunidad.size());
                }
            }
        }

        return comunidades.stream().distinct().collect(Collectors.toList());
    }

    private Comparator<Edge> compararAristasPorSimilitud(Set<TEMA> interesesActual) {
        return (a1, a2) -> {
            Set<TEMA> temas1 = (Set<TEMA>) a1.getAttribute("temas");
            Set<TEMA> temas2 = (Set<TEMA>) a2.getAttribute("temas");
            double sim1 = (double) temas1.size() / interesesActual.size();
            double sim2 = (double) temas2.size() / interesesActual.size();
            return Double.compare(sim2, sim1); // Orden descendente
        };
    }

    public void verificarAtributosAristas() {
        System.out.println("\n=== VERIFICACIÓN DE ATRIBUTOS EN ARISTAS ===");

        grafoIntereses.edges().forEach(edge -> {
            System.out.println("\nArista: " + edge.getId());
            System.out.println("Nodos: " + edge.getNode0().getId() + " <-> " + edge.getNode1().getId());

            Object temas = edge.getAttribute("temas");
            if (temas == null) {
                System.out.println("ERROR: Atributo 'temas' es null");
            } else {
                System.out.println("Tipo del atributo: " + temas.getClass().getName());
                try {
                    @SuppressWarnings("unchecked")
                    Set<TEMA> temasSet = (Set<TEMA>) temas;
                    System.out.println("Temas comunes: " + temasSet);
                } catch (ClassCastException e) {
                    System.out.println("ERROR: El atributo no es un Set<TEMA>");
                    System.out.println("Contenido real: " + temas);
                }
            }
        });
    }

}

