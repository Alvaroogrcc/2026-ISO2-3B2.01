package es.sescam.cliente.view;

import java.util.List;

import es.sescam.cliente.model.DoctorDTO;

public interface DoctorView {

    void mostrarDoctor(DoctorDTO doctor);

    void mostrarDoctores(List<DoctorDTO> doctores);

    void mostrarMensaje(String mensaje);
}
