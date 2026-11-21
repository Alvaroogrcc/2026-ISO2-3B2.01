package es.sescam.servidor.service;

import java.util.List;
import java.util.Optional;

import es.sescam.servidor.model.Cita;

public interface CitaService {

    Cita crear(Cita cita);

    Optional<Cita> obtenerPorId(Long id);

    List<Cita> listar();

    List<Cita> listarPorPaciente(Long pacienteId);
}
