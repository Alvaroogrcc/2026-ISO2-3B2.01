package es.sescam.cliente.service.auth;

import java.util.Objects;

/**
 * Identidad del solicitante para aplicar reglas de autorización en contratos API.
 */
public class Solicitante {

    private final String dni;
    private final RolSolicitante rol;

    public Solicitante(String dni, RolSolicitante rol) {
        this.dni = Objects.requireNonNull(dni, "dni");
        this.rol = Objects.requireNonNull(rol, "rol");
        if (this.dni.isBlank()) {
            throw new IllegalArgumentException("dni no puede estar vacío");
        }
    }

    public String getDni() {
        return dni;
    }

    public RolSolicitante getRol() {
        return rol;
    }
}
