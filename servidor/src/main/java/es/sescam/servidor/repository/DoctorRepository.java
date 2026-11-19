package es.sescam.servidor.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import es.sescam.servidor.model.Doctor;

public class DoctorRepository {

    private final List<Doctor> doctores = new ArrayList<>();

    public List<Doctor> findAll() {
        return new ArrayList<>(doctores);
    }

    public Optional<Doctor> findById(Long id) {
        return doctores.stream().filter(d -> d.getId() != null && d.getId().equals(id)).findFirst();
    }

    public Doctor save(Doctor doctor) {
        doctores.add(doctor);
        return doctor;
    }
}
