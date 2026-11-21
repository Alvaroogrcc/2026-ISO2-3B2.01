package es.sescam.cliente.service;

import java.util.List;

import es.sescam.cliente.model.DoctorDTO;

/**
 * Contrato API específico para la entidad Doctor.
 */
public interface DoctorApiClient {

    String getBaseUrl();

    DoctorDTO obtenerDoctor(Long doctorId);

    List<DoctorDTO> listarDoctores();
}
