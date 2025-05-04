package co.edu.uniquindio.Actually.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContenidoAcademico implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String titulo;
    protected TEMA tema;
    protected String autor;
    protected String contenido;
    protected String id;
    protected List<Valoracion> valoraciones;

    public ContenidoAcademico() {
        this.valoraciones = new ArrayList<>();
    }

    public ContenidoAcademico(String titulo, TEMA tema, String autor, String contenido, String id) {
        this();
        this.titulo = titulo;
        this.tema = tema;
        this.autor = autor;
        this.contenido = contenido;
        this.id = id;
    }

    public void agregarValoracion(Valoracion valoracion) {
        if (valoracion != null) {
            valoraciones.add(valoracion);
        }
    }

    public double calcularPuntuacion() {
        if (valoraciones.isEmpty()) {
            return 0;
        }
        return valoraciones.stream()
                .mapToInt(Valoracion::getPuntaje)
                .average()
                .orElse(0);
    }

    // Getters y Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public TEMA getTema() {
        return tema;
    }

    public void setTema(TEMA tema) {
        this.tema = tema;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Valoracion> getValoraciones() {
        return valoraciones;
    }

    @Override
    public String toString() {
        return "ContenidoAcademico{" +
                "titulo='" + titulo + '\'' +
                ", tema=" + tema +
                ", autor='" + autor + '\'' +
                ", id='" + id + '\'' +
                ", puntuacion=" + String.format("%.2f", calcularPuntuacion()) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContenidoAcademico)) return false;
        ContenidoAcademico that = (ContenidoAcademico) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
