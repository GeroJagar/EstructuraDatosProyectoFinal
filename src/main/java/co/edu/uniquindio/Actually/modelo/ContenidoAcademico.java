package co.edu.uniquindio.Actually.modelo;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContenidoAcademico implements Serializable {
    private String titulo;
    private TEMA tema;
    private String autor;
    private int puntuacion;
    private String id;
}