package es.sescam.servidor.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import es.sescam.servidor.model.Cita;
import es.sescam.servidor.seguridad.Usuario;

public interface CitaController {

    Cita crearCita(Cita cita);

    Optional<Cita> obtenerCita(Long id);

    List<Cita> listarCitas();

    List<Cita> listarCitasPorPaciente(Long pacienteId);

    /**
     * Puerta de entrada para asignar una cita pendiente. Delega todas las reglas en el servicio.
     */
    Cita asignarCita(Long citaId, LocalDate fecha, LocalTime hora, Usuario usuario);

    /**
     * Puerta de entrada para reprogramar una cita confirmada.
     */
    Cita reprogramarCita(Long citaId, LocalDate fecha, LocalTime hora, Usuario usuario);

    /**
     * Puerta de entrada para cancelar una cita y liberar su hueco.
     */
    Cita cancelarCita(Long citaId, Usuario usuario);
}
