package es.sescam.cliente.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import es.sescam.cliente.model.CitaDTO;
import es.sescam.cliente.service.auth.Solicitante;
import es.sescam.cliente.service.error.ApiException;

/**
 * Contrato API específico para la entidad Cita.
 * El solicitante se identifica por DNI único para controlar acceso.
 */
public interface CitaApiClient {

    String getBaseUrl();

    List<CitaDTO> obtenerCitasPaciente(Solicitante solicitante, Long pacienteId);

    CitaDTO obtenerCita(Solicitante solicitante, Long citaId);

    /**
     * Asigna una cita pendiente a través del canal remoto.
     *
     * @param solicitante identidad del administrativo que opera
     * @param citaId identificador de la cita pendiente
     * @param fecha nueva fecha
     * @param hora nueva hora
     * @return la cita confirmada devuelta por el servidor
     * @throws ApiException si la identidad no es válida, el rol no es administrativo o la cita no existe
     */
    CitaDTO asignarCita(Solicitante solicitante, Long citaId, LocalDate fecha, LocalTime hora);

    /**
     * Reprograma una cita confirmada a través del canal remoto.
     *
     * @param solicitante identidad del administrativo que opera
     * @param citaId identificador de la cita
     * @param fecha nueva fecha
     * @param hora nueva hora
     * @return la cita reprogramada devuelta por el servidor
     * @throws ApiException si la identidad, el rol o los datos de la cita no son válidos
     */
    CitaDTO reprogramarCita(Solicitante solicitante, Long citaId, LocalDate fecha, LocalTime hora);

    /**
     * Cancela una cita a través del canal remoto.
     *
     * @param solicitante identidad del administrativo que opera
     * @param citaId identificador de la cita
     * @return la cita cancelada devuelta por el servidor
     * @throws ApiException si la identidad, el rol o la cita no son válidos
     */
    CitaDTO cancelarCita(Solicitante solicitante, Long citaId);
}
