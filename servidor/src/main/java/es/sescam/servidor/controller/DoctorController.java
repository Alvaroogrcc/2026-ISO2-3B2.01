package es.sescam.servidor.controller;

import java.util.List;
import java.util.Optional;

import es.sescam.servidor.model.Doctor;

public interface DoctorController {

    Doctor crearDoctor(Doctor doctor);

    Optional<Doctor> obtenerDoctor(Long id);

    List<Doctor> listarDoctores();
}
