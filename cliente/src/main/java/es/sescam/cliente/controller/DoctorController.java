package es.sescam.cliente.controller;

import java.util.List;

import es.sescam.cliente.model.DoctorDTO;

import es.sescam.cliente.service.auth.Solicitante;

public interface DoctorController {

    void consultarDoctor(Solicitante solicitante, Long doctorId);

    void listarDoctores(Solicitante solicitante);

    void mostrarDoctor(DoctorDTO doctor);

    void mostrarDoctores(List<DoctorDTO> doctores);

    void mostrarMensaje(String mensaje);
}
