package co.edu.uniquindio.Actually.modelo;

public class NodoDoble<T extends Comparable<T>> extends Nodo<T> {

    private NodoDoble<T> anterior;

    public NodoDoble(T dato) {
        super(dato);
        this.anterior = null;
    }

    public NodoDoble<T> getAnterior() {
        return anterior;
    }

    public void setAnterior(NodoDoble<T> anterior) {
        this.anterior = anterior;
    }

    @Override
    public NodoDoble<T> getSiguiente() {
        return (NodoDoble<T>) super.getSiguiente();
    }

    @Override
    public void setSiguiente(Nodo<T> siguiente) {
        if (siguiente != null && !(siguiente instanceof NodoDoble)) {
            throw new IllegalArgumentException("Debe ser una instancia de NodoDoble");
        }
        super.setSiguiente(siguiente);
    }
}

