package co.edu.uniquindio.Actually.modelo;

import java.io.Serializable;

public class SolicitudAyuda implements Serializable {
    private TEMA tema;
    private int urgencia;
    private Usuario solicitante;

    // Constructor sin argumentos
    public SolicitudAyuda() {
    }

    // Constructor con todos los parámetros
    public SolicitudAyuda(TEMA tema, int urgencia, Usuario solicitante) {
        this.tema = tema;
        this.urgencia = urgencia;
        this.solicitante = solicitante;
    }

    // Getters y setters
    public TEMA getTema() {
        return tema;
    }

    public void setTema(TEMA tema) {
        this.tema = tema;
    }

    public int getUrgencia() {
        return urgencia;
    }

    public void setUrgencia(int urgencia) {
        this.urgencia = urgencia;
    }

    public Usuario getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Usuario solicitante) {
        this.solicitante = solicitante;
    }

    // Método toString
    @Override
    public String toString() {
        return "SolicitudAyuda{" +
                "tema=" + tema +
                ", urgencia=" + urgencia +
                ", solicitante=" + solicitante +
                '}';
    }
}
