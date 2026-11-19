package es.sescam.servidor.controller;

import es.sescam.servidor.service.CitaService;

public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    public CitaService getCitaService() {
        return citaService;
    }
}
