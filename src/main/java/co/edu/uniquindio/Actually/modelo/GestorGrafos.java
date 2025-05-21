package co.edu.uniquindio.Actually.modelo;

import java.util.Set;

public class GestorGrafos {

    private final GrafoAmistades grafoAmistades;
    private final GrafoIntereses grafoIntereses;

    public GestorGrafos() {
        grafoAmistades = new GrafoAmistades();
        grafoIntereses = new GrafoIntereses();
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

    public GrafoAmistades getGrafoAmistades() {
        return grafoAmistades;
    }

    public GrafoIntereses getGrafoIntereses() {
        return grafoIntereses;
    }
}


