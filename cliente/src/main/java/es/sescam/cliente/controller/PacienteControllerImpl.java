package es.sescam.cliente.controller;

import java.util.List;
import java.util.Objects;

import es.sescam.cliente.model.PacienteDTO;
import es.sescam.cliente.service.PacienteApiClient;
import es.sescam.cliente.service.error.ApiException;
import es.sescam.cliente.view.PacienteView;

public class PacienteControllerImpl implements PacienteController {

    private final PacienteApiClient pacienteApiClient;
    private final PacienteView view;

    public PacienteControllerImpl(PacienteApiClient pacienteApiClient, PacienteView view) {
        this.pacienteApiClient = Objects.requireNonNull(pacienteApiClient, "pacienteApiClient");
        this.view = Objects.requireNonNull(view, "view");
    }

    @Override
    public void consultarPaciente(es.sescam.cliente.service.auth.Solicitante solicitante, Long pacienteId) {
        try {
            PacienteDTO paciente = pacienteApiClient.obtenerPaciente(solicitante, pacienteId);
            if (paciente == null) {
                throw new ApiException(new es.sescam.cliente.service.error.ApiError("PACIENTE_NOT_FOUND", "No se ha encontrado el paciente con id " + pacienteId, 404));
            }
            view.mostrarPaciente(paciente);
        } catch (ApiException e) {
            view.mostrarMensaje(e.getError().getMessage());
        }
    }

    @Override
    public void listarPacientes(es.sescam.cliente.service.auth.Solicitante solicitante) {
        try {
            view.mostrarPacientes(pacienteApiClient.listarPacientes(solicitante));
        } catch (ApiException e) {
            view.mostrarMensaje(e.getError().getMessage());
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
}
