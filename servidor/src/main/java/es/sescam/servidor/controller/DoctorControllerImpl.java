package es.sescam.servidor.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import es.sescam.servidor.model.Doctor;
import es.sescam.servidor.service.DoctorService;

public class DoctorControllerImpl implements DoctorController {

    private final DoctorService doctorService;

    public DoctorControllerImpl(DoctorService doctorService) {
        this.doctorService = Objects.requireNonNull(doctorService, "doctorService");
    }

    @Override
    public Doctor crearDoctor(Doctor doctor) {
        return doctorService.crear(doctor);
    }

    @Override
    public Optional<Doctor> obtenerDoctor(Long id) {
        return doctorService.obtenerPorId(id);
    }

    @Override
    public List<Doctor> listarDoctores() {
        return doctorService.listar();
    }
}
