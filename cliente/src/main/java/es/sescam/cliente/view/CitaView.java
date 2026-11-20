package es.sescam.cliente.view;

import java.util.List;

import es.sescam.cliente.model.CitaDTO;

public interface CitaView {

    void mostrarCita(CitaDTO cita);

    void mostrarCitas(List<CitaDTO> citas);

    void mostrarMensaje(String mensaje);
}
