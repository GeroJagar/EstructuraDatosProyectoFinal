package co.edu.uniquindio.Actually.estructurasDeDatos.Nodo;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

/*
Esta clase se usará para crear las listas doblemente enlazadas
*/

public class NodoDoble<T> extends Nodo<T> {
    private Nodo<T> anterior;
    public NodoDoble(T value) {
        super(value);
        anterior = null;
    }


}
