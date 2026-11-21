package es.sescam.cliente.controller;

import java.util.List;
import java.util.Objects;

import es.sescam.cliente.model.DoctorDTO;
import es.sescam.cliente.service.DoctorApiClient;
import es.sescam.cliente.service.error.ApiException;
import es.sescam.cliente.view.DoctorView;

public class DoctorControllerImpl implements DoctorController {

    private final DoctorApiClient doctorApiClient;
    private final DoctorView view;

    public DoctorControllerImpl(DoctorApiClient doctorApiClient, DoctorView view) {
        this.doctorApiClient = Objects.requireNonNull(doctorApiClient, "doctorApiClient");
        this.view = Objects.requireNonNull(view, "view");
    }

    @Override
    public void consultarDoctor(Long doctorId) {
        try {
            DoctorDTO doctor = doctorApiClient.obtenerDoctor(doctorId);
            if (doctor == null) {
                view.mostrarMensaje("No se ha encontrado el doctor solicitado.");
                return;
            }
            view.mostrarDoctor(doctor);
        } catch (ApiException e) {
            view.mostrarMensaje(e.getError().getMessage());
        }
    }

    @Override
    public void listarDoctores() {
        try {
            view.mostrarDoctores(doctorApiClient.listarDoctores());
        } catch (ApiException e) {
            view.mostrarMensaje(e.getError().getMessage());
        }
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
