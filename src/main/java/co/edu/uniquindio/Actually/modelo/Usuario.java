package co.edu.uniquindio.Actually.modelo;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class Usuario implements Serializable {
    protected String nombre;
    protected String contrasena;
    protected String id;
    protected String correo;
}