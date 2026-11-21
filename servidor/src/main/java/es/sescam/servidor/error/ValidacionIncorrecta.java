package es.sescam.servidor.error;

/**
 * Excepción común que indica que los datos recibidos no cumplen las reglas de negocio.
 * <p>
 * Los mensajes deben ser claros para el usuario y no incluir datos clínicos.
 * </p>
 */
public class ValidacionIncorrecta extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepción con el mensaje indicado.
     *
     * @param mensaje descripción del error de validación, sin datos clínicos
     */
    public ValidacionIncorrecta(String mensaje) {
        super(mensaje);
    }
}
