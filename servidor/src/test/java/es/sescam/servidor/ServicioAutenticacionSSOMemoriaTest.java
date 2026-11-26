package es.sescam.servidor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;

import org.junit.jupiter.api.Test;

import es.sescam.servidor.seguridad.Rol;
import es.sescam.servidor.seguridad.ServicioAutenticacionSSOMemoria;
import es.sescam.servidor.seguridad.Usuario;

class ServicioAutenticacionSSOMemoriaTest {

    @Test
    void autenticaSesionValidaYRechazaInvalidas() {
        ServicioAutenticacionSSOMemoria sso = new ServicioAutenticacionSSOMemoria();
        Usuario activo = new Usuario("u1", "u1", "U1", EnumSet.of(Rol.MEDICO), true);
        Usuario inactivo = new Usuario("u2", "u2", "U2", EnumSet.of(Rol.MEDICO), false);

        sso.registrarSesion("tok-valido", activo);
        sso.registrarSesion("tok-inactivo", inactivo);

        assertTrue(sso.autenticar("tok-valido").isPresent());
        assertFalse(sso.autenticar("tok-inactivo").isPresent());
        assertFalse(sso.autenticar("tok-inexistente").isPresent());
        assertFalse(sso.autenticar(" ").isPresent());
        assertFalse(sso.autenticar(null).isPresent());

        sso.revocarSesion("tok-valido");
        assertFalse(sso.autenticar("tok-valido").isPresent());

        sso.registrarSesion("tok-cerrar", activo);
        sso.cerrarSesion("tok-cerrar");
        assertFalse(sso.autenticar("tok-cerrar").isPresent());
    }
}
