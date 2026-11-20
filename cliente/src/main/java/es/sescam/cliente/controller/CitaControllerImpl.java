package es.sescam.cliente.controller;

import java.util.List;
import java.util.Objects;

import es.sescam.cliente.model.CitaDTO;
import es.sescam.cliente.service.ApiClient;
import es.sescam.cliente.view.CitaView;

public class CitaControllerImpl implements CitaController {

    private final ApiClient apiClient;
    private final CitaView view;

    public CitaControllerImpl(ApiClient apiClient, CitaView view) {
        this.apiClient = Objects.requireNonNull(apiClient, "apiClient");
        this.view = Objects.requireNonNull(view, "view");
    }

    @Override
    public void mostrarCita(CitaDTO cita) {
        view.mostrarCita(cita);
    }

    @Override
    public void mostrarCitas(List<CitaDTO> citas) {
        view.mostrarCitas(citas);
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        view.mostrarMensaje(mensaje);
    }
}
