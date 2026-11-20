package es.sescam.servidor.service;

import java.util.List;
import java.util.Optional;

import es.sescam.servidor.model.Paciente;

public interface PacienteService {

    Paciente crear(Paciente paciente);

    Optional<Paciente> obtenerPorId(Long id);

    List<Paciente> listar();
}
