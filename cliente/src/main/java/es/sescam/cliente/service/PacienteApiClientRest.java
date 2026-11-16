package es.sescam.cliente.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import es.sescam.cliente.model.PacienteDTO;
import es.sescam.cliente.service.auth.RolSolicitante;
import es.sescam.cliente.service.auth.Solicitante;
import es.sescam.cliente.service.error.ApiError;
import es.sescam.cliente.service.error.ApiException;

/**
 * Implementación de contrato del cliente de pacientes.
 * <p>
 * En esta iteración no se realizan llamadas HTTP reales: se aplican las
 * validaciones del contrato y se devuelven errores coherentes ({@code 400},
 * {@code 401}, {@code 403} o {@code 404}). La sustitución por un cliente HTTP
 * real se hará cuando exista el borde REST del servidor.
 * </p>
 */
public class PacienteApiClientRest implements PacienteApiClient {

    private final String baseUrl;

    /**
     * Crea el cliente con la URL base de la API.
     *
     * @param baseUrl URL base de la API
     */
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

    @Override
    public PacienteDTO crearPaciente(Solicitante solicitante, PacienteDTO paciente) {
        validarSolicitante(solicitante);
        exigirAdministrador(solicitante);
        validarDatosPaciente(paciente);
        // Implementación mínima sin red: se acepta el alta y se devuelve una copia de los datos.
        return copiar(paciente);
    }

    @Override
    public PacienteDTO actualizarPaciente(Solicitante solicitante, Long id, PacienteDTO datos) {
        validarSolicitante(solicitante);
        exigirAdministrador(solicitante);
        if (id == null) {
            throw new ApiException(new ApiError("PACIENTE_INVALID_REQUEST", "El id del paciente no puede ser nulo", 400));
        }
        validarDatosPaciente(datos);
        throw new ApiException(new ApiError("PACIENTE_NOT_FOUND", "No se ha encontrado el paciente con id " + id, 404));
    }

    @Override
    public void darDeBajaPaciente(Solicitante solicitante, Long id) {
        validarSolicitante(solicitante);
        exigirAdministrador(solicitante);
        if (id == null) {
            throw new ApiException(new ApiError("PACIENTE_INVALID_REQUEST", "El id del paciente no puede ser nulo", 400));
        }
        throw new ApiException(new ApiError("PACIENTE_NOT_FOUND", "No se ha encontrado el paciente con id " + id, 404));
    }

    @Override
    public PacienteDTO buscarPacientePorDni(Solicitante solicitante, String dni) {
        validarSolicitante(solicitante);
        if (solicitante.getRol() == RolSolicitante.PACIENTE) {
            throw new ApiException(new ApiError("FORBIDDEN", "Un paciente solo puede consultar su propia información", 403));
        }
        if (dni == null || dni.isBlank()) {
            throw new ApiException(new ApiError("PACIENTE_INVALID_REQUEST", "El DNI del paciente no puede estar vacío", 400));
        }
        throw new ApiException(new ApiError("PACIENTE_NOT_FOUND", "No se ha encontrado el paciente con el DNI indicado", 404));
    }

    private void validarSolicitante(Solicitante solicitante) {
        if (solicitante == null || solicitante.getDni() == null || solicitante.getDni().isBlank()) {
            throw new ApiException(new ApiError("UNAUTHORIZED", "Debe identificarse mediante un DNI válido", 401));
        }
    }

    private void exigirAdministrador(Solicitante solicitante) {
        if (solicitante.getRol() != RolSolicitante.ADMINISTRADOR) {
            throw new ApiException(new ApiError("FORBIDDEN", "Solo los administradores pueden gestionar pacientes", 403));
        }
    }

    private void validarDatosPaciente(PacienteDTO paciente) {
        if (paciente == null) {
            throw new ApiException(new ApiError("PACIENTE_INVALID_REQUEST", "Los datos del paciente son obligatorios", 400));
        }
        if (estaVacio(paciente.getNombre()) || estaVacio(paciente.getApellidos()) || estaVacio(paciente.getDni())) {
            throw new ApiException(new ApiError("PACIENTE_INVALID_REQUEST", "Nombre, apellidos y DNI son obligatorios", 400));
        }
    }

    private PacienteDTO copiar(PacienteDTO origen) {
        PacienteDTO copia = new PacienteDTO();
        copia.setId(origen.getId());
        copia.setNombre(origen.getNombre());
        copia.setApellidos(origen.getApellidos());
        copia.setDni(origen.getDni());
        copia.setFechaNacimiento(origen.getFechaNacimiento());
        copia.setTelefono(origen.getTelefono());
        copia.setEmail(origen.getEmail());
        copia.setDireccion(origen.getDireccion());
        copia.setNumeroHistoriaClinica(origen.getNumeroHistoriaClinica());
        copia.setFechaBaja(origen.getFechaBaja());
        return copia;
    }

    private boolean estaVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}
