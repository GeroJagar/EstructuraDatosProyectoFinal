package co.edu.uniquindio.Actually.modelo;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public abstract class Usuario {
    protected String nombre;
    protected String contraseña;
    protected String id;
    protected String correo;

}