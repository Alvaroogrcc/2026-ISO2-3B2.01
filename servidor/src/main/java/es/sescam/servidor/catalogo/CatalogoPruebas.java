package es.sescam.servidor.catalogo;

import java.util.List;
import java.util.Optional;

import es.sescam.servidor.model.TipoPrueba;

/**
 * Contrato de acceso al catálogo de pruebas diagnósticas (CU-06).
 * <p>
 * Proporciona los atributos de cada prueba que deben aplicarse al prescribir:
 * preparación previa, uso de contraste y exigencia de consentimiento informado,
 * junto con la versión del catálogo utilizada (RNF-FUN-03).
 * </p>
 * <p>
 * La implementación definitiva corresponde al CU-17. En esta iteración se
 * utiliza un stub local con un catálogo reducido de demostración.
 * </p>
 */
public interface CatalogoPruebas {

    /**
     * Busca un tipo de prueba por su identificador.
     *
     * @param id identificador del tipo de prueba
     * @return tipo de prueba encontrado o {@link Optional#empty()} si no existe
     */
    Optional<TipoPrueba> obtenerPorId(Long id);

    /**
     * Lista todos los tipos de prueba disponibles en el catálogo.
     *
     * @return lista de tipos de prueba
     */
    List<TipoPrueba> listar();

    /**
     * Devuelve la versión del catálogo aplicada.
     *
     * @return versión del catálogo
     */
    String getVersion();
}
