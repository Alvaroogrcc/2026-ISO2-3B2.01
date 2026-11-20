package es.sescam.cliente.controller;

import java.util.List;
import java.util.Objects;

import es.sescam.cliente.model.PacienteDTO;
import es.sescam.cliente.service.ApiClient;
import es.sescam.cliente.view.PacienteView;

public class PacienteControllerImpl implements PacienteController {

    private final ApiClient apiClient;
    private final PacienteView view;

    public PacienteControllerImpl(ApiClient apiClient, PacienteView view) {
        this.apiClient = Objects.requireNonNull(apiClient, "apiClient");
        this.view = Objects.requireNonNull(view, "view");
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
