package es.sescam.cliente.controller;

import java.util.List;

import es.sescam.cliente.model.CitaDTO;

public interface CitaController {

    void mostrarCita(CitaDTO cita);

    void mostrarCitas(List<CitaDTO> citas);

    void mostrarMensaje(String mensaje);
}
