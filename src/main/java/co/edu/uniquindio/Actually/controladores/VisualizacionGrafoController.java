package co.edu.uniquindio.Actually.controladores;

import co.edu.uniquindio.Actually.Actually;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import org.graphstream.graph.Graph;
import org.graphstream.ui.fx_viewer.FxViewPanel;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.javafx.FxGraphRenderer;

public class VisualizacionGrafoController {

    @FXML
    private StackPane grafoContainer;

    private Actually actually = Actually.getInstance();

    @FXML
    public void initialize() {
        mostrarGrafoCombinado();
    }

    private void mostrarGrafoCombinado() {
        Graph grafoCombinado = actually.getGestorGrafos().generarGrafoCombinado();

        configurarEstilosGrafo(grafoCombinado);

        FxViewer viewer = new FxViewer(grafoCombinado, FxViewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();

        FxViewPanel panel = (FxViewPanel) viewer.addDefaultView(false, new FxGraphRenderer());

        grafoContainer.getChildren().clear();
        grafoContainer.getChildren().add(panel);
    }

    private void configurarEstilosGrafo(Graph grafo) {
        String estiloGrafo = """
        graph {
            padding: 30px;
        }
        node {
            size: 20px;
            shape: circle;
            fill-mode: plain;
            fill-color: #4a6baf;
            stroke-mode: plain;
            stroke-color: #2c3e50;
            stroke-width: 1px;
            text-alignment: center;
            text-size: 14;
            text-style: bold;
            text-color: #2c3e50;
        }
        node.moderador {
            fill-color: #e74c3c;
        }
        edge {
            shape: line;
            fill-color: #7f8c8d;
            arrow-size: 5px, 2px;
            text-alignment: above;
            text-size: 12;
            text-style: italic;
            text-color: #34495e;
        }
        edge.interes {
            fill-color: #27ae60;
        }
        edge.amistad {
            fill-color: #3498db;
        }
    """;

        grafo.setAttribute("ui.stylesheet", estiloGrafo);

        // Etiquetas para nodos
        grafo.nodes().forEach(nodo -> nodo.setAttribute("ui.label", nodo.getId()));

        // ❌ Eliminar aristas reflexivas
        grafo.edges()
                .filter(e -> e.getNode0().equals(e.getNode1()))
                .toList()  // evita ConcurrentModificationException
                .forEach(grafo::removeEdge);

        // ✅ Estilizar aristas restantes
        grafo.edges().forEach(arista -> {
            String tipo = (String) arista.getAttribute("tipo");
            if (tipo != null) {
                arista.setAttribute("ui.class", tipo);

                if ("interes".equals(tipo)) {
                    Object temas = arista.getAttribute("temas");
                    arista.setAttribute("ui.label", temas != null ? temas.toString() : "Interés");
                } else if ("amistad".equals(tipo)) {
                    arista.setAttribute("ui.label", "Amigos");
                }
            } else {
                System.err.println("[WARN] Arista sin tipo entre: " +
                        arista.getNode0().getId() + " y " + arista.getNode1().getId());
            }
        });
    }
}
