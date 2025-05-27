package co.edu.uniquindio.Actually.modelo;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class GrupoEstudio implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String nombre;
    private String id;
    private TEMA tema;
    private List<Estudiante> participantes = new ArrayList<>();

    // Constructor sin argumentos
    public GrupoEstudio() {
    }

    // Constructor con todos los parámetros
    public GrupoEstudio(String nombre, TEMA tema, List<Estudiante> participantes) {
        this.nombre = nombre;
        this.id = UUID.randomUUID().toString();
        this.tema = tema;
        this.participantes = participantes;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void agregarParticipante(Estudiante estudiante) {
        if (estudiante != null && !participantes.contains(estudiante)) {
            participantes.add(estudiante);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrupoEstudio that = (GrupoEstudio) o;
        return Objects.equals(nombre, that.nombre) &&
                Objects.equals(tema, that.tema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, tema);
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
