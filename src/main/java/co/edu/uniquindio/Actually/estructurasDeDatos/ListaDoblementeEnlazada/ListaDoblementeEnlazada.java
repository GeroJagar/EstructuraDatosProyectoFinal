package co.edu.uniquindio.Actually.estructurasDeDatos.ListaDoblementeEnlazada;

import co.edu.uniquindio.Actually.estructurasDeDatos.Nodo.NodoDoble;

/*
Clase para crear una lista doblemente enlazada generica, que añade a la cola de la lista.
Los nodos están conectados con su siguiente y su anterior.
 */
public class ListaDoblementeEnlazada<T> {
    private NodoDoble<T> head;
    private NodoDoble<T> tail;

    public ListaDoblementeEnlazada() {
        head = null;
        tail = null;
    }
    /*
    Método para añadir un nodo a la lista por valor.
    @param value. El valor a agregar a la lista.
     */
    public void añadir(T value) {
        NodoDoble<T> newNode = new NodoDoble(value); //Se crea el nuevo nodo.

        if (head == null) {                 //Si la lista esta vacía, la cabeza y la cola serán el nodo a agregar.

            head = newNode;
            tail = newNode;

        }else{                              //Sino, hay uno o más elementos en la lista.

            tail.setSiguiente(newNode);     //Se setea el siguiente de la cola con el nuevo nodo.
            newNode.setAnterior(tail);      //El nuevo nodo se setea el anterior con la cola.
            tail = newNode;                 //Y ahora la cola pasa a ser el nuevo nodo.

        }
    }
    /*
    Metodo para remover un elemento de la lista por valor.
    @param value. El valor a eliminar a la lista.
     */
    public void quitarPorValor(T value) {
        if (head != null) {
            NodoDoble<T> current = head;
            if (head != tail){                                     //Me aseguro que la lista tenga al menos dos cosas.
                while (current != null) {
                    if (current.getValor().equals(value)) {
                        if (current == head) {                        //Caso 1) Si el nodo que hay que eliminar está en la cabeza.

                            head = (NodoDoble<T>) current.getSiguiente(); //Cabeza pasa a ser el siguiente nodo de la lista.
                            head.setAnterior(null);                   //El nodo anterior a la nueva cabeza se hace un set null para que el anterior se desenlance.

                        }else if (current != tail) {                                          //Caso 2) el nodo no está en la cola, o sea que está en mitad de la lista.

                            NodoDoble<T> next = (NodoDoble<T>) current.getSiguiente();           //Creo un nodo siguiente al que tengo que eliminar.
                            NodoDoble<T> previous = (NodoDoble<T>) current.getAnterior();   //Creo un nodo anterior al que tengo eliminar.
                            next.setAnterior(previous);                                       //El nodo siguiente le seteo el anterior.
                            previous.setSiguiente(next);                                           //El nodo anterior le seteo el siguiente.

                        }else{                                            //Caso 3) Si el nodo a borrar es la cola.

                            tail = (NodoDoble<T>) current.getAnterior(); //A cola lo vuelvo el anterior a la cola.
                            tail.setSiguiente(null);                           //el siguiente a la cola lo vuelvo nulo

                        }
                    }

                    current = (NodoDoble<T>) current.getSiguiente(); //Para pasar al siguiente nodo.

                }

            }else{                                        //Si la cabeza es igual a la cola significa que solo hay una cosa en la lista.
                if(head.getValor().equals(value)){        //Si el único objeto que hay en la lista es el que tengo que tengo que borrar.
                    head = null;                          //La cabeza se vuelve null y la cola también.
                    tail = null;
                }
            }
        }else{
/*
Dado el caso tocaría hacer un exception donde la lista esté vacía.
Dado el caso tocaría hacer un exception donde la lista esté vacía.
Dado el caso tocaría hacer un exception donde la lista esté vacía.
Dado el caso tocaría hacer un exception donde la lista esté vacía.
Dado el caso tocaría hacer un exception donde la lista esté vacía.
 */
        }
    }
}
