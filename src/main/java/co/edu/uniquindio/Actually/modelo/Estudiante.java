package co.edu.uniquindio.Actually.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Estudiante extends Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    private final List<GrupoEstudio> gruposRechazados = new ArrayList<>();

    private List<GrupoEstudio> gruposEstudio;
    private List<Estudiante> amigos;
    private List<ContenidoAcademico> contenidosSubidos;

    private int puntosParticipacion;
    private NivelParticipacion nivel;

    public Estudiante() {
        this.gruposEstudio = new ArrayList<>();
        this.amigos = new ArrayList<>();
        this.contenidosSubidos = new ArrayList<>();
        this.puntosParticipacion = 0;
        this.nivel = NivelParticipacion.NOVATO;
    }

    // Métodos para gestionar participación
    public void agregarPuntos(int puntos) {
        this.puntosParticipacion += puntos;
        actualizarNivel();
    }

    private void actualizarNivel() {
        this.nivel = NivelParticipacion.determinarNivel(puntosParticipacion);
    }

    // Getters
    public int getPuntosParticipacion() {
        return puntosParticipacion;
    }

    public NivelParticipacion getNivel() {
        return nivel;
    }

    // Getters y Setters
    public List<GrupoEstudio> getGruposEstudio() {
        return gruposEstudio;
    }

    public void setGruposEstudio(List<GrupoEstudio> gruposEstudio) {
        this.gruposEstudio = (gruposEstudio != null) ? gruposEstudio : new ArrayList<>();
    }

    public List<Estudiante> getAmigos() {
        return amigos;
    }

    public void setAmigos(List<Estudiante> amigos) {
        this.amigos = (amigos != null) ? amigos : new ArrayList<>();
    }

    public List<ContenidoAcademico> getContenidosSubidos() {
        return contenidosSubidos;
    }

    public void setContenidosSubidos(List<ContenidoAcademico> contenidosSubidos) {
        this.contenidosSubidos = (contenidosSubidos != null) ? contenidosSubidos : new ArrayList<>();
    }

    // Métodos para agregar elementos
    public void agregarGrupoEstudio(GrupoEstudio grupo) {
        if (grupo != null && !gruposEstudio.contains(grupo)) {
            gruposEstudio.add(grupo);
        }
    }

    public void agregarGrupoRechazado(GrupoEstudio grupo) {
        if (!gruposRechazados.contains(grupo)) {
            gruposRechazados.add(grupo);
        }
    }

    public List<GrupoEstudio> getGruposRechazados() {
        return gruposRechazados;
    }

    public boolean haRechazadoGrupo(GrupoEstudio grupo) {
        return gruposRechazados.contains(grupo);
    }

    public void agregarAmigo(Estudiante amigo) {
        if (amigo != null && !this.amigos.contains(amigo)) {
            this.amigos.add(amigo);
            agregarPuntos(5);
        }
    }

    public void eliminarAmigo(Estudiante amigo) {
        this.amigos.remove(amigo);
    }

    public void subirContenido(ContenidoAcademico contenido) {
        if (contenido != null && !contenidosSubidos.contains(contenido)) {
            contenidosSubidos.add(contenido);
            agregarPuntos(10);
        }
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "nombre='" + getNombre() + '\'' +
                ", id='" + getId() + '\'' +
                ", correo='" + getCorreo() + '\'' +
                ", gruposEstudio=" + gruposEstudio.size() +
                ", amigos=" + amigos.size() +
                ", contenidosSubidos=" + contenidosSubidos.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Estudiante)) return false;
        Estudiante that = (Estudiante) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
