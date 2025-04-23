package co.edu.uniquindio.Actually.modelo;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudAyuda implements Serializable {
    private TEMA tema;
    private int urgencia;
    private Usuario solicitante;
}