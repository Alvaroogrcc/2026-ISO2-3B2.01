package es.sescam.cliente.service;

import java.util.List;
import java.util.Objects;

import es.sescam.cliente.model.CitaDTO;
import es.sescam.cliente.model.DoctorDTO;
import es.sescam.cliente.model.PacienteDTO;

/**
 * Implementación compuesta para compatibilidad. Cada entidad tiene su propio contrato e implementación.
 */
@Deprecated
public class ApiClientRest implements ApiClient {

    private final CitaApiClient citaApiClient;
    private final PacienteApiClient pacienteApiClient;
    private final DoctorApiClient doctorApiClient;

    public ApiClientRest(String baseUrl) {
        String url = Objects.requireNonNull(baseUrl, "baseUrl");
        this.citaApiClient = new CitaApiClientRest(url);
        this.pacienteApiClient = new PacienteApiClientRest(url);
        this.doctorApiClient = new DoctorApiClientRest(url);
    }

    @Override
    public String getBaseUrl() {
        return citaApiClient.getBaseUrl();
    }

    @Override
    public List<CitaDTO> obtenerCitasPaciente(Long pacienteId) {
        return citaApiClient.obtenerCitasPaciente(pacienteId);
    }

    @Override
    public CitaDTO obtenerCita(Long citaId) {
        return citaApiClient.obtenerCita(citaId);
    }

    @Override
    public PacienteDTO obtenerPaciente(Long pacienteId) {
        return pacienteApiClient.obtenerPaciente(pacienteId);
    }

    @Override
    public List<PacienteDTO> listarPacientes() {
        return pacienteApiClient.listarPacientes();
    }

    @Override
    public DoctorDTO obtenerDoctor(Long doctorId) {
        return doctorApiClient.obtenerDoctor(doctorId);
    }

    @Override
    public List<DoctorDTO> listarDoctores() {
        return doctorApiClient.listarDoctores();
    }
}
