package es.sescam.servidor.service;

import es.sescam.servidor.repository.PacienteRepository;

public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public PacienteRepository getPacienteRepository() {
        return pacienteRepository;
    }
}
