package co.edu.uniquindio.Actually.modelo;

import java.io.Serializable;

public class ContenidoAcademico implements Serializable {
    private String titulo;
    private TEMA tema;
    private String autor;
    private int puntuacion;
    private String id;

    // Constructor sin argumentos
    public ContenidoAcademico() {
    }

    // Constructor con todos los parámetros
    public ContenidoAcademico(String titulo, TEMA tema, String autor, int puntuacion, String id) {
        this.titulo = titulo;
        this.tema = tema;
        this.autor = autor;
        this.puntuacion = puntuacion;
        this.id = id;
    }

    // Getters y setters
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

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Método toString
    @Override
    public String toString() {
        return "ContenidoAcademico{" +
                "titulo='" + titulo + '\'' +
                ", tema=" + tema +
                ", autor='" + autor + '\'' +
                ", puntuacion=" + puntuacion +
                ", id='" + id + '\'' +
                '}';
    }
}
