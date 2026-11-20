package es.sescam.servidor.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import es.sescam.servidor.model.Paciente;
import es.sescam.servidor.service.PacienteService;

public class PacienteControllerImpl implements PacienteController {

    private final PacienteService pacienteService;

    public PacienteControllerImpl(PacienteService pacienteService) {
        this.pacienteService = Objects.requireNonNull(pacienteService, "pacienteService");
    }

    @Override
    public Paciente crearPaciente(Paciente paciente) {
        return pacienteService.crear(paciente);
    }

    @Override
    public Optional<Paciente> obtenerPaciente(Long id) {
        return pacienteService.obtenerPorId(id);
    }

    @Override
    public List<Paciente> listarPacientes() {
        return pacienteService.listar();
    }
}
