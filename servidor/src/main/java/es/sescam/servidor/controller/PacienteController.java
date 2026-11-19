package es.sescam.servidor.controller;

import es.sescam.servidor.service.PacienteService;

public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    public PacienteService getPacienteService() {
        return pacienteService;
    }
}
