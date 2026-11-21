package es.sescam.servidor.error;

/**
 * Excepción común que indica que la entidad solicitada no existe.
 * <p>
 * Se utiliza en los servicios cuando una operación requiere una entidad concreta
 * y esta no puede localizarse. No debe revelar datos clínicos en el mensaje.
 * </p>
 */
public class EntidadNoEncontrada extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepción con el mensaje indicado.
     *
     * @param mensaje descripción del error, sin datos clínicos
     */
    public EntidadNoEncontrada(String mensaje) {
        super(mensaje);
    }
}
