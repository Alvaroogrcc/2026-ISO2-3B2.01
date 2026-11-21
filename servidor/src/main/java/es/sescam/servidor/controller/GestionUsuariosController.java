package es.sescam.servidor.controller;

import java.util.List;
import java.util.Set;

import es.sescam.servidor.seguridad.Permiso;
import es.sescam.servidor.seguridad.Rol;
import es.sescam.servidor.seguridad.Usuario;

/**
 * Contrato de controlador para CU-18: gestión de usuarios, roles y permisos.
 */
public interface GestionUsuariosController {

    Usuario crearUsuario(Usuario administrador, String id, String nombreUsuario, String nombreCompleto, Set<Rol> roles);

    Usuario asignarRoles(Usuario administrador, String usuarioId, Set<Rol> roles);

    void desactivarUsuario(Usuario administrador, String usuarioId);

    List<Usuario> listarUsuarios(Usuario administrador);

    Usuario obtenerUsuario(Usuario administrador, String usuarioId);

    void definirPermisosRol(Usuario administrador, Rol rol, Set<Permiso> permisos);

    Set<Permiso> consultarPermisosRol(Usuario administrador, Rol rol);
}
