package co.edu.uniquindio.Actually.excepciones;

/**
 * La clase CampoVacioException representa una excepción lanzada cuando se detecta
 * que un campo requerido está vacío o no contiene ningún valor.
 */
public class CampoVacioException extends Exception {

    /**
     * Crea una nueva instancia de CampoVacioException con el mensaje especificado.
     *
     * @param mensaje El mensaje que describe la causa de la excepción.
     */
    public CampoVacioException(String mensaje) {
        super(mensaje);
    }
}