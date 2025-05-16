package co.edu.uniquindio.Actually.modelo;

import java.io.Serializable;

public class SolicitudAyuda implements Serializable, Comparable<SolicitudAyuda> {
    private TEMA tema;
    private int urgencia; // 1 (más urgente) a 5 (menos urgente)
    private Usuario solicitante;
    private String id;
    private String descripcion;
    private EstadoSolicitud estado;
    private String idContenidoResuelto; // Contenido que resuelve la solicitud

    public enum EstadoSolicitud {
        PENDIENTE, EN_PROCESO, RESUELTA
    }

    public SolicitudAyuda() {
        this.estado = EstadoSolicitud.PENDIENTE;
    }

    public SolicitudAyuda(TEMA tema, int urgencia, Usuario solicitante, String descripcion) {
        this();
        this.tema = tema;
        this.urgencia = urgencia;
        this.solicitante = solicitante;
        this.descripcion = descripcion;
    }

    // Getters y setters existentes...


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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public String getIdContenidoResuelto() {
        return idContenidoResuelto;
    }

    public void setIdContenidoResuelto(String idContenidoResuelto) {
        this.idContenidoResuelto = idContenidoResuelto;
    }

    @Override
    public int compareTo(SolicitudAyuda otra) {
        // Ordenamos de menor a mayor urgencia (1 es más urgente que 5)
        return Integer.compare(this.urgencia, otra.urgencia);
    }

    @Override
    public String toString() {
        return "SolicitudAyuda{" +
                "tema=" + tema +
                ", urgencia=" + urgencia +
                ", solicitante=" + solicitante.getNombre() +
                ", estado=" + estado +
                (idContenidoResuelto != null ? ", contenidoResuelto=" + idContenidoResuelto : "") +
                '}';
    }
}