package co.edu.uniquindio.Actually.modelo;

import java.util.Set;

public class GestorGrafos {

    private final GrafoAmistades grafoAmistades;
    private final GrafoIntereses grafoIntereses;

    public GestorGrafos() {
        this.grafoAmistades = new GrafoAmistades();
        this.grafoIntereses = new GrafoIntereses();
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

    // Accesores para los grafos (por si los necesitas para visualización)
    public GrafoAmistades getGrafoAmistades() {
        return grafoAmistades;
    }

    public GrafoIntereses getGrafoIntereses() {
        return grafoIntereses;
    }
}