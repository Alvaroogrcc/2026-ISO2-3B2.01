package es.sescam.servidor.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import es.sescam.servidor.model.Doctor;

public class DoctorRepositoryMemoria implements DoctorRepository {

    private final ConcurrentMap<Long, Doctor> doctores = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(0);

    @Override
    public List<Doctor> findAll() {
        return new ArrayList<>(doctores.values());
    }

    @Override
    public Optional<Doctor> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(doctores.get(id));
    }

    @Override
    public Doctor save(Doctor doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("El doctor no puede ser nulo");
        }
        if (doctor.getId() == null) {
            doctor.setId(secuencia.incrementAndGet());
        } else {
            secuencia.updateAndGet(actual -> Math.max(actual, doctor.getId()));
        }
        doctores.put(doctor.getId(), doctor);
        return doctor;
    }
}
