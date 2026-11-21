package es.sescam.servidor.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import es.sescam.servidor.model.Cita;

public class CitaRepositoryMemoria implements CitaRepository {

    private final ConcurrentMap<Long, Cita> citas = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(0);

    @Override
    public List<Cita> findAll() {
        return new ArrayList<>(citas.values());
    }

    @Override
    public Optional<Cita> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(citas.get(id));
    }

    @Override
    public Cita save(Cita cita) {
        if (cita == null) {
            throw new IllegalArgumentException("La cita no puede ser nula");
        }
        if (cita.getId() == null) {
            cita.setId(secuencia.incrementAndGet());
        } else {
            secuencia.updateAndGet(actual -> Math.max(actual, cita.getId()));
        }
        citas.put(cita.getId(), cita);
        return cita;
    }

    @Override
    public List<Cita> findByPacienteId(Long pacienteId) {
        List<Cita> resultado = new ArrayList<>();
        if (pacienteId == null) {
            return resultado;
        }
        for (Cita cita : citas.values()) {
            if (cita.getPaciente() != null && pacienteId.equals(cita.getPaciente().getId())) {
                resultado.add(cita);
            }
        }
        return resultado;
    }

    @Override
    public List<Cita> findByDoctorIdAndFecha(Long doctorId, LocalDate fecha) {
        List<Cita> resultado = new ArrayList<>();
        if (doctorId == null || fecha == null) {
            return resultado;
        }
        for (Cita cita : citas.values()) {
            if (cita.getDoctor() != null && doctorId.equals(cita.getDoctor().getId())
                    && fecha.equals(cita.getFecha())) {
                resultado.add(cita);
            }
        }
        return resultado;
    }
}
