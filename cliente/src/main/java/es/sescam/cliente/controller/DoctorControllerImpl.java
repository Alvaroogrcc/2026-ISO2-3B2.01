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
    public void consultarDoctor(es.sescam.cliente.service.auth.Solicitante solicitante, Long doctorId) {
        try {
            DoctorDTO doctor = doctorApiClient.obtenerDoctor(solicitante, doctorId);
            if (doctor == null) {
                throw new ApiException(new es.sescam.cliente.service.error.ApiError("DOCTOR_NOT_FOUND", "No se ha encontrado el doctor con id " + doctorId, 404));
            }
            view.mostrarDoctor(doctor);
        } catch (ApiException e) {
            view.mostrarMensaje(e.getError().getMessage());
        }
    }

    @Override
    public void listarDoctores(es.sescam.cliente.service.auth.Solicitante solicitante) {
        try {
            view.mostrarDoctores(doctorApiClient.listarDoctores(solicitante));
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
