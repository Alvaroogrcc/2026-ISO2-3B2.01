package es.sescam.cliente.controller;

import java.util.List;

import es.sescam.cliente.model.DoctorDTO;

public interface DoctorController {

    void mostrarDoctor(DoctorDTO doctor);

    void mostrarDoctores(List<DoctorDTO> doctores);

    void mostrarMensaje(String mensaje);
}
