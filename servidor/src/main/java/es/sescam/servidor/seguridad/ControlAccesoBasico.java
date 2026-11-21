package es.sescam.servidor.seguridad;

import java.time.Instant;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import es.sescam.servidor.auditoria.Auditoria;
import es.sescam.servidor.error.AccesoDenegado;
import es.sescam.servidor.error.ValidacionIncorrecta;

/**
 * Implementación en memoria del control de acceso por roles y permisos (CU-18).
 */
public class ControlAccesoBasico implements ControlAcceso {

    private final Auditoria auditoria;
    private final Map<Rol, Set<Permiso>> permisosPorRol = new EnumMap<>(Rol.class);

    public ControlAccesoBasico(Auditoria auditoria) {
        this.auditoria = Objects.requireNonNull(auditoria, "auditoria");
        cargarPermisosPorDefecto();
    }

    private void cargarPermisosPorDefecto() {
        permisosPorRol.put(Rol.ADMINISTRADOR_SISTEMA, EnumSet.allOf(Permiso.class));
        permisosPorRol.put(Rol.ADMINISTRATIVO, EnumSet.of(
                Permiso.CONSULTAR_CITAS, Permiso.GESTIONAR_CITAS,
                Permiso.CONSULTAR_PACIENTES, Permiso.GESTIONAR_PACIENTES));
        permisosPorRol.put(Rol.MEDICO, EnumSet.of(
                Permiso.CONSULTAR_CITAS, Permiso.CONSULTAR_PACIENTES,
                Permiso.CONSULTAR_INFORMES, Permiso.GESTIONAR_INFORMES));
        permisosPorRol.put(Rol.PACIENTE, EnumSet.of(Permiso.CONSULTAR_CITAS));
    }

    @Override
    public boolean tienePermiso(Usuario usuario, Permiso permiso) {
        if (usuario == null || !usuario.isActivo() || permiso == null) {
            return false;
        }
        for (Rol rol : usuario.getRoles()) {
            Set<Permiso> permisos = permisosPorRol.get(rol);
            if (permisos != null && permisos.contains(permiso)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void exigirPermiso(Usuario usuario, Permiso permiso, String recurso) {
        if (tienePermiso(usuario, permiso)) {
            return;
        }
        String usuarioId = usuario == null ? "anonimo" : usuario.getId();
        String rol = (usuario == null || usuario.getRoles().isEmpty()) ? "SIN_ROL" : usuario.getRoles().iterator().next().name();
        String recursoSeguro = recurso == null ? "desconocido" : recurso;
        auditoria.registrarAcceso(usuarioId, recursoSeguro, "ACCESO_DENEGADO:" + permiso, Instant.now());
        throw new AccesoDenegado("Acceso denegado al recurso: " + recursoSeguro + " para rol: " + rol);
    }

    @Override
    public Set<Permiso> permisosDeRol(Rol rol) {
        if (rol == null) {
            throw new ValidacionIncorrecta("El rol es obligatorio");
        }
        Set<Permiso> permisos = permisosPorRol.get(rol);
        return permisos == null ? Collections.emptySet() : Collections.unmodifiableSet(EnumSet.copyOf(permisos));
    }

    @Override
    public void definirPermisosRol(Usuario administrador, Rol rol, Set<Permiso> permisos) {
        exigirPermiso(administrador, Permiso.GESTIONAR_ROLES_PERMISOS, "seguridad/roles/" + rol);
        if (rol == null) {
            throw new ValidacionIncorrecta("El rol es obligatorio");
        }
        Set<Permiso> nuevos = (permisos == null || permisos.isEmpty())
                ? Collections.emptySet()
                : EnumSet.copyOf(permisos);
        permisosPorRol.put(rol, nuevos);
        auditoria.registrarEvento("CAMBIO_PERMISOS_ROL",
                "admin=" + administrador.getId() + ";rol=" + rol + ";permisos=" + nuevos,
                Instant.now());
    }
}
