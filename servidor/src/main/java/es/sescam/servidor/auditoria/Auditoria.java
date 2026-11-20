package es.sescam.servidor.auditoria;

import java.time.Instant;

/**
 * Contrato de auditoría del sistema (It. 0). Registra accesos y actividades relevantes.
 */
public interface Auditoria {

    void registrarAcceso(String usuarioId, String recurso, String accion, Instant fechaHora);

    void registrarEvento(String tipoEvento, String descripcion, Instant fechaHora);
}
