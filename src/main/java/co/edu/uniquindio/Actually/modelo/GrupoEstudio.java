package co.edu.uniquindio.Actually.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrupoEstudio implements Serializable {
    private String nombre;
    private TEMA tema;
    private List<Estudiante> participantes = new ArrayList<>();

    // Constructor sin argumentos
    public GrupoEstudio() {
    }

    // Constructor con todos los parámetros
    public GrupoEstudio(String nombre, TEMA tema, List<Estudiante> participantes) {
        this.nombre = nombre;
        this.tema = tema;
        this.participantes = participantes;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TEMA getTema() {
        return tema;
    }

    public void setTema(TEMA tema) {
        this.tema = tema;
    }

    public List<Estudiante> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<Estudiante> participantes) {
        this.participantes = participantes;
    }

    // Método para agregar participante
    public void agregarParticipante(Estudiante estudiante) {
        participantes.add(estudiante);
    }

    // Método toString
    @Override
    public String toString() {
        return "GrupoEstudio{" +
                "nombre='" + nombre + '\'' +
                ", tema=" + tema +
                ", participantes=" + participantes +
                '}';
    }
}
