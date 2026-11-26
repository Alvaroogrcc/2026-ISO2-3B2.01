package es.sescam.servidor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.sescam.servidor.auditoria.AuditoriaStub;
import es.sescam.servidor.error.AccesoDenegado;
import es.sescam.servidor.error.EntidadNoEncontrada;
import es.sescam.servidor.error.ValidacionIncorrecta;
import es.sescam.servidor.repository.UsuarioRepositoryMemoria;
import es.sescam.servidor.seguridad.ControlAccesoBasico;
import es.sescam.servidor.seguridad.Permiso;
import es.sescam.servidor.seguridad.Rol;
import es.sescam.servidor.seguridad.Usuario;
import es.sescam.servidor.service.GestionUsuariosService;
import es.sescam.servidor.service.GestionUsuariosServiceImpl;

class GestionUsuariosServiceTest {

    private AuditoriaStub auditoria;
    private ControlAccesoBasico controlAcceso;
    private GestionUsuariosService service;
    private Usuario admin;

    @BeforeEach
    void setUp() {
        auditoria = new AuditoriaStub();
        controlAcceso = new ControlAccesoBasico(auditoria);
        service = new GestionUsuariosServiceImpl(new UsuarioRepositoryMemoria(), controlAcceso, auditoria);
        admin = new Usuario("admin-1", "admin", "Admin", EnumSet.of(Rol.ADMINISTRADOR_SISTEMA), true);
    }

    @Test
    void adminCreaUsuarioYAsignaRoles() {
        Usuario creado = service.crearUsuario(admin, "u1", "medico1", "Medico Uno", EnumSet.of(Rol.MEDICO));
        assertNotNull(creado);
        assertEquals("u1", creado.getId());
        assertTrue(creado.isActivo());
        assertTrue(creado.tieneRol(Rol.MEDICO));

        Usuario actualizado = service.asignarRoles(admin, "u1", EnumSet.of(Rol.MEDICO, Rol.ADMINISTRATIVO));
        assertEquals(Set.of(Rol.MEDICO, Rol.ADMINISTRATIVO), actualizado.getRoles());
        assertTrue(auditoria.getEventos().stream().anyMatch(e -> e.contains("USUARIO_CREADO")));
        assertTrue(auditoria.getEventos().stream().anyMatch(e -> e.contains("USUARIO_ROLES_ASIGNADOS")));
    }

    @Test
    void desactivarUsuarioConservaHistorial() {
        service.crearUsuario(admin, "u2", "user2", "User Dos", EnumSet.of(Rol.ADMINISTRATIVO));
        service.desactivarUsuario(admin, "u2");
        Usuario obtenido = service.obtenerUsuario(admin, "u2");
        assertFalse(obtenido.isActivo());
        assertEquals(1, service.listarUsuarios(admin).size());
        assertTrue(auditoria.getEventos().stream().anyMatch(e -> e.contains("USUARIO_DESACTIVADO")));
    }

    @Test
    void noAdminNoPuedeGestionarUsuarios() {
        Usuario medico = new Usuario("m1", "med", "Med", EnumSet.of(Rol.MEDICO), true);
        assertThrows(AccesoDenegado.class,
                () -> service.crearUsuario(medico, "u3", "u3", "U3", EnumSet.of(Rol.PACIENTE)));
        assertThrows(AccesoDenegado.class, () -> service.listarUsuarios(medico));
        assertTrue(auditoria.getEventos().stream().anyMatch(e -> e.startsWith("ACCESO|") && e.contains("ACCESO_DENEGADO")));
    }

    @Test
    void validacionesYNoEncontrado() {
        assertThrows(ValidacionIncorrecta.class,
                () -> service.crearUsuario(admin, " ", "u4", "U4", EnumSet.of(Rol.MEDICO)));
        assertThrows(ValidacionIncorrecta.class,
                () -> service.crearUsuario(admin, "u4", " ", "U4", EnumSet.of(Rol.MEDICO)));
        assertThrows(ValidacionIncorrecta.class,
                () -> service.crearUsuario(admin, "u4", "u4", "U4", java.util.Collections.emptySet()));

        service.crearUsuario(admin, "u4", "u4", "U4", EnumSet.of(Rol.MEDICO));
        assertThrows(ValidacionIncorrecta.class,
                () -> service.crearUsuario(admin, "u4", "otro", "Otro", EnumSet.of(Rol.MEDICO)));
        assertThrows(ValidacionIncorrecta.class,
                () -> service.crearUsuario(admin, "u5", "u4", "Otro", EnumSet.of(Rol.MEDICO)));

        assertThrows(EntidadNoEncontrada.class, () -> service.asignarRoles(admin, "noexiste", EnumSet.of(Rol.MEDICO)));
        assertThrows(EntidadNoEncontrada.class, () -> service.desactivarUsuario(admin, "noexiste"));
        assertThrows(EntidadNoEncontrada.class, () -> service.obtenerUsuario(admin, "noexiste"));
    }

    @Test
    void definirPermisosRolValidaYAudita() {
        controlAcceso.definirPermisosRol(admin, Rol.MEDICO, EnumSet.of(Permiso.CONSULTAR_CITAS));
        Set<Permiso> permisos = controlAcceso.permisosDeRol(Rol.MEDICO);
        assertEquals(Set.of(Permiso.CONSULTAR_CITAS), permisos);
        assertTrue(auditoria.getEventos().stream().anyMatch(e -> e.contains("CAMBIO_PERMISOS_ROL")));

        assertThrows(ValidacionIncorrecta.class, () -> controlAcceso.permisosDeRol(null));
        assertThrows(ValidacionIncorrecta.class,
                () -> controlAcceso.definirPermisosRol(admin, null, EnumSet.of(Permiso.CONSULTAR_CITAS)));

        Usuario medico = new Usuario("m1", "med", "Med", EnumSet.of(Rol.MEDICO), true);
        assertThrows(AccesoDenegado.class,
                () -> controlAcceso.definirPermisosRol(medico, Rol.PACIENTE, EnumSet.of(Permiso.CONSULTAR_CITAS)));
    }
}
