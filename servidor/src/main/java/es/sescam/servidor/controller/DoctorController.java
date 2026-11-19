package es.sescam.servidor.controller;

import es.sescam.servidor.service.DoctorService;

public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    public DoctorService getDoctorService() {
        return doctorService;
    }
}
