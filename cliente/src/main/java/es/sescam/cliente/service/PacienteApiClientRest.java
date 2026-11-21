package es.sescam.cliente.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import es.sescam.cliente.model.PacienteDTO;
import es.sescam.cliente.service.auth.RolSolicitante;
import es.sescam.cliente.service.auth.Solicitante;
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
    public PacienteDTO obtenerPaciente(Solicitante solicitante, Long pacienteId) {
        validarSolicitante(solicitante);
        if (pacienteId == null) {
            throw new ApiException(new ApiError("PACIENTE_INVALID_REQUEST", "pacienteId no puede ser nulo", 400));
        }
        if (solicitante.getRol() == RolSolicitante.PACIENTE) {
            throw new ApiException(new ApiError("FORBIDDEN", "Un paciente solo puede consultar su propia información", 403));
        }
        throw new ApiException(new ApiError("PACIENTE_NOT_FOUND", "No se ha encontrado el paciente con id " + pacienteId, 404));
    }

    @Override
    public List<PacienteDTO> listarPacientes(Solicitante solicitante) {
        validarSolicitante(solicitante);
        if (solicitante.getRol() != RolSolicitante.DOCTOR && solicitante.getRol() != RolSolicitante.ADMINISTRADOR) {
            throw new ApiException(new ApiError("FORBIDDEN", "Solo doctores o administradores pueden listar pacientes", 403));
        }
        return Collections.emptyList();
    }

    private void validarSolicitante(Solicitante solicitante) {
        if (solicitante == null || solicitante.getDni() == null || solicitante.getDni().isBlank()) {
            throw new ApiException(new ApiError("UNAUTHORIZED", "Debe identificarse mediante un DNI válido", 401));
        }
    }
}
