package org.proyectofinal.backend.dataStructures.DoubleEndedLinkedList;

import org.proyectofinal.backend.dataStructures.Node.NodeDouble;
/*
Clase para crear una lista doblemente enlazada generica, que añade a la cola de la lista.
Los nodos están conectados con su siguiente y su anterior.
 */
public class DoubleEndedLinkedList<T> {
    private NodeDouble<T> head;
    private NodeDouble<T> tail;

    public DoubleEndedLinkedList() {
        head = null;
        tail = null;
    }
/*
Método para añadir un nodo a la lista por valor.
@param value. El valor a agregar a la lista.
 */
    public void add(T value) {
        NodeDouble<T> newNode = new NodeDouble(value); //Se crea el nuevo nodo.
        if (head == null) { //Si la lista esta vacía, la cabeza y la cola serán el nodo a agregar.
            head = newNode;
            tail = newNode;
        }else{                         //Sino, hay uno o más elementos en la lista.
            tail.setNext(newNode);     //Se setea el siguiente de la cola con el nuevo nodo.
            newNode.setPrevious(tail); //El nuevo nodo se setea el anterior con la cola.
            tail = newNode;            //Y ahora la cola pasa a ser el nuevo nodo.
        }
    }
/*
Metodo para remover un elemento de la lista por valor.
@param value. El valor a eliminar a la lista.
 */
    public void removeByValue(T value) {
        if (head != null) {
            NodeDouble<T> current = head;
            if (head != tail){                                     //Me aseguro que la lista tenga al menos dos cosas.
                while (current != null) {
                    if (current.getValue().equals(value)) {
                        if (current == head) {                        //Caso 1) Si el nodo que hay que eliminar está en la cabeza.

                            head = (NodeDouble<T>) current.getNext(); //Cabeza pasa a ser el siguiente nodo de la lista.
                            head.setPrevious(null);                   //El nodo anterior a la nueva cabeza se hace un set null para que el anterior se desenlance.

                        }else if (current != tail) {                                          //Caso 2) el nodo no está en la cola, o sea que está en mitad de la lista.

                            NodeDouble<T> next = (NodeDouble<T>) current.getNext();           //Creo un nodo siguiente al que tengo que eliminar.
                            NodeDouble<T> previous = (NodeDouble<T>) current.getPrevious();   //Creo un nodo anterior al que tengo eliminar.
                            next.setPrevious(previous);                                       //El nodo siguiente le seteo el anterior.
                            previous.setNext(next);                                           //El nodo anterior le seteo el siguiente.

                        }else{                                            //Caso 3) Si el nodo a borrar es la cola.

                            tail = (NodeDouble<T>) current.getPrevious(); //A cola lo vuelvo el anterior a la cola.
                            tail.setNext(null);                           //el siguiente a la cola lo vuelvo nulo

                        }
                    }

                    current = (NodeDouble<T>) current.getNext(); //Para pasar al siguiente nodo.

                }

            }else{                                        //Si la cabeza es igual a la cola significa que solo hay una cosa en la lista.
                if(head.getValue().equals(value)){        //Si el único objeto que hay en la lista es el que tengo que tengo que borrar.
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
