package co.edu.uniquindio.Actually.modelo;

import java.io.Serializable;

public class Valoracion implements Serializable {

    private String estudianteId;
    private int puntaje; // de 1 a 5

    public Valoracion() {
    }

    public Valoracion(String estudianteId, int puntaje) {
        this.estudianteId = estudianteId;
        this.puntaje = puntaje;
    }

    public String getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(String estudianteId) {
        this.estudianteId = estudianteId;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    @Override
    public String toString() {
        return "Valoracion{" +
                "estudianteId='" + estudianteId + '\'' +
                ", puntaje=" + puntaje +
                '}';
    }
}
