package es.sescam.servidor.seguridad;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementación en memoria de SSO para pruebas/desarrollo (CU-18).
 * Simula la validación de tokens corporativos sin almacenar credenciales.
 */
public class ServicioAutenticacionSSOMemoria implements ServicioAutenticacionSSO {

    private final Map<String, Usuario> sesiones = new ConcurrentHashMap<>();

    public void registrarSesion(String tokenSSO, Usuario usuario) {
        Objects.requireNonNull(tokenSSO, "tokenSSO");
        Objects.requireNonNull(usuario, "usuario");
        sesiones.put(tokenSSO, usuario);
    }

    public void revocarSesion(String tokenSSO) {
        if (tokenSSO != null) {
            sesiones.remove(tokenSSO);
        }
    }

    @Override
    public Optional<Usuario> autenticar(String tokenSSO) {
        if (tokenSSO == null || tokenSSO.isBlank()) {
            return Optional.empty();
        }
        Usuario usuario = sesiones.get(tokenSSO);
        if (usuario == null || !usuario.isActivo()) {
            return Optional.empty();
        }
        return Optional.of(usuario);
    }

    @Override
    public void cerrarSesion(String tokenSSO) {
        revocarSesion(tokenSSO);
    }
}
