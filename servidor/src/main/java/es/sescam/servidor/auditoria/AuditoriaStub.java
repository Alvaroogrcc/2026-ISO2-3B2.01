package es.sescam.servidor.auditoria;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementación stub de auditoría para la Iteración 0.
 */
public class AuditoriaStub implements Auditoria {

    private final List<String> eventos = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void registrarAcceso(String usuarioId, String recurso, String accion, Instant fechaHora) {
        eventos.add(String.format("ACCESO|%s|%s|%s|%s", fechaHora, usuarioId, recurso, accion));
    }

    @Override
    public void registrarEvento(String tipoEvento, String descripcion, Instant fechaHora) {
        eventos.add(String.format("EVENTO|%s|%s|%s", fechaHora, tipoEvento, descripcion));
    }

    public List<String> getEventos() {
        synchronized (eventos) {
            return new ArrayList<>(eventos);
        }
    }
}
