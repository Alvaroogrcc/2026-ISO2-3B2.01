package es.sescam.servidor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.sescam.servidor.auditoria.AuditoriaStub;
import es.sescam.servidor.controller.GestionUsuariosController;
import es.sescam.servidor.controller.GestionUsuariosControllerImpl;
import es.sescam.servidor.error.AccesoDenegado;
import es.sescam.servidor.repository.UsuarioRepositoryMemoria;
import es.sescam.servidor.seguridad.ControlAccesoBasico;
import es.sescam.servidor.seguridad.Permiso;
import es.sescam.servidor.seguridad.Rol;
import es.sescam.servidor.seguridad.Usuario;
import es.sescam.servidor.service.GestionUsuariosServiceImpl;

class GestionUsuariosControllerTest {

    private GestionUsuariosController controller;
    private Usuario admin;

    @BeforeEach
    void setUp() {
        AuditoriaStub auditoria = new AuditoriaStub();
        ControlAccesoBasico controlAcceso = new ControlAccesoBasico(auditoria);
        GestionUsuariosServiceImpl service = new GestionUsuariosServiceImpl(new UsuarioRepositoryMemoria(), controlAcceso, auditoria);
        controller = new GestionUsuariosControllerImpl(service, controlAcceso);
        admin = new Usuario("admin-1", "admin", "Admin", EnumSet.of(Rol.ADMINISTRADOR_SISTEMA), true);
    }

    @Test
    void controladorGestionaUsuariosYPermisos() {
        controller.crearUsuario(admin, "u1", "user1", "Usuario 1", EnumSet.of(Rol.MEDICO));
        controller.asignarRoles(admin, "u1", EnumSet.of(Rol.MEDICO, Rol.PACIENTE));
        Usuario u = controller.obtenerUsuario(admin, "u1");
        assertEquals(Set.of(Rol.MEDICO, Rol.PACIENTE), u.getRoles());

        controller.definirPermisosRol(admin, Rol.PACIENTE, EnumSet.of(Permiso.CONSULTAR_CITAS, Permiso.CONSULTAR_INFORMES));
        Set<Permiso> permisos = controller.consultarPermisosRol(admin, Rol.PACIENTE);
        assertTrue(permisos.contains(Permiso.CONSULTAR_INFORMES));

        controller.desactivarUsuario(admin, "u1");
        assertFalse(controller.obtenerUsuario(admin, "u1").isActivo());
        assertEquals(1, controller.listarUsuarios(admin).size());
    }

    @Test
    void consultarPermisosRequiereAdmin() {
        Usuario medico = new Usuario("m1", "med", "Med", EnumSet.of(Rol.MEDICO), true);
        assertThrows(AccesoDenegado.class, () -> controller.consultarPermisosRol(medico, Rol.MEDICO));
        assertThrows(AccesoDenegado.class,
                () -> controller.definirPermisosRol(medico, Rol.MEDICO, EnumSet.of(Permiso.CONSULTAR_CITAS)));
    }
}
