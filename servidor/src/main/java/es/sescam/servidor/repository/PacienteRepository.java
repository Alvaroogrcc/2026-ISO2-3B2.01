package es.sescam.servidor.repository;

import java.util.List;
import java.util.Optional;

import es.sescam.servidor.model.Paciente;

public interface PacienteRepository {

    List<Paciente> findAll();

    Optional<Paciente> findById(Long id);

    Paciente save(Paciente paciente);
}
