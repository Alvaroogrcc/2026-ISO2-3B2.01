package es.sescam.cliente.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import es.sescam.cliente.model.DoctorDTO;
import es.sescam.cliente.service.auth.Solicitante;
import es.sescam.cliente.service.error.ApiError;
import es.sescam.cliente.service.error.ApiException;

public class DoctorApiClientRest implements DoctorApiClient {

    private final String baseUrl;

    public DoctorApiClientRest(String baseUrl) {
        this.baseUrl = Objects.requireNonNull(baseUrl, "baseUrl");
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public DoctorDTO obtenerDoctor(Solicitante solicitante, Long doctorId) {
        validarSolicitante(solicitante);
        if (doctorId == null) {
            throw new ApiException(new ApiError("DOCTOR_INVALID_REQUEST", "doctorId no puede ser nulo", 400));
        }
        throw new ApiException(new ApiError("DOCTOR_NOT_FOUND", "No se ha encontrado el doctor con id " + doctorId, 404));
    }

    @Override
    public List<DoctorDTO> listarDoctores(Solicitante solicitante) {
        validarSolicitante(solicitante);
        return Collections.emptyList();
    }

    private void validarSolicitante(Solicitante solicitante) {
        if (solicitante == null || solicitante.getDni() == null || solicitante.getDni().isBlank()) {
            throw new ApiException(new ApiError("UNAUTHORIZED", "Debe identificarse mediante un DNI válido", 401));
        }
    }
}
