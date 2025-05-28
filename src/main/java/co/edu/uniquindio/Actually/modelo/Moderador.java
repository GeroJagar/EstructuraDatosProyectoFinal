package co.edu.uniquindio.Actually.modelo;

import java.util.ArrayList;
import java.util.List;

public class Moderador extends Usuario {

    private List<ContenidoAcademico> contenidosSubidos;

    public Moderador() {
        this.contenidosSubidos = new ArrayList<>();
    }

    public void subirContenido(ContenidoAcademico contenido) {
        if (contenido != null && !contenidosSubidos.contains(contenido)) {
            contenidosSubidos.add(contenido);
        }
    }
}
