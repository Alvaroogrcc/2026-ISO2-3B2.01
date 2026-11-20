package es.sescam.servidor.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import es.sescam.servidor.model.Paciente;

public class PacienteRepositoryMemoria implements PacienteRepository {

    private final ConcurrentMap<Long, Paciente> pacientes = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(0);

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
}
