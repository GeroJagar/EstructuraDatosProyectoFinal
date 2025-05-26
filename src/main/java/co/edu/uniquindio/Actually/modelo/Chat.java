package co.edu.uniquindio.Actually.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Chat implements Serializable {

    /*
    Esta clase es la encarga de crear chats entre estudiantes, usa una lista de mensajes los cuales se envian los estudiantes
     */

    private static Chat instance;
    private Estudiante estudiante1;
    private Estudiante estudiante2;
    private List<Mensaje> mensajes;

    public Chat(Estudiante estudiante1, Estudiante estudiante2) {
        this.estudiante1 = estudiante1;
        this.estudiante2 = estudiante2;
        this.mensajes = new ArrayList<>();
    }
    /*
    Aqui crea un mensaje dependiendo de cual es el remitente del mensaje
     */
    public void enviarMensaje(Estudiante remitente, String contenido) {
        Estudiante destinatario = remitente.equals(estudiante1) ? estudiante2 : estudiante1;
        Mensaje mensaje = new Mensaje(remitente, destinatario, contenido);
        mensajes.add(mensaje);
    }

    public List<Mensaje> getMensajes() {
        return mensajes;
    }

}
