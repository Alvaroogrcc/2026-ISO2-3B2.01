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
    public void consultarPaciente(Long pacienteId) {
        try {
            PacienteDTO paciente = pacienteApiClient.obtenerPaciente(pacienteId);
            if (paciente == null) {
                view.mostrarMensaje("No se ha encontrado el paciente solicitado.");
                return;
            }
            view.mostrarPaciente(paciente);
        } catch (ApiException e) {
            view.mostrarMensaje(e.getError().getMessage());
        }
    }

    @Override
    public void listarPacientes() {
        try {
            view.mostrarPacientes(pacienteApiClient.listarPacientes());
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
