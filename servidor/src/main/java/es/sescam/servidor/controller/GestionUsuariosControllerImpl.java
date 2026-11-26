package es.sescam.servidor.controller;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import es.sescam.servidor.seguridad.ControlAcceso;
import es.sescam.servidor.seguridad.Permiso;
import es.sescam.servidor.seguridad.Rol;
import es.sescam.servidor.seguridad.Usuario;
import es.sescam.servidor.service.GestionUsuariosService;

/**
 * Implementación de controlador de entrada para CU-18.
 */
public class GestionUsuariosControllerImpl implements GestionUsuariosController {

    private final GestionUsuariosService gestionUsuariosService;
    private final ControlAcceso controlAcceso;

    public GestionUsuariosControllerImpl(GestionUsuariosService gestionUsuariosService, ControlAcceso controlAcceso) {
        this.gestionUsuariosService = Objects.requireNonNull(gestionUsuariosService, "gestionUsuariosService");
        this.controlAcceso = Objects.requireNonNull(controlAcceso, "controlAcceso");
    }

    @Override
    public Usuario crearUsuario(Usuario administrador, String id, String nombreUsuario, String nombreCompleto, Set<Rol> roles) {
        return gestionUsuariosService.crearUsuario(administrador, id, nombreUsuario, nombreCompleto, roles);
    }

    @Override
    public Usuario asignarRoles(Usuario administrador, String usuarioId, Set<Rol> roles) {
        return gestionUsuariosService.asignarRoles(administrador, usuarioId, roles);
    }

    @Override
    public void desactivarUsuario(Usuario administrador, String usuarioId) {
        gestionUsuariosService.desactivarUsuario(administrador, usuarioId);
    }

    @Override
    public List<Usuario> listarUsuarios(Usuario administrador) {
        return gestionUsuariosService.listarUsuarios(administrador);
    }

    @Override
    public Usuario obtenerUsuario(Usuario administrador, String usuarioId) {
        return gestionUsuariosService.obtenerUsuario(administrador, usuarioId);
    }

    @Override
    public void definirPermisosRol(Usuario administrador, Rol rol, Set<Permiso> permisos) {
        controlAcceso.definirPermisosRol(administrador, rol, permisos);
    }

    @Override
    public Set<Permiso> consultarPermisosRol(Usuario administrador, Rol rol) {
        controlAcceso.exigirPermiso(administrador, Permiso.GESTIONAR_ROLES_PERMISOS, "seguridad/roles/" + rol);
        return controlAcceso.permisosDeRol(rol);
    }
}
