package co.edu.uniquindio.Actually.modelo;

import java.io.Serializable;

public class NodoContenido implements Serializable {

    private String clave;
    private ContenidoAcademico contenido;
    private NodoContenido izquierdo;
    private NodoContenido derecho;

    public NodoContenido(ContenidoAcademico contenido, String clave) {
        this.contenido = contenido;
        this.clave = clave;
        this.izquierdo = null;
        this.derecho = null;
    }

    public ContenidoAcademico getContenido() {
        return contenido;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setContenido(ContenidoAcademico contenido) {
        this.contenido = contenido;
    }

    public NodoContenido getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(NodoContenido izquierdo) {
        this.izquierdo = izquierdo;
    }

    public NodoContenido getDerecho() {
        return derecho;
    }

    public void setDerecho(NodoContenido derecho) {
        this.derecho = derecho;
    }

    @Override
    public String toString() {
        return "NodoContenido{" +
                "contenido=" + contenido +
                ", izquierdo=" + izquierdo +
                ", derecho=" + derecho +
                '}';
    }
}
