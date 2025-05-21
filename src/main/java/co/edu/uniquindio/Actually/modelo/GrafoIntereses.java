package co.edu.uniquindio.Actually.modelo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GrafoIntereses {
    private Map<String, Set<TEMA>> intereses = new HashMap<>();

    public void agregarEstudiante(String id, Set<TEMA> temas) {
        intereses.put(id, new HashSet<>(temas));
    }

    public void actualizarIntereses(String id, Set<TEMA> nuevosTemas) {
        intereses.put(id, new HashSet<>(nuevosTemas));
    }

    public void eliminarEstudiante(String id) {
        intereses.remove(id);
    }
}
