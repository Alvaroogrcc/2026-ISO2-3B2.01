package es.sescam.cliente.controller;

import java.util.List;
import java.util.Objects;

import es.sescam.cliente.model.DoctorDTO;
import es.sescam.cliente.service.ApiClient;
import es.sescam.cliente.view.DoctorView;

public class DoctorControllerImpl implements DoctorController {

    private final ApiClient apiClient;
    private final DoctorView view;

    public DoctorControllerImpl(ApiClient apiClient, DoctorView view) {
        this.apiClient = Objects.requireNonNull(apiClient, "apiClient");
        this.view = Objects.requireNonNull(view, "view");
    }

    @Override
    public void mostrarDoctor(DoctorDTO doctor) {
        view.mostrarDoctor(doctor);
    }

    @Override
    public void mostrarDoctores(List<DoctorDTO> doctores) {
        view.mostrarDoctores(doctores);
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        view.mostrarMensaje(mensaje);
    }
}
