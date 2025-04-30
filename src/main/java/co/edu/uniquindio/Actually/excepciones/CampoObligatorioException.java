package co.edu.uniquindio.Actually.excepciones;

/**
 * La clase CampoObligatorioException representa una excepción lanzada cuando un campo obligatorio
 * no se proporciona o está vacío en un contexto donde se requiere un valor.
 */
public class CampoObligatorioException extends Exception {

    /**
     * Constructor que crea una nueva instancia de CampoObligatorioException con el mensaje especificado.
     *
     * @param mensaje el mensaje que describe la causa de la excepción.
     */
    public CampoObligatorioException(String mensaje) {
        super(mensaje);
    }
}