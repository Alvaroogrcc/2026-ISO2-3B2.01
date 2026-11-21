package es.sescam.cliente.service;

import java.util.List;

import es.sescam.cliente.model.CitaDTO;
import es.sescam.cliente.service.auth.Solicitante;

/**
 * Contrato API específico para la entidad Cita.
 * El solicitante se identifica por DNI único para controlar acceso.
 */
public interface CitaApiClient {

    String getBaseUrl();

    List<CitaDTO> obtenerCitasPaciente(Solicitante solicitante, Long pacienteId);

    CitaDTO obtenerCita(Solicitante solicitante, Long citaId);
}
