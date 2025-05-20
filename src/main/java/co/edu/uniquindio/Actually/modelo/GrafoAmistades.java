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
        Set<String> sugerencias = new LinkedHashSet<>(); // Usamos LinkedHashSet para mantener el orden
        Set<String> amigosDirectos = grafoAmistades.getOrDefault(idEstudiante, new HashSet<>());
        Set<TEMA> misIntereses = interesesEstudiantes.getOrDefault(idEstudiante, new HashSet<>());

        // Primero obtenemos amigos de amigos con intereses comunes
        Map<String, Integer> puntuacionesAmigosDeAmigos = new HashMap<>();

        for (String amigo : amigosDirectos) {
            for (String amigoDeAmigo : grafoAmistades.getOrDefault(amigo, new HashSet<>())) {
                if (!amigoDeAmigo.equals(idEstudiante) && !amigosDirectos.contains(amigoDeAmigo)) {
                    Set<TEMA> interesesAmigo = interesesEstudiantes.getOrDefault(amigoDeAmigo, new HashSet<>());
                    int puntos = (int) interesesAmigo.stream()
                            .filter(misIntereses::contains)
                            .count();

                    puntuacionesAmigosDeAmigos.put(amigoDeAmigo,
                            puntuacionesAmigosDeAmigos.getOrDefault(amigoDeAmigo, 0) + puntos + 2); // +2 por ser amigo de amigo
                }
            }
        }

        // Luego obtenemos todas las personas con intereses comunes (que no sean amigos directos)
        Map<String, Integer> puntuacionesIntereses = new HashMap<>();

        for (Map.Entry<String, Set<TEMA>> entry : interesesEstudiantes.entrySet()) {
            String otroEstudiante = entry.getKey();
            if (!otroEstudiante.equals(idEstudiante) && !amigosDirectos.contains(otroEstudiante)) {
                int puntos = (int) entry.getValue().stream()
                        .filter(misIntereses::contains)
                        .count();

                if (puntos > 0) {
                    puntuacionesIntereses.put(otroEstudiante, puntos);
                }
            }
        }

        // Combinamos ambos conjuntos, dando prioridad a amigos de amigos
        Map<String, Integer> puntuacionesCombinadas = new HashMap<>();
        puntuacionesCombinadas.putAll(puntuacionesAmigosDeAmigos);

        // Sumamos puntos a las sugerencias por intereses que ya existan
        puntuacionesIntereses.forEach((k, v) ->
                puntuacionesCombinadas.merge(k, v, Integer::sum));

        // Ordenamos primero por puntuación, luego por tipo (amigos de amigos primero)
        List<String> resultado = new ArrayList<>(puntuacionesCombinadas.keySet());
        resultado.sort((a, b) -> {
            int cmp = puntuacionesCombinadas.get(b) - puntuacionesCombinadas.get(a);
            if (cmp != 0) return cmp;

            // Si tienen la misma puntuación, amigos de amigos primero
            boolean aEsAmigoDeAmigo = puntuacionesAmigosDeAmigos.containsKey(a);
            boolean bEsAmigoDeAmigo = puntuacionesAmigosDeAmigos.containsKey(b);

            if (aEsAmigoDeAmigo && !bEsAmigoDeAmigo) return -1;
            if (!aEsAmigoDeAmigo && bEsAmigoDeAmigo) return 1;
            return 0;
        });

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