package es.sescam.cliente.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import es.sescam.cliente.model.CitaDTO;
import es.sescam.cliente.service.auth.RolSolicitante;
import es.sescam.cliente.service.auth.Solicitante;
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
    public List<CitaDTO> obtenerCitasPaciente(Solicitante solicitante, Long pacienteId) {
        validarSolicitante(solicitante);
        if (pacienteId == null) {
            throw new ApiException(new ApiError("CITA_INVALID_REQUEST", "pacienteId no puede ser nulo", 400));
        }
        if (solicitante.getRol() == RolSolicitante.PACIENTE) {
            // El paciente solo accede a su propia información; en ausencia de mapeo DNI->pacienteId, se deniega por defecto.
            throw new ApiException(new ApiError("FORBIDDEN", "Un paciente solo puede consultar sus propias citas", 403));
        }
        // Implementación mínima: pendiente de llamada HTTP real.
        return Collections.emptyList();
    }

    @Override
    public CitaDTO obtenerCita(Solicitante solicitante, Long citaId) {
        validarSolicitante(solicitante);
        if (citaId == null) {
            throw new ApiException(new ApiError("CITA_INVALID_REQUEST", "citaId no puede ser nulo", 400));
        }
        // Implementación mínima: pendiente de llamada HTTP real. Al no poder verificar pertenencia, denegamos a paciente.
        if (solicitante.getRol() == RolSolicitante.PACIENTE) {
            throw new ApiException(new ApiError("FORBIDDEN", "Un paciente solo puede consultar sus propias citas", 403));
        }
        throw new ApiException(new ApiError("CITA_NOT_FOUND", "No se ha encontrado la cita con id " + citaId, 404));
    }

    @Override
    public CitaDTO asignarCita(Solicitante solicitante, Long citaId, LocalDate fecha, LocalTime hora) {
        validarGestion(solicitante, citaId);
        validarFechaHora(fecha, hora);
        throw pendiente("Asignación");
    }

    @Override
    public CitaDTO reprogramarCita(Solicitante solicitante, Long citaId, LocalDate fecha, LocalTime hora) {
        validarGestion(solicitante, citaId);
        validarFechaHora(fecha, hora);
        throw pendiente("Reprogramación");
    }

    @Override
    public CitaDTO cancelarCita(Solicitante solicitante, Long citaId) {
        validarGestion(solicitante, citaId);
        throw pendiente("Cancelación");
    }

    private void validarGestion(Solicitante solicitante, Long citaId) {
        validarSolicitante(solicitante);
        validarAdministrativo(solicitante);
        if (citaId == null) {
            throw new ApiException(new ApiError("CITA_INVALID_REQUEST", "citaId no puede ser nulo", 400));
        }
    }

    private void validarFechaHora(LocalDate fecha, LocalTime hora) {
        if (fecha == null || hora == null) {
            throw new ApiException(new ApiError("CITA_INVALID_REQUEST", "fecha y hora son obligatorias", 400));
        }
    }

    private ApiException pendiente(String operacion) {
        return new ApiException(new ApiError("CITA_API_PENDIENTE", operacion + " pendiente de integración HTTP", 501));
    }

    private void validarAdministrativo(Solicitante solicitante) {
        if (solicitante.getRol() != RolSolicitante.ADMINISTRADOR) {
            throw new ApiException(new ApiError("FORBIDDEN", "Se requiere rol administrativo para gestionar citas", 403));
        }
    }

    private void validarSolicitante(Solicitante solicitante) {
        if (solicitante == null || solicitante.getDni() == null || solicitante.getDni().isBlank()) {
            throw new ApiException(new ApiError("UNAUTHORIZED", "Debe identificarse mediante un DNI válido", 401));
        }
    }
}
