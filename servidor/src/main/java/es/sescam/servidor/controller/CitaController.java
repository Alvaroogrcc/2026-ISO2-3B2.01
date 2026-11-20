package es.sescam.servidor.controller;

import java.util.List;
import java.util.Optional;

import es.sescam.servidor.model.Cita;

public interface CitaController {

    Cita crearCita(Cita cita);

    Optional<Cita> obtenerCita(Long id);

    List<Cita> listarCitas();
}
