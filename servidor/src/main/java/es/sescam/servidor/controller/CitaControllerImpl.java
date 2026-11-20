package es.sescam.servidor.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import es.sescam.servidor.model.Cita;
import es.sescam.servidor.service.CitaService;

public class CitaControllerImpl implements CitaController {

    private final CitaService citaService;

    public CitaControllerImpl(CitaService citaService) {
        this.citaService = Objects.requireNonNull(citaService, "citaService");
    }

    @Override
    public Cita crearCita(Cita cita) {
        return citaService.crear(cita);
    }

    @Override
    public Optional<Cita> obtenerCita(Long id) {
        return citaService.obtenerPorId(id);
    }

    @Override
    public List<Cita> listarCitas() {
        return citaService.listar();
    }
}
