package es.sescam.servidor.service;

import java.util.List;
import java.util.Set;

import es.sescam.servidor.seguridad.Rol;
import es.sescam.servidor.seguridad.Usuario;

/**
 * Contrato de negocio para gestión de usuarios, roles y permisos (CU-18).
 */
public interface GestionUsuariosService {

    Usuario crearUsuario(Usuario administrador, String id, String nombreUsuario, String nombreCompleto, Set<Rol> roles);

    Usuario asignarRoles(Usuario administrador, String usuarioId, Set<Rol> roles);

    void desactivarUsuario(Usuario administrador, String usuarioId);

    Usuario obtenerUsuario(Usuario administrador, String usuarioId);

    List<Usuario> listarUsuarios(Usuario administrador);
}
