package es.sescam.cliente.service;

import java.util.List;

import es.sescam.cliente.model.CitaDTO;

/**
 * Contrato API específico para la entidad Cita.
 */
public interface CitaApiClient {

    String getBaseUrl();

    List<CitaDTO> obtenerCitasPaciente(Long pacienteId);

    CitaDTO obtenerCita(Long citaId);
}
