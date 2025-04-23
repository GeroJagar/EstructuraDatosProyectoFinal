package co.edu.uniquindio.Actually.modelo;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrupoEstudio implements Serializable {
    private String nombre;
    private TEMA tema;
    private List<Estudiante> participantes = new ArrayList<>();

    public void agregarParticipante(Estudiante estudiante) {
        participantes.add(estudiante);
    }
}