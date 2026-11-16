package es.sescam.servidor.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import es.sescam.servidor.model.Paciente;

/**
 * Implementación en memoria del repositorio de pacientes.
 * <p>
 * No aplica reglas de negocio: solo almacena y recupera pacientes. Es apta para
 * ejecuciones concurrentes gracias al uso de estructuras concurrentes.
 * </p>
 */
public class PacienteRepositoryMemoria implements PacienteRepository {

    private final ConcurrentMap<Long, Paciente> pacientes = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(0);

    /**
     * Crea un repositorio de pacientes vacío.
     */
    public PacienteRepositoryMemoria() {
    }

    @Override
    public List<Paciente> findAll() {
        return new ArrayList<>(pacientes.values());
    }

    @Override
    public Optional<Paciente> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(pacientes.get(id));
    }

    @Override
    public Paciente save(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("El paciente no puede ser nulo");
        }
        if (paciente.getId() == null) {
            paciente.setId(secuencia.incrementAndGet());
        } else {
            secuencia.updateAndGet(actual -> Math.max(actual, paciente.getId()));
        }
        pacientes.put(paciente.getId(), paciente);
        return paciente;
    }

    @Override
    public Optional<Paciente> findByDni(String dni) {
        if (dni == null || dni.isBlank()) {
            return Optional.empty();
        }
        for (Paciente paciente : pacientes.values()) {
            if (dni.equals(paciente.getDni())) {
                return Optional.of(paciente);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Paciente> findByNumeroHistoriaClinica(String numeroHistoriaClinica) {
        if (numeroHistoriaClinica == null || numeroHistoriaClinica.isBlank()) {
            return Optional.empty();
        }
        for (Paciente paciente : pacientes.values()) {
            if (numeroHistoriaClinica.equals(paciente.getNumeroHistoriaClinica())) {
                return Optional.of(paciente);
            }
        }
        return Optional.empty();
    }
}
