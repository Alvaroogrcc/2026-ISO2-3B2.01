package es.sescam.cliente.service;

import java.util.List;

import es.sescam.cliente.model.PacienteDTO;

/**
 * Contrato API específico para la entidad Paciente.
 */
public interface PacienteApiClient {

    String getBaseUrl();

    PacienteDTO obtenerPaciente(Long pacienteId);

    List<PacienteDTO> listarPacientes();
}
