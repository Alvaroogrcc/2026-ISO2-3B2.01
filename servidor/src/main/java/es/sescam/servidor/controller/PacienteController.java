package es.sescam.servidor.controller;

import java.util.List;
import java.util.Optional;

import es.sescam.servidor.model.Paciente;

public interface PacienteController {

    Paciente crearPaciente(Paciente paciente);

    Optional<Paciente> obtenerPaciente(Long id);

    List<Paciente> listarPacientes();
}
