package es.sescam.servidor.error;

/**
 * Excepción común que indica que el usuario no tiene permisos para realizar la operación.
 * <p>
 * Se lanza cuando el usuario no está autenticado o no posee ninguno de los roles
 * autorizados para la operación solicitada (RNF-SEG-01).
 * </p>
 */
public class AccesoDenegado extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepción con el mensaje indicado.
     *
     * @param mensaje descripción del error de acceso
     */
    public AccesoDenegado(String mensaje) {
        super(mensaje);
    }
}
