package co.edu.uniquindio.Actually.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ColaPrioridad<T extends Comparable<T>> implements Serializable {
    private Nodo<T> primero;
    private int tamaño;

    public ColaPrioridad() {
        this.primero = null;
        this.tamaño = 0;
    }

    /**
     * Inserta un elemento en la cola según su prioridad
     * @param elemento Elemento a insertar
     */
    public void encolar(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);

        // Caso 1: La cola está vacía
        if (primero == null) {
            primero = nuevoNodo;
        }
        // Caso 2: El nuevo elemento tiene mayor prioridad que el primero
        else if (elemento.compareTo(primero.getDato()) < 0) {
            nuevoNodo.setSiguiente(primero);
            primero = nuevoNodo;
        }
        // Caso 3: Buscar la posición correcta para insertar
        else {
            Nodo<T> actual = primero;
            while (actual.getSiguiente() != null &&
                    elemento.compareTo(actual.getSiguiente().getDato()) >= 0) {
                actual = actual.getSiguiente();
            }
            nuevoNodo.setSiguiente(actual.getSiguiente());
            actual.setSiguiente(nuevoNodo);
        }
        tamaño++;
    }

    /**
     * Elimina y retorna el elemento con mayor prioridad
     * @return Elemento con mayor prioridad
     * @throws Exception Si la cola está vacía
     */
    public T desencolar() throws Exception {
        if (estaVacia()) {
            throw new Exception("La cola de prioridad está vacía");
        }
        T dato = primero.getDato();
        primero = primero.getSiguiente();
        tamaño--;
        return dato;
    }

    /**
     * Retorna el elemento con mayor prioridad sin eliminarlo
     * @return Elemento con mayor prioridad
     * @throws Exception Si la cola está vacía
     */
    public T frente() throws Exception {
        if (estaVacia()) {
            throw new Exception("La cola de prioridad está vacía");
        }
        return primero.getDato();
    }

    /**
     * Verifica si la cola está vacía
     * @return true si está vacía, false en caso contrario
     */
    public boolean estaVacia() {
        return primero == null;
    }

    /**
     * Retorna el tamaño de la cola
     * @return Tamaño de la cola
     */
    public int getTamaño() {
        return tamaño;
    }

    /**
     * Retorna una lista con todos los elementos de la cola
     * @return Lista con los elementos
     */
    public List<T> obtenerTodosElementos() {
        List<T> elementos = new ArrayList<>();
        Nodo<T> actual = primero;
        while (actual != null) {
            elementos.add(actual.getDato());
            actual = actual.getSiguiente();
        }
        return elementos;
    }

    /**
     * Elimina un elemento específico de la cola
     * @param elemento Elemento a eliminar
     * @return true si se eliminó, false si no se encontró
     */
    public boolean eliminar(T elemento) {
        if (primero == null) return false;

        // Caso especial: eliminar el primer elemento
        if (primero.getDato().equals(elemento)) {
            primero = primero.getSiguiente();
            tamaño--;
            return true;
        }

        Nodo<T> actual = primero;
        while (actual.getSiguiente() != null && !actual.getSiguiente().getDato().equals(elemento)) {
            actual = actual.getSiguiente();
        }

        if (actual.getSiguiente() != null) {
            actual.setSiguiente(actual.getSiguiente().getSiguiente());
            tamaño--;
            return true;
        }

        return false;
    }
}