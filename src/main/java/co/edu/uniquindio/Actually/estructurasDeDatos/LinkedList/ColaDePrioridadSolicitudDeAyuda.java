package co.edu.uniquindio.Actually.estructurasDeDatos.LinkedList;

import co.edu.uniquindio.Actually.estructurasDeDatos.Nodo.Nodo;
import co.edu.uniquindio.Actually.modelo.SolicitudAyuda;

/*
Clase para hacer la cola de prioridad exclusiva para las solicitudes de ayuda.
 */
public class ColaDePrioridadSolicitudDeAyuda {

    private Nodo<SolicitudAyuda> head; //Se necesitan nodos de tipo solicitud de ayuda.

    public ColaDePrioridadSolicitudDeAyuda() {
        head = null;
    }
/*
Método enconlar a la cola de prioridad.
*/
    public void enqueue(SolicitudAyuda helpRequest) {
        Nodo<SolicitudAyuda> newHelpRequest = new Nodo<>(helpRequest);

        if (head == null || helpRequest.getUrgencia() < head.getValor().getUrgencia()) {//Primero se valida si la cabeza es null, lo que indica que la cola está vacía; en ese caso, el nuevo nodo se agrega como cabeza. Alternativamente, si la prioridad del nodo actual en la cabeza es mayor que la del nuevo nodo, este también se inserta al inicio de la cola.
            newHelpRequest.setSiguiente(head);
            head = newHelpRequest;
        }else{
            Nodo<SolicitudAyuda> prev = head;
            Nodo<SolicitudAyuda> pivot = head.getSiguiente();
            while (pivot != null) {
                if (helpRequest.getUrgencia() < pivot.getValor().getUrgencia()) { //Valido si la nueva solicitud de ayuda tiene un menor nivel que la que estamos comparando
                    prev.setSiguiente(newHelpRequest);                            //Si es menor, entonces el anterior al que comparamos, lo enlazamos con la solicitud.
                    newHelpRequest.setSiguiente(pivot);                           //Y la nueva solicitud la enlazamos con el que estabamos comparando.
                    return;
                }else{
                    prev = pivot;             //Sino simplemente avanzo en la cola y ya.
                    pivot = pivot.getSiguiente();
                }
            }
            prev.setSiguiente(newHelpRequest);        }
    }
/*
Método para desencolar de la cola (durísimo).
*/
    public SolicitudAyuda dequeue() {
        if(head == null){
            return null;
        }else{
            SolicitudAyuda helpRequest = head.getValor();
            head = head.getSiguiente();
            return helpRequest;
        }
    }
/*
Método para observar el elemento de la cabeza de la cola.
*/
    public SolicitudAyuda peek() {
        return head.getValor();
    }
}
