package es.sescam.servidor.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import es.sescam.servidor.model.Doctor;
import es.sescam.servidor.repository.DoctorRepository;

public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = Objects.requireNonNull(doctorRepository, "doctorRepository");
    }

    @Override
    public Doctor crear(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public Optional<Doctor> obtenerPorId(Long id) {
        return doctorRepository.findById(id);
    }

    @Override
    public List<Doctor> listar() {
        return doctorRepository.findAll();
    }
}
