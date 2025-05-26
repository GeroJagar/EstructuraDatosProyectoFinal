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
    public Map<TEMA, List<GrupoEstudio>> generarSugerenciasGrupos() {
        Map<TEMA, List<GrupoEstudio>> sugerencias = new HashMap<>();

        // 1. Cargar grupos EXISTENTES (no crear nuevos)
        List<GrupoEstudio> gruposExistentes = actually.getGruposEstudio();

        // 2. Agrupar por tema
        for (GrupoEstudio grupo : gruposExistentes) {
            sugerencias.computeIfAbsent(grupo.getTema(), k -> new ArrayList<>()).add(grupo);
        }

        return sugerencias;
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