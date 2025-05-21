package co.edu.uniquindio.Actually.modelo;

import java.util.*;

public class GrafoAmistades {
    private Map<String, Set<String>> grafoAmistades;
    private Map<String, Set<TEMA>> interesesEstudiantes;

    public GrafoAmistades() {
        grafoAmistades = new HashMap<>();
    }

    public void agregarEstudiante(String idEstudiante) {
        grafoAmistades.putIfAbsent(idEstudiante, new HashSet<>());
    }

    public void agregarAmistad(String idEstudiante1, String idEstudiante2) {
        if (grafoAmistades.containsKey(idEstudiante1) && grafoAmistades.containsKey(idEstudiante2)) {
            grafoAmistades.get(idEstudiante1).add(idEstudiante2);
            grafoAmistades.get(idEstudiante2).add(idEstudiante1);
        }
    }

    public void eliminarEstudiante(String idEstudiante) {
        grafoAmistades.remove(idEstudiante);
        for (Set<String> amigos : grafoAmistades.values()) {
            amigos.remove(idEstudiante);
        }
    }

    public Set<String> obtenerAmigos(String idEstudiante) {
        return grafoAmistades.getOrDefault(idEstudiante, Collections.emptySet());
    }

    public boolean sonAmigos(String id1, String id2) {
        return grafoAmistades.getOrDefault(id1, Collections.emptySet()).contains(id2);
    }

    public void eliminarAmistad(String id1, String id2) {
        if (grafoAmistades.containsKey(id1)) {
            grafoAmistades.get(id1).remove(id2);
        }
        if (grafoAmistades.containsKey(id2)) {
            grafoAmistades.get(id2).remove(id1);
        }
    }

    public Set<String> obtenerEstudiantes() {
        return grafoAmistades.keySet();
    }
    public List<String> obtenerSugerencias(String idEstudiante, int limite) {
        Set<String> sugerencias = new LinkedHashSet<>();
        Set<String> amigosDirectos = grafoAmistades.getOrDefault(idEstudiante, new HashSet<>());
        Set<TEMA> misIntereses = interesesEstudiantes.getOrDefault(idEstudiante, new HashSet<>());

        // Sugerencias por amigos de amigos
        Map<String, Integer> puntuacionesAmigosDeAmigos = new HashMap<>();

        for (String amigo : amigosDirectos) {
            for (String amigoDeAmigo : grafoAmistades.getOrDefault(amigo, new HashSet<>())) {
                if (!amigoDeAmigo.equals(idEstudiante) && !amigosDirectos.contains(amigoDeAmigo)) {
                    Set<TEMA> interesesAmigo = interesesEstudiantes.getOrDefault(amigoDeAmigo, new HashSet<>());
                    int puntos = (int) interesesAmigo.stream()
                            .filter(misIntereses::contains)
                            .count();

                    puntuacionesAmigosDeAmigos.put(amigoDeAmigo,
                            puntuacionesAmigosDeAmigos.getOrDefault(amigoDeAmigo, 0) + puntos + 2);
                }
            }
        }

        // Sugerencias por intereses en común
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

        // Combinar sugerencias
        Map<String, Integer> puntuacionesCombinadas = new HashMap<>(puntuacionesAmigosDeAmigos);
        puntuacionesIntereses.forEach((k, v) ->
                puntuacionesCombinadas.merge(k, v, Integer::sum));

        // Ordenar sugerencias
        List<String> resultado = new ArrayList<>(puntuacionesCombinadas.keySet());
        resultado.sort((a, b) -> {
            int cmp = puntuacionesCombinadas.get(b) - puntuacionesCombinadas.get(a);
            if (cmp != 0) return cmp;

            boolean aEsAmigoDeAmigo = puntuacionesAmigosDeAmigos.containsKey(a);
            boolean bEsAmigoDeAmigo = puntuacionesAmigosDeAmigos.containsKey(b);

            if (aEsAmigoDeAmigo && !bEsAmigoDeAmigo) return -1;
            if (!aEsAmigoDeAmigo && bEsAmigoDeAmigo) return 1;
            return 0;
        });

        return resultado.subList(0, Math.min(limite, resultado.size()));
    }


}
