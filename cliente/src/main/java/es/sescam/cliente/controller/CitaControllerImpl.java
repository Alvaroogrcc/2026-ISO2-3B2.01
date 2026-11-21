package es.sescam.cliente.controller;

import java.util.List;
import java.util.Objects;

import es.sescam.cliente.model.CitaDTO;
import es.sescam.cliente.service.CitaApiClient;
import es.sescam.cliente.service.error.ApiException;
import es.sescam.cliente.view.CitaView;

public class CitaControllerImpl implements CitaController {

    private final CitaApiClient citaApiClient;
    private final CitaView view;

    public CitaControllerImpl(CitaApiClient citaApiClient, CitaView view) {
        this.citaApiClient = Objects.requireNonNull(citaApiClient, "citaApiClient");
        this.view = Objects.requireNonNull(view, "view");
    }

    @Override
    public void consultarMisCitas(Long pacienteId) {
        try {
            List<CitaDTO> citas = citaApiClient.obtenerCitasPaciente(pacienteId);
            view.mostrarCitas(citas);
        } catch (ApiException e) {
            view.mostrarMensaje(e.getError().getMessage());
        }
    }

    @Override
    public void consultarDetalleCita(Long citaId) {
        try {
            CitaDTO cita = citaApiClient.obtenerCita(citaId);
            if (cita == null) {
                view.mostrarMensaje("No se ha encontrado la cita solicitada.");
                return;
            }
            view.mostrarCita(cita);
        } catch (ApiException e) {
            view.mostrarMensaje(e.getError().getMessage());
        }
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
