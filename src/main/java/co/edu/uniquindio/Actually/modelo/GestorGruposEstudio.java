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
        Map<TEMA, List<GrupoEstudio>> sugerencias = new HashMap<>();
        Map<TEMA, GrupoEstudio> gruposExistentesPorTema = actually.getGruposEstudio().stream()
                .collect(Collectors.toMap(GrupoEstudio::getTema, g -> g, (g1, g2) -> g1));

        Set<Set<String>> comunidadesUnicas = new HashSet<>(
                gestorGrafos.getGrafoIntereses().detectarComunidades(0.3)
        );

        for (Set<String> comunidadIds : comunidadesUnicas) {
            List<Estudiante> miembrosPotenciales = comunidadIds.stream()  // Cambiamos el nombre para claridad
                    .map(actually::obtenerEstudiantePorId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            TEMA temaPrincipal = calcularTemaPrincipal(miembrosPotenciales);

            if (temaPrincipal != null && miembrosPotenciales.size() >= 2) {
                if (!gruposExistentesPorTema.containsKey(temaPrincipal)) {
                    GrupoEstudio nuevoGrupo = new GrupoEstudio();
                    nuevoGrupo.setNombre("Grupo de " + temaPrincipal.getName());
                    nuevoGrupo.setTema(temaPrincipal);
                    nuevoGrupo.setParticipantes(new ArrayList<>());  // 👈 Grupo vacío inicialmente

                    actually.agregarGrupo(nuevoGrupo);
                    gruposExistentesPorTema.put(temaPrincipal, nuevoGrupo);

                    // Opcional: guardar aquí si es necesario
                    actually.guardarGrupos();
                }

                // Añadir a sugerencias sin duplicados
                if (!sugerencias.containsKey(temaPrincipal) ||
                        !sugerencias.get(temaPrincipal).contains(gruposExistentesPorTema.get(temaPrincipal))) {
                    sugerencias.computeIfAbsent(temaPrincipal, k -> new ArrayList<>())
                            .add(gruposExistentesPorTema.get(temaPrincipal));
                }
            }
        }
        return sugerencias;
    }

    // === MÉTODO NUEVO PARA CALCULAR EL TEMA PRINCIPAL ===
    private TEMA calcularTemaPrincipal(List<Estudiante> miembros) {
        // 1. Contar cuántos contenidos hay de cada tema
        Map<TEMA, Long> conteoTemas = miembros.stream()
                .flatMap(e -> e.getContenidosSubidos().stream())
                .collect(Collectors.groupingBy(
                        ContenidoAcademico::getTema,
                        Collectors.counting()
                ));

        // 2. Obtener el tema con más contenidos
        return conteoTemas.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null); // Retorna null si no hay contenidos
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