package co.edu.uniquindio.Actually.modelo;

import java.io.Serializable;

public class ABBContenido implements Serializable {

    private NodoContenido raiz;

    public ABBContenido(NodoContenido raiz) {
        this.raiz = raiz;
    }

    public ABBContenido() {
    }

    public NodoContenido getRaiz() {
        return raiz;
    }

    public void setRaiz(NodoContenido raiz) {
        this.raiz = raiz;
    }

    public void insertar(ContenidoAcademico contenido, String clave) {
        raiz = insertarRecursivo(raiz, contenido, clave);
    }

    private NodoContenido insertarRecursivo(NodoContenido nodo, ContenidoAcademico contenido, String clave) {
        if (nodo == null) return new NodoContenido(contenido, clave);
        int comparacion = clave.compareToIgnoreCase(nodo.getClave());
        if (comparacion < 0) {
            nodo.setIzquierdo(insertarRecursivo(nodo.getIzquierdo(), contenido, clave));
        } else if (comparacion > 0) {
            nodo.setDerecho(insertarRecursivo(nodo.getDerecho(), contenido, clave));
        }
        return nodo;
    }

    public ContenidoAcademico buscar(String clave) {
        return buscarRecursivo(raiz, clave);
    }

    private ContenidoAcademico buscarRecursivo(NodoContenido nodo, String clave) {
        if (nodo == null) {
            System.out.println("Nodo nulo. No se encontró la clave: " + clave);
            return null;
        }

        System.out.println("Visitando nodo con clave: " + nodo.getClave());

        int comparacion = clave.compareToIgnoreCase(nodo.getClave());

        if (comparacion == 0) {
            System.out.println("¡Clave encontrada!: " + nodo.getClave());
            return nodo.getContenido();
        }

        if (comparacion < 0) {
            System.out.println("Clave buscada es menor. Ir a la izquierda.");
            return buscarRecursivo(nodo.getIzquierdo(), clave);
        } else {
            System.out.println("Clave buscada es mayor. Ir a la derecha.");
            return buscarRecursivo(nodo.getDerecho(), clave);
        }
    }


    @Override
    public String toString() {
        return "ABBContenido{" +
                "raiz=" + raiz +
                '}';
    }
}
