package es.sescam.cliente.service;

import java.util.List;

import es.sescam.cliente.model.DoctorDTO;
import es.sescam.cliente.service.auth.Solicitante;

/**
 * Contrato API específico para la entidad Doctor.
 * El solicitante se identifica por DNI único para controlar acceso.
 */
public interface DoctorApiClient {

    String getBaseUrl();

    DoctorDTO obtenerDoctor(Solicitante solicitante, Long doctorId);

    List<DoctorDTO> listarDoctores(Solicitante solicitante);
}
