package es.sescam.servidor.seguridad;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * Modelo de usuario y roles (RNF-SEG-01).
 */
public class Usuario {

    private final String id;
    private final String nombreUsuario;
    private final String nombreCompleto;
    private final Set<Rol> roles;
    private final boolean activo;

    public Usuario(String id, String nombreUsuario, String nombreCompleto, Set<Rol> roles) {
        this(id, nombreUsuario, nombreCompleto, roles, true);
    }

    public Usuario(String id, String nombreUsuario, String nombreCompleto, Set<Rol> roles, boolean activo) {
        this.id = Objects.requireNonNull(id, "id");
        this.nombreUsuario = Objects.requireNonNull(nombreUsuario, "nombreUsuario");
        this.nombreCompleto = nombreCompleto;
        this.roles = roles == null || roles.isEmpty()
                ? Collections.emptySet()
                : Collections.unmodifiableSet(EnumSet.copyOf(roles));
        this.activo = activo;
    }

    public String getId() { return id; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getNombreCompleto() { return nombreCompleto; }
    public Set<Rol> getRoles() { return roles; }
    public boolean isActivo() { return activo; }

    public boolean tieneRol(Rol rol) {
        return roles.contains(rol);
    }
}
