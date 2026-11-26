package es.sescam.servidor.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import es.sescam.servidor.model.InformeClinico;

/**
 * Implementación en memoria del repositorio de informes clínicos (CU-09).
 * <p>
 * No aplica reglas de negocio: solo almacena y recupera informes. Es apta para
 * ejecuciones concurrentes gracias al uso de estructuras concurrentes.
 * </p>
 */
public class InformeClinicoRepositoryMemoria implements InformeClinicoRepository {

    private final ConcurrentMap<Long, InformeClinico> informes = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(0);

    /**
     * Crea un repositorio de informes clínicos vacío.
     */
    public InformeClinicoRepositoryMemoria() {
    }

    @Override
    public List<InformeClinico> findAll() {
        return new ArrayList<>(informes.values());
    }

    @Override
    public Optional<InformeClinico> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(informes.get(id));
    }

    @Override
    public InformeClinico save(InformeClinico informe) {
        if (informe == null) {
            throw new IllegalArgumentException("El informe clínico no puede ser nulo");
        }
        if (informe.getId() == null) {
            informe.setId(secuencia.incrementAndGet());
        } else {
            secuencia.updateAndGet(actual -> Math.max(actual, informe.getId()));
        }
        informes.put(informe.getId(), informe);
        return informe;
    }

    @Override
    public List<InformeClinico> findByPacienteId(Long pacienteId) {
        List<InformeClinico> encontrados = new ArrayList<>();
        if (pacienteId == null) {
            return encontrados;
        }
        for (InformeClinico informe : informes.values()) {
            if (informe.getPaciente() != null && pacienteId.equals(informe.getPaciente().getId())) {
                encontrados.add(informe);
            }
        }
        return encontrados;
    }

    @Override
    public List<InformeClinico> findBySolicitudId(Long solicitudId) {
        List<InformeClinico> encontrados = new ArrayList<>();
        if (solicitudId == null) {
            return encontrados;
        }
        for (InformeClinico informe : informes.values()) {
            if (informe.getSolicitud() != null && solicitudId.equals(informe.getSolicitud().getId())) {
                encontrados.add(informe);
            }
        }
        return encontrados;
    }
}
