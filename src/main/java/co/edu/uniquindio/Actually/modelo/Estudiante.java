package co.edu.uniquindio.Actually.modelo;

import java.util.ArrayList;
import java.util.List;

public class Estudiante extends Usuario {
    private List<GrupoEstudio> gruposEstudio = new ArrayList<>();
    private List<Estudiante> amigos = new ArrayList<>();

    // Constructor sin argumentos
    public Estudiante() {
    }

    // Constructor con todos los parámetros
    public Estudiante(List<GrupoEstudio> gruposEstudio, List<Estudiante> amigos) {
        this.gruposEstudio = gruposEstudio;
        this.amigos = amigos;
    }

    // Getters y setters
    public List<GrupoEstudio> getGruposEstudio() {
        return gruposEstudio;
    }

    public void setGruposEstudio(List<GrupoEstudio> gruposEstudio) {
        this.gruposEstudio = gruposEstudio;
    }

    public List<Estudiante> getAmigos() {
        return amigos;
    }

    public void setAmigos(List<Estudiante> amigos) {
        this.amigos = amigos;
    }

    // Métodos para agregar grupo de estudio y amigo
    public void agregarGrupoEstudio(GrupoEstudio grupo) {
        gruposEstudio.add(grupo);
    }

    public void agregarAmigo(Estudiante estudiante) {
        amigos.add(estudiante);
    }

    // Método toString
    @Override
    public String toString() {
        return "Estudiante{" +
                "gruposEstudio=" + gruposEstudio +
                ", amigos=" + amigos +
                ", nombre='" + super.getNombre() + '\'' +
                ", contrasena='" + super.getContrasena() + '\'' +
                ", id='" + super.getId() + '\'' +
                ", correo='" + super.getCorreo() + '\'' +
                '}';
    }
}
