package es.sescam.servidor.service;

import es.sescam.servidor.repository.DoctorRepository;

public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public DoctorRepository getDoctorRepository() {
        return doctorRepository;
    }
}
