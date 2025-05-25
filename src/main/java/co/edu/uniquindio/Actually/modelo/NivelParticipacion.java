package co.edu.uniquindio.Actually.modelo;

public enum NivelParticipacion {
    NOVATO("Novato", 0, 49),
    APRENDIZ("Aprendiz", 50, 99),
    COLABORADOR("Colaborador", 100, 199),
    EXPERTO("Experto", 200, 349),
    MAESTRO("Maestro", 350, Integer.MAX_VALUE);

    private final String nombre;
    private final int minPuntos;
    private final int maxPuntos;

    NivelParticipacion(String nombre, int minPuntos, int maxPuntos) {
        this.nombre = nombre;
        this.minPuntos = minPuntos;
        this.maxPuntos = maxPuntos;
    }

    public static NivelParticipacion determinarNivel(int puntos) {
        for (NivelParticipacion nivel : values()) {
            if (puntos >= nivel.minPuntos && puntos <= nivel.maxPuntos) {
                return nivel;
            }
        }
        return NOVATO;
    }

    public String getNombre() {
        return nombre;
    }

    public int getMinPuntos() {
        return minPuntos;
    }

    public int getMaxPuntos() {
        return maxPuntos;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
