package co.edu.uniquindio.Actually.modelo;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Estudiante extends Usuario {
    private List<GrupoEstudio> gruposEstudio = new ArrayList<>();
    private List<Estudiante> amigos = new ArrayList<>();

    public void agregarGrupoEstudio(GrupoEstudio grupo) {
        gruposEstudio.add(grupo);
    }

    public void agregarAmigo(Estudiante estudiante) {
        amigos.add(estudiante);
    }
}
