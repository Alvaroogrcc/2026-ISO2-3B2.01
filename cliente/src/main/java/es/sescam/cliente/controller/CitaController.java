package es.sescam.cliente.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import es.sescam.cliente.model.CitaDTO;

import es.sescam.cliente.service.auth.Solicitante;

public interface CitaController {

    void consultarMisCitas(Solicitante solicitante, Long pacienteId);

    void consultarDetalleCita(Solicitante solicitante, Long citaId);

    void mostrarCita(CitaDTO cita);

    void mostrarCitas(List<CitaDTO> citas);

    void mostrarMensaje(String mensaje);

    /**
     * Solicita la asignación de una cita pendiente y muestra el resultado por la vista.
     */
    void asignarCita(Solicitante solicitante, Long citaId, LocalDate fecha, LocalTime hora);

    /**
     * Solicita la reprogramación de una cita confirmada y muestra el resultado por la vista.
     */
    void reprogramarCita(Solicitante solicitante, Long citaId, LocalDate fecha, LocalTime hora);

    /**
     * Solicita la cancelación de una cita y muestra el resultado por la vista.
     */
    void cancelarCita(Solicitante solicitante, Long citaId);
}
