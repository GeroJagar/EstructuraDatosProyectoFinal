package co.edu.uniquindio.Actually.modelo;

import co.edu.uniquindio.Actually.Actually;

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
    public Map<TEMA, List<GrupoEstudio>> generarSugerenciasGrupos() {
        Map<TEMA, List<GrupoEstudio>> sugerencias = new HashMap<>();
        GrafoIntereses grafoIntereses = gestorGrafos.getGrafoIntereses();

        List<Set<String>> comunidades = grafoIntereses.detectarComunidades(0.3);

        for (Set<String> comunidadIds : comunidades) {
            // 1. Solo verificar que existan los estudiantes (pero NO agregarlos aún)
            List<String> idsValidos = comunidadIds.stream()
                    .filter(id -> actually.obtenerEstudiantePorId(id) != null)
                    .collect(Collectors.toList());

            if (idsValidos.size() < 2) continue; // Saltar comunidades pequeñas

            // 2. Calcular temas predominantes (sin filtrar miembros aún)
            Map<TEMA, Long> conteoTemas = idsValidos.stream()
                    .flatMap(id -> actually.obtenerEstudiantePorId(id).getContenidosSubidos().stream())
                    .collect(Collectors.groupingBy(
                            ContenidoAcademico::getTema,
                            Collectors.counting()
                    ));

            // 3. Crear grupos VACÍOS por cada tema relevante
            conteoTemas.entrySet().stream()
                    .filter(entry -> entry.getValue() >= 2) // Mínimo 2 contenidos del tema
                    .sorted(Map.Entry.<TEMA, Long>comparingByValue().reversed())
                    .limit(2) // Máximo 2 temas por comunidad
                    .forEach(entry -> {
                        TEMA tema = entry.getKey();
                        GrupoEstudio grupo = new GrupoEstudio();
                        grupo.setNombre("Grupo de " + tema);
                        grupo.setTema(tema);
                        grupo.setParticipantes(new ArrayList<>()); // ¡Grupo vacío inicialmente!

                        sugerencias.computeIfAbsent(tema, k -> new ArrayList<>()).add(grupo);
                    });
        }
        return sugerencias;
    }

    // Aceptar sugerencia de grupo
    public void aceptarSugerenciaGrupo(Estudiante estudiante, GrupoEstudio grupoSugerido) {
        // 1. Buscar si ya existe un grupo activo para este tema
        GrupoEstudio grupoExistente = actually.obtenerGrupoPorTemaYTamanio(
                grupoSugerido.getTema(), grupoSugerido.getParticipantes().size());

        // 2. Si no existe, crear uno nuevo (vacío)
        if (grupoExistente == null) {
            grupoExistente = new GrupoEstudio();
            grupoExistente.setNombre(grupoSugerido.getNombre());
            grupoExistente.setTema(grupoSugerido.getTema());
            grupoExistente.setParticipantes(new ArrayList<>());
            actually.agregarGrupo(grupoExistente);
        }

        // 3. Verificar que el estudiante no esté ya en el grupo
        if (!grupoExistente.getParticipantes().contains(estudiante)) {
            grupoExistente.agregarParticipante(estudiante);
            estudiante.agregarGrupoEstudio(grupoExistente);
            estudiante.agregarPuntos(15);

            // Actualizar el nombre con el nuevo tamaño
            grupoExistente.setNombre("Grupo de " + grupoExistente.getTema() +
                    " (" + grupoExistente.getParticipantes().size() + " miembros)");
        }
    }

    // Rechazar sugerencia de grupo
    public void rechazarSugerenciaGrupo(Estudiante estudiante, GrupoEstudio grupo) {
        // Registrar rechazo para no mostrar esta sugerencia nuevamente
        estudiante.agregarGrupoRechazado(grupo);
    }
}