package es.sescam.cliente.view;

import java.util.List;

import es.sescam.cliente.model.PacienteDTO;

public interface PacienteView {

    void mostrarPaciente(PacienteDTO paciente);

    void mostrarPacientes(List<PacienteDTO> pacientes);

    void mostrarMensaje(String mensaje);
}
