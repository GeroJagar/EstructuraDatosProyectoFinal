package co.edu.uniquindio.Actually.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Mensaje implements Serializable {
    /*
    Clase mensaje la cual se usa en la clase chat.
     */
    private Estudiante remitente;
    private Estudiante destinatario;
    private String contenido;
    private LocalDateTime timestamp; //Este atributo nos da la fecha y la hora por si depronto se agrega despues a los chats

    public Mensaje(Estudiante remitente, Estudiante destinatario, String contenido) {
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.contenido = contenido;
        this.timestamp = LocalDateTime.now();
    }

    public Estudiante getRemitente() {
        return remitente;
    }

    public void setRemitente(Estudiante remitente) {
        this.remitente = remitente;
    }

    public Estudiante getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Estudiante destinatario) {
        this.destinatario = destinatario;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

