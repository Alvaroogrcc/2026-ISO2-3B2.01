package es.sescam.cliente.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import es.sescam.cliente.model.CitaDTO;
import es.sescam.cliente.service.error.ApiError;
import es.sescam.cliente.service.error.ApiException;

public class CitaApiClientRest implements CitaApiClient {

    private final String baseUrl;

    public CitaApiClientRest(String baseUrl) {
        this.baseUrl = Objects.requireNonNull(baseUrl, "baseUrl");
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public List<CitaDTO> obtenerCitasPaciente(Long pacienteId) {
        if (pacienteId == null) {
            throw new ApiException(new ApiError("CITA_INVALID_REQUEST", "pacienteId no puede ser nulo", 400));
        }
        // Implementación mínima: pendiente de llamada HTTP real.
        return Collections.emptyList();
    }

    @Override
    public CitaDTO obtenerCita(Long citaId) {
        if (citaId == null) {
            throw new ApiException(new ApiError("CITA_INVALID_REQUEST", "citaId no puede ser nulo", 400));
        }
        // Implementación mínima: pendiente de llamada HTTP real.
        throw new ApiException(new ApiError("CITA_NOT_FOUND", "No se ha encontrado la cita con id " + citaId, 404));
    }
}
