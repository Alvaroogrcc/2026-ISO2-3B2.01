package es.sescam.servidor.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import es.sescam.servidor.model.EstadoSolicitud;
import es.sescam.servidor.model.SolicitudPrueba;

/**
 * Implementación en memoria del repositorio de solicitudes de prueba (CU-06).
 * <p>
 * No aplica reglas de negocio: solo almacena y recupera solicitudes. Es apta para
 * ejecuciones concurrentes gracias al uso de estructuras concurrentes.
 * </p>
 */
public class SolicitudPruebaRepositoryMemoria implements SolicitudPruebaRepository {

    private final ConcurrentMap<Long, SolicitudPrueba> solicitudes = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(0);

    /**
     * Crea un repositorio de solicitudes vacío.
     */
    public SolicitudPruebaRepositoryMemoria() {
    }

    @Override
    public List<SolicitudPrueba> findAll() {
        return new ArrayList<>(solicitudes.values());
    }

    @Override
    public Optional<SolicitudPrueba> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(solicitudes.get(id));
    }

    @Override
    public SolicitudPrueba save(SolicitudPrueba solicitud) {
        if (solicitud == null) {
            throw new IllegalArgumentException("La solicitud de prueba no puede ser nula");
        }
        if (solicitud.getId() == null) {
            solicitud.setId(secuencia.incrementAndGet());
        } else {
            secuencia.updateAndGet(actual -> Math.max(actual, solicitud.getId()));
        }
        solicitudes.put(solicitud.getId(), solicitud);
        return solicitud;
    }

    @Override
    public List<SolicitudPrueba> findByPacienteId(Long pacienteId) {
        List<SolicitudPrueba> encontradas = new ArrayList<>();
        if (pacienteId == null) {
            return encontradas;
        }
        for (SolicitudPrueba solicitud : solicitudes.values()) {
            if (solicitud.getPaciente() != null && pacienteId.equals(solicitud.getPaciente().getId())) {
                encontradas.add(solicitud);
            }
        }
        return encontradas;
    }

    @Override
    public List<SolicitudPrueba> findByEstado(EstadoSolicitud estado) {
        List<SolicitudPrueba> encontradas = new ArrayList<>();
        if (estado == null) {
            return encontradas;
        }
        for (SolicitudPrueba solicitud : solicitudes.values()) {
            if (estado == solicitud.getEstado()) {
                encontradas.add(solicitud);
            }
        }
        return encontradas;
    }
}
