package co.edu.uniquindio.Actually.modelo;

import co.edu.uniquindio.Actually.Actually;
import co.edu.uniquindio.Actually.utilidades.ArchivoUtilidades;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GestorGruposEstudio {
    private static GestorGruposEstudio instance;
    private final Actually actually;
    private final GestorGrafos gestorGrafos;

    private GestorGruposEstudio() {
        this.actually = Actually.getInstance();
        this.gestorGrafos = GestorGrafos.getInstance();
    }

    public static GestorGruposEstudio getInstance() {
        if (instance == null) {
            instance = new GestorGruposEstudio();
        }
        return instance;
    }

    // Generar sugerencias de grupos basados en intereses
    public Map<TEMA, List<GrupoEstudio>> generarSugerenciasGrupos() throws IOException {
        Map<TEMA, Set<GrupoEstudio>> sugerencias = new HashMap<>();

        // Mapa de grupos ya existentes por tema (solo uno por tema)
        Map<TEMA, GrupoEstudio> gruposExistentesPorTema = actually.getGruposEstudio().stream()
                .collect(Collectors.toMap(GrupoEstudio::getTema, g -> g, (g1, g2) -> g1));

        // Detectar comunidades en el grafo de intereses
        Set<Set<String>> comunidadesUnicas = new HashSet<>(
                gestorGrafos.getGrafoIntereses().detectarComunidades(0.1)
        );

        for (Set<String> comunidadIds : comunidadesUnicas) {
            List<Estudiante> miembros = comunidadIds.stream()
                    .map(actually::obtenerEstudiantePorId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            List<TEMA> temasPrincipales = calcularTemasPrincipales(miembros);

            for (TEMA tema : temasPrincipales) {
                if (miembros.size() < 2) continue;

                if (!gruposExistentesPorTema.containsKey(tema)) {
                    GrupoEstudio nuevoGrupo = new GrupoEstudio();
                    nuevoGrupo.setNombre("Grupo de " + tema.getName());
                    nuevoGrupo.setTema(tema);
                    nuevoGrupo.setParticipantes(new ArrayList<>());

                    actually.agregarGrupo(nuevoGrupo);
                    gruposExistentesPorTema.put(tema, nuevoGrupo);
                    actually.guardarGrupos();
                }

                GrupoEstudio grupo = gruposExistentesPorTema.get(tema);
                if (grupo != null) {
                    sugerencias.computeIfAbsent(tema, k -> new HashSet<>()).add(grupo);
                    if (!sugerencias.get(tema).contains(grupo)) {
                        System.out.println("[DEBUG] Añadiendo grupo a sugerencia: " + grupo.getNombre());
                        sugerencias.get(tema).add(grupo);
                    }
                }
            }
        }

        // Log final de verificación
        sugerencias.forEach((tema, grupos) -> {
            System.out.println("[SUGERENCIA] Tema: " + tema + " | Grupos sugeridos: " +
                    grupos.stream().map(GrupoEstudio::getNombre).collect(Collectors.joining(", ")));
        });

        Map<TEMA, List<GrupoEstudio>> resultadoFinal = new HashMap<>();
        sugerencias.forEach((tema, grupos) -> resultadoFinal.put(tema, new ArrayList<>(grupos)));
        return resultadoFinal;
    }


    // === MÉTODO NUEVO PARA CALCULAR EL TEMA PRINCIPAL ===
    private List<TEMA> calcularTemasPrincipales(List<Estudiante> miembros) {
        Map<TEMA, Long> conteoTemas = miembros.stream()
                .flatMap(e -> e.getContenidosSubidos().stream())
                .collect(Collectors.groupingBy(
                        ContenidoAcademico::getTema,
                        Collectors.counting()
                ));

        if (conteoTemas.isEmpty()) return Collections.emptyList();

        long max = conteoTemas.values().stream().max(Long::compare).get();
        return conteoTemas.entrySet().stream()
                .filter(e -> e.getValue() == max)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }


    // Aceptar sugerencia de grupo
    public void aceptarSugerenciaGrupo(Estudiante estudiante, GrupoEstudio grupoSugerido) throws IOException {
        // 1. Buscar grupo existente por tema (o crear uno nuevo)
        GrupoEstudio grupoExistente = actually.obtenerGrupoPorTema(grupoSugerido.getTema());

        if (grupoExistente == null) {
            grupoExistente = new GrupoEstudio();
            grupoExistente.setNombre(grupoSugerido.getNombre());
            grupoExistente.setTema(grupoSugerido.getTema());
            actually.agregarGrupo(grupoExistente);
        }

        // 2. Usar el método agregarParticipante() para mantener la lógica centralizada
        if (!grupoExistente.getParticipantes().contains(estudiante)) {
            grupoExistente.agregarParticipante(estudiante);
            estudiante.agregarGrupoEstudio(grupoExistente);

            // 3. Persistir cambios (ambos lados de la relación)
            actually.guardarGrupos();
            ArchivoUtilidades.serializarObjeto(actually.RUTA_USUARIOS, actually.getUsuarios());
        }
    }



}