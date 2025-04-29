package org.proyectofinal.backend.dataStructures.Node;
    /*
  Esta clase se usara para crear las listas doblemente enlazadas
   */
public class NodeDouble<T> extends Node<T> {
    Node<T> previous;
    public NodeDouble(T value) {
        super(value);
        previous = null;
    }

    public Node<T> getPrevious() {
        return previous;
    }

    public void setPrevious(Node<T> previous) {
        this.previous = previous;
    }


}
