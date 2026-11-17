package es.sescam.cliente.controller;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import es.sescam.cliente.model.CitaDTO;
import es.sescam.cliente.view.CitaView;

public class CitaControllerImpl implements CitaController {

    private final CitaView view;

    public CitaControllerImpl(CitaView view) {
        this.view = Objects.requireNonNull(view, "view");
    }

    @Override
    public void consultarMisCitas(List<CitaDTO> citas) {
        if (citas == null || citas.isEmpty()) {
            view.mostrarMensaje("No hay citas para mostrar.");
            view.mostrarCitas(Collections.emptyList());
            return;
        }
        view.mostrarCitas(citas);
    }

    @Override
    public void consultarDetalleCita(CitaDTO cita) {
        if (cita == null) {
            view.mostrarMensaje("No se encontró la cita solicitada.");
            return;
        }
        view.mostrarCita(cita);
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
