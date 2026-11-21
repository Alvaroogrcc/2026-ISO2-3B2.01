package es.sescam.cliente.service;

import java.util.List;

import es.sescam.cliente.model.PacienteDTO;
import es.sescam.cliente.service.auth.Solicitante;

/**
 * Contrato API específico para la entidad Paciente.
 * El solicitante se identifica por DNI único para controlar acceso.
 */
public interface PacienteApiClient {

    String getBaseUrl();

    PacienteDTO obtenerPaciente(Solicitante solicitante, Long pacienteId);

    /**
     * Solo DOCTOR o ADMINISTRADOR pueden listar pacientes.
     */
    List<PacienteDTO> listarPacientes(Solicitante solicitante);
}
