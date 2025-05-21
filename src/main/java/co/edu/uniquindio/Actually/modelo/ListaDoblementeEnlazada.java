package co.edu.uniquindio.Actually.modelo;

public class ListaDoblementeEnlazada<T extends Comparable<T>> {

    private NodoDoble<T> cabeza;
    private NodoDoble<T> cola;
    private int tamaño;

    public ListaDoblementeEnlazada() {
        this.cabeza = null;
        this.cola = null;
        this.tamaño = 0;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int getTamaño() {
        return tamaño;
    }

    // Insertar al final
    public void insertarAlFinal(T dato) {
        NodoDoble<T> nuevo = new NodoDoble<>(dato);

        if (estaVacia()) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.setSiguiente(nuevo);
            nuevo.setAnterior(cola);
            cola = nuevo;
        }

        tamaño++;
    }

    // Insertar al inicio
    public void insertarAlInicio(T dato) {
        NodoDoble<T> nuevo = new NodoDoble<>(dato);

        if (estaVacia()) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            nuevo.setSiguiente(cabeza);
            cabeza.setAnterior(nuevo);
            cabeza = nuevo;
        }

        tamaño++;
    }

    // Eliminar un nodo con un dato específico
    public boolean eliminar(T dato) {
        NodoDoble<T> actual = cabeza;

        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                if (actual == cabeza) {
                    cabeza = actual.getSiguiente();
                    if (cabeza != null) {
                        cabeza.setAnterior(null);
                    } else {
                        cola = null;
                    }
                } else if (actual == cola) {
                    cola = actual.getAnterior();
                    if (cola != null) {
                        cola.setSiguiente(null);
                    }
                } else {
                    actual.getAnterior().setSiguiente(actual.getSiguiente());
                    actual.getSiguiente().setAnterior(actual.getAnterior());
                }

                tamaño--;
                return true;
            }

            actual = actual.getSiguiente();
        }

        return false;
    }

    // Buscar un nodo
    public NodoDoble<T> buscar(T dato) {
        NodoDoble<T> actual = cabeza;

        while (actual != null) {
            if (actual.getDato().equals(dato)) {
                return actual;
            }
            actual = actual.getSiguiente();
        }

        return null;
    }
}
