package es.sescam.cliente.controller;

import java.util.List;

import es.sescam.cliente.model.CitaDTO;

import es.sescam.cliente.service.auth.Solicitante;

public interface CitaController {

    void consultarMisCitas(Solicitante solicitante, Long pacienteId);

    void consultarDetalleCita(Solicitante solicitante, Long citaId);

    void mostrarCita(CitaDTO cita);

    void mostrarCitas(List<CitaDTO> citas);

    void mostrarMensaje(String mensaje);
}
