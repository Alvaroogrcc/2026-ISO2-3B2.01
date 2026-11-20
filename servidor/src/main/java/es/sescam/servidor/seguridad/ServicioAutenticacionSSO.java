package es.sescam.servidor.seguridad;

import java.util.Optional;

/**
 * Contrato de integración con Single Sign-On corporativo (It. 0).
 */
public interface ServicioAutenticacionSSO {

    /**
     * Valida un token/assertion emitida por el proveedor SSO y devuelve el usuario autenticado.
     */
    Optional<Usuario> autenticar(String tokenSSO);

    /**
     * Cierra la sesión asociada al token indicado en el contexto de la aplicación.
     */
    void cerrarSesion(String tokenSSO);
}
