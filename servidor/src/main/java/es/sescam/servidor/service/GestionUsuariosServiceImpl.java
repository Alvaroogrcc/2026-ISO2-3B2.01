package es.sescam.servidor.service;

import java.time.Instant;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import es.sescam.servidor.auditoria.Auditoria;
import es.sescam.servidor.error.EntidadNoEncontrada;
import es.sescam.servidor.error.ValidacionIncorrecta;
import es.sescam.servidor.repository.UsuarioRepository;
import es.sescam.servidor.seguridad.ControlAcceso;
import es.sescam.servidor.seguridad.Permiso;
import es.sescam.servidor.seguridad.Rol;
import es.sescam.servidor.seguridad.Usuario;

/**
 * Implementación de la gestión de usuarios y asignación de roles (CU-18).
 */
public class GestionUsuariosServiceImpl implements GestionUsuariosService {

    private final UsuarioRepository usuarioRepository;
    private final ControlAcceso controlAcceso;
    private final Auditoria auditoria;

    public GestionUsuariosServiceImpl(UsuarioRepository usuarioRepository, ControlAcceso controlAcceso, Auditoria auditoria) {
        this.usuarioRepository = Objects.requireNonNull(usuarioRepository, "usuarioRepository");
        this.controlAcceso = Objects.requireNonNull(controlAcceso, "controlAcceso");
        this.auditoria = Objects.requireNonNull(auditoria, "auditoria");
    }

    @Override
    public Usuario crearUsuario(Usuario administrador, String id, String nombreUsuario, String nombreCompleto, Set<Rol> roles) {
        controlAcceso.exigirPermiso(administrador, Permiso.GESTIONAR_USUARIOS, "usuarios");
        String idNormalizado = normalizarObligatorio(id, "El id de usuario es obligatorio");
        String nombreUsuarioNormalizado = normalizarObligatorio(nombreUsuario, "El nombre de usuario es obligatorio");
        Set<Rol> rolesNormalizados = normalizarRoles(roles);
        usuarioRepository.findById(idNormalizado).ifPresent(u -> {
            throw new ValidacionIncorrecta("Ya existe un usuario con el id indicado");
        });
        usuarioRepository.findByNombreUsuario(nombreUsuarioNormalizado).ifPresent(u -> {
            throw new ValidacionIncorrecta("Ya existe un usuario con el nombre de usuario indicado");
        });
        Usuario creado = new Usuario(idNormalizado, nombreUsuarioNormalizado, nombreCompleto, rolesNormalizados, true);
        Usuario guardado = usuarioRepository.save(creado);
        auditoria.registrarEvento("USUARIO_CREADO",
                "admin=" + administrador.getId() + ";usuario=" + guardado.getId() + ";roles=" + guardado.getRoles(),
                Instant.now());
        return guardado;
    }

    @Override
    public Usuario asignarRoles(Usuario administrador, String usuarioId, Set<Rol> roles) {
        controlAcceso.exigirPermiso(administrador, Permiso.GESTIONAR_USUARIOS, "usuarios/" + usuarioId + "/roles");
        Usuario existente = obtenerExistente(usuarioId);
        Set<Rol> rolesNormalizados = normalizarRoles(roles);
        Usuario actualizado = new Usuario(existente.getId(), existente.getNombreUsuario(), existente.getNombreCompleto(),
                rolesNormalizados, existente.isActivo());
        Usuario guardado = usuarioRepository.save(actualizado);
        auditoria.registrarEvento("USUARIO_ROLES_ASIGNADOS",
                "admin=" + administrador.getId() + ";usuario=" + guardado.getId() + ";roles=" + guardado.getRoles(),
                Instant.now());
        return guardado;
    }

    @Override
    public void desactivarUsuario(Usuario administrador, String usuarioId) {
        controlAcceso.exigirPermiso(administrador, Permiso.GESTIONAR_USUARIOS, "usuarios/" + usuarioId + "/desactivar");
        Usuario existente = obtenerExistente(usuarioId);
        if (!existente.isActivo()) {
            return;
        }
        Usuario desactivado = new Usuario(existente.getId(), existente.getNombreUsuario(), existente.getNombreCompleto(),
                existente.getRoles(), false);
        usuarioRepository.save(desactivado);
        auditoria.registrarEvento("USUARIO_DESACTIVADO",
                "admin=" + administrador.getId() + ";usuario=" + desactivado.getId(),
                Instant.now());
    }

    @Override
    public Usuario obtenerUsuario(Usuario administrador, String usuarioId) {
        controlAcceso.exigirPermiso(administrador, Permiso.GESTIONAR_USUARIOS, "usuarios/" + usuarioId);
        return obtenerExistente(usuarioId);
    }

    @Override
    public List<Usuario> listarUsuarios(Usuario administrador) {
        controlAcceso.exigirPermiso(administrador, Permiso.GESTIONAR_USUARIOS, "usuarios");
        return usuarioRepository.findAll();
    }

    private Usuario obtenerExistente(String usuarioId) {
        String idNormalizado = normalizarObligatorio(usuarioId, "El id de usuario es obligatorio");
        return usuarioRepository.findById(idNormalizado)
                .orElseThrow(() -> new EntidadNoEncontrada("No existe el usuario con id " + idNormalizado));
    }

    private String normalizarObligatorio(String valor, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidacionIncorrecta(mensaje);
        }
        return valor.trim();
    }

    private Set<Rol> normalizarRoles(Set<Rol> roles) {
        if (roles == null || roles.isEmpty()) {
            throw new ValidacionIncorrecta("El usuario debe tener al menos un rol");
        }
        if (roles.stream().anyMatch(Objects::isNull)) {
            throw new ValidacionIncorrecta("Los roles no pueden contener valores nulos");
        }
        return Collections.unmodifiableSet(EnumSet.copyOf(roles));
    }
}
