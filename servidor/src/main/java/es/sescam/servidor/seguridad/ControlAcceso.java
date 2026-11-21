package es.sescam.servidor.seguridad;

import java.util.Set;

/**
 * Contrato de control de acceso por rol y permiso (CU-18, RNF-SEG-01).
 */
public interface ControlAcceso {

    boolean tienePermiso(Usuario usuario, Permiso permiso);

    void exigirPermiso(Usuario usuario, Permiso permiso, String recurso);

    Set<Permiso> permisosDeRol(Rol rol);

    void definirPermisosRol(Usuario administrador, Rol rol, Set<Permiso> permisos);
}
