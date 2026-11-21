package es.sescam.cliente.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import es.sescam.cliente.model.PacienteDTO;
import es.sescam.cliente.service.error.ApiError;
import es.sescam.cliente.service.error.ApiException;

public class PacienteApiClientRest implements PacienteApiClient {

    private final String baseUrl;

    public PacienteApiClientRest(String baseUrl) {
        this.baseUrl = Objects.requireNonNull(baseUrl, "baseUrl");
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public PacienteDTO obtenerPaciente(Long pacienteId) {
        if (pacienteId == null) {
            throw new ApiException(new ApiError("PACIENTE_INVALID_REQUEST", "pacienteId no puede ser nulo", 400));
        }
        throw new ApiException(new ApiError("PACIENTE_NOT_FOUND", "No se ha encontrado el paciente con id " + pacienteId, 404));
    }

    @Override
    public List<PacienteDTO> listarPacientes() {
        return Collections.emptyList();
    }
}
