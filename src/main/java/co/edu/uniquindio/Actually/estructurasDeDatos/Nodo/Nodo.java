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
Clase basica para hacer nodos enlazados.
 */
public class Nodo <T> {

    protected T valor;
    protected Nodo<T> siguiente;

    public Nodo(T value) {
        this.valor = value;
        siguiente = null;
    }
}
