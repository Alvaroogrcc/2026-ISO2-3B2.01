package es.sescam.cliente.controller;

import java.util.List;
import java.util.Objects;

import es.sescam.cliente.model.PacienteDTO;
import es.sescam.cliente.service.PacienteApiClient;
import es.sescam.cliente.service.auth.Solicitante;
import es.sescam.cliente.service.error.ApiError;
import es.sescam.cliente.service.error.ApiException;
import es.sescam.cliente.view.PacienteView;

/**
 * Implementación del controlador de presentación de pacientes.
 * <p>
 * Captura los errores de la API y los comunica a la vista mediante
 * {@link PacienteView#mostrarError(String)}. Las operaciones correctas se
 * confirman con {@link PacienteView#mostrarConfirmacion(String)}.
 * </p>
 */
public class PacienteControllerImpl implements PacienteController {

    private final PacienteApiClient pacienteApiClient;
    private final PacienteView view;

    /**
     * Crea el controlador con el cliente de la API y la vista.
     *
     * @param pacienteApiClient cliente de la API de pacientes
     * @param view vista de pacientes
     */
    public PacienteControllerImpl(PacienteApiClient pacienteApiClient, PacienteView view) {
        this.pacienteApiClient = Objects.requireNonNull(pacienteApiClient, "pacienteApiClient");
        this.view = Objects.requireNonNull(view, "view");
    }

    @Override
    public void consultarPaciente(Solicitante solicitante, Long pacienteId) {
        try {
            PacienteDTO paciente = pacienteApiClient.obtenerPaciente(solicitante, pacienteId);
            if (paciente == null) {
                throw noEncontrado(pacienteId);
            }
            view.mostrarPaciente(paciente);
        } catch (ApiException e) {
            view.mostrarMensaje(e.getError().getMessage());
        }
    }

    @Override
    public void listarPacientes(Solicitante solicitante) {
        try {
            view.mostrarPacientes(pacienteApiClient.listarPacientes(solicitante));
        } catch (ApiException e) {
            view.mostrarMensaje(e.getError().getMessage());
        }
    }

    @Override
    public void crearPaciente(Solicitante solicitante, PacienteDTO paciente) {
        try {
            PacienteDTO creado = pacienteApiClient.crearPaciente(solicitante, paciente);
            if (creado != null) {
                view.mostrarPaciente(creado);
            }
            view.mostrarConfirmacion("Paciente dado de alta correctamente");
        } catch (ApiException e) {
            view.mostrarError(e.getError().getMessage());
        }
    }

    @Override
    public void actualizarPaciente(Solicitante solicitante, Long id, PacienteDTO datos) {
        try {
            PacienteDTO actualizado = pacienteApiClient.actualizarPaciente(solicitante, id, datos);
            if (actualizado != null) {
                view.mostrarPaciente(actualizado);
            }
            view.mostrarConfirmacion("Paciente modificado correctamente");
        } catch (ApiException e) {
            view.mostrarError(e.getError().getMessage());
        }
    }

    @Override
    public void darDeBajaPaciente(Solicitante solicitante, Long id) {
        try {
            pacienteApiClient.darDeBajaPaciente(solicitante, id);
            view.mostrarConfirmacion("Paciente dado de baja correctamente");
        } catch (ApiException e) {
            view.mostrarError(e.getError().getMessage());
        }
    }

    @Override
    public void buscarPorDni(Solicitante solicitante, String dni) {
        try {
            PacienteDTO paciente = pacienteApiClient.buscarPacientePorDni(solicitante, dni);
            if (paciente == null) {
                throw new ApiException(new ApiError("PACIENTE_NOT_FOUND",
                        "No se ha encontrado el paciente con el DNI indicado", 404));
            }
            view.mostrarPaciente(paciente);
        } catch (ApiException e) {
            view.mostrarError(e.getError().getMessage());
        }
    }

    @Override
    public void mostrarPaciente(PacienteDTO paciente) {
        view.mostrarPaciente(paciente);
    }

    @Override
    public void mostrarPacientes(List<PacienteDTO> pacientes) {
        view.mostrarPacientes(pacientes);
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        view.mostrarMensaje(mensaje);
    }

    private ApiException noEncontrado(Long pacienteId) {
        return new ApiException(new ApiError("PACIENTE_NOT_FOUND",
                "No se ha encontrado el paciente con id " + pacienteId, 404));
    }
}
