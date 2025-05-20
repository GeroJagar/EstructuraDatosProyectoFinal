package co.edu.uniquindio.Actually.modelo;

import java.util.*;

public class GrafoAmistades {
    private Map<String, Set<String>> grafoAmistades;
    private Map<String, Set<TEMA>> interesesEstudiantes;

    public GrafoAmistades() {
        grafoAmistades = new HashMap<>();
        interesesEstudiantes = new HashMap<>();
    }

    public void agregarEstudiante(String idEstudiante, Set<TEMA> intereses) {
        grafoAmistades.putIfAbsent(idEstudiante, new HashSet<>());
        interesesEstudiantes.put(idEstudiante, new HashSet<>(intereses));
    }

    public void actualizarIntereses(String idEstudiante, Set<TEMA> nuevosIntereses) {
        if (interesesEstudiantes.containsKey(idEstudiante)) {
            interesesEstudiantes.get(idEstudiante).clear();
            interesesEstudiantes.get(idEstudiante).addAll(nuevosIntereses);
        }
    }

    public void agregarAmistad(String idEstudiante1, String idEstudiante2) {
        grafoAmistades.putIfAbsent(idEstudiante1, new HashSet<>());
        grafoAmistades.putIfAbsent(idEstudiante2, new HashSet<>());

        grafoAmistades.get(idEstudiante1).add(idEstudiante2);
        grafoAmistades.get(idEstudiante2).add(idEstudiante1);
    }

    public List<String> obtenerSugerencias(String idEstudiante, int limite) {
        Set<String> sugerencias = new HashSet<>();
        Set<String> amigosDirectos = grafoAmistades.getOrDefault(idEstudiante, new HashSet<>());
        Set<TEMA> misIntereses = interesesEstudiantes.getOrDefault(idEstudiante, new HashSet<>());

        // Si no tiene amigos, sugerir por intereses comunes
        if (amigosDirectos.isEmpty()) {
            return sugerirPorIntereses(idEstudiante, misIntereses, limite);
        }

        // Si tiene amigos, sugerir amigos de amigos con intereses comunes
        Map<String, Integer> puntuaciones = new HashMap<>();

        for (String amigo : amigosDirectos) {
            for (String amigoDeAmigo : grafoAmistades.getOrDefault(amigo, new HashSet<>())) {
                if (!amigoDeAmigo.equals(idEstudiante) && !amigosDirectos.contains(amigoDeAmigo)) {
                    Set<TEMA> interesesAmigo = interesesEstudiantes.getOrDefault(amigoDeAmigo, new HashSet<>());
                    int puntos = (int) interesesAmigo.stream()
                            .filter(misIntereses::contains)
                            .count();

                    puntuaciones.put(amigoDeAmigo, puntuaciones.getOrDefault(amigoDeAmigo, 0) + puntos + 1); // +1 por la conexión
                }
            }
        }

        // Si no hay suficientes sugerencias, completar con intereses comunes
        if (puntuaciones.size() < limite) {
            sugerencias.addAll(sugerirPorIntereses(idEstudiante, misIntereses, limite - puntuaciones.size()));
        }

        // Ordenar por puntuación
        List<String> resultado = new ArrayList<>(puntuaciones.keySet());
        resultado.sort((a, b) -> puntuaciones.get(b) - puntuaciones.get(a));

        return resultado.subList(0, Math.min(limite, resultado.size()));
    }

    private List<String> sugerirPorIntereses(String idEstudiante, Set<TEMA> misIntereses, int limite) {
        Map<String, Integer> puntuaciones = new HashMap<>();

        for (Map.Entry<String, Set<TEMA>> entry : interesesEstudiantes.entrySet()) {
            String otroEstudiante = entry.getKey();
            if (!otroEstudiante.equals(idEstudiante) && !grafoAmistades.getOrDefault(idEstudiante, new HashSet<>()).contains(otroEstudiante)) {
                int puntos = (int) entry.getValue().stream()
                        .filter(misIntereses::contains)
                        .count();

                if (puntos > 0) {
                    puntuaciones.put(otroEstudiante, puntos);
                }
            }
        }

        List<String> resultado = new ArrayList<>(puntuaciones.keySet());
        resultado.sort((a, b) -> puntuaciones.get(b) - puntuaciones.get(a));

        return resultado.subList(0, Math.min(limite, resultado.size()));
    }

    public void eliminarAmistad(String idEstudiante1, String idEstudiante2) {
    }
}