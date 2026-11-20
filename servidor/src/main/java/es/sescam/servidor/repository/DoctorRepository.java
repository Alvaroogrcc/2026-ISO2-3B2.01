package es.sescam.servidor.repository;

import java.util.List;
import java.util.Optional;

import es.sescam.servidor.model.Doctor;

public interface DoctorRepository {

    List<Doctor> findAll();

    Optional<Doctor> findById(Long id);

    Doctor save(Doctor doctor);
}
