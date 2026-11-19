package es.sescam.servidor.service;

import es.sescam.servidor.repository.CitaRepository;

public class CitaService {

    private final CitaRepository citaRepository;

    public CitaService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    public CitaRepository getCitaRepository() {
        return citaRepository;
    }
}
