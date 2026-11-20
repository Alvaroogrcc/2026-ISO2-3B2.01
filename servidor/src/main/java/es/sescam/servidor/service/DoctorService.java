package es.sescam.servidor.service;

import java.util.List;
import java.util.Optional;

import es.sescam.servidor.model.Doctor;

public interface DoctorService {

    Doctor crear(Doctor doctor);

    Optional<Doctor> obtenerPorId(Long id);

    List<Doctor> listar();
}
