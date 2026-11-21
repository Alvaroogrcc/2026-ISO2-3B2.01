package es.sescam.cliente.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import es.sescam.cliente.model.DoctorDTO;
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
    public DoctorDTO obtenerDoctor(Long doctorId) {
        if (doctorId == null) {
            throw new ApiException(new ApiError("DOCTOR_INVALID_REQUEST", "doctorId no puede ser nulo", 400));
        }
        return null;
    }

    @Override
    public List<DoctorDTO> listarDoctores() {
        return Collections.emptyList();
    }
}
