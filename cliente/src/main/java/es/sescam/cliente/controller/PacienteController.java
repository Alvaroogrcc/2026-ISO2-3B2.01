package es.sescam.cliente.controller;

import java.util.List;

import es.sescam.cliente.model.PacienteDTO;

public interface PacienteController {

    void consultarPaciente(Long pacienteId);

    void listarPacientes();

    void mostrarPaciente(PacienteDTO paciente);

    void mostrarPacientes(List<PacienteDTO> pacientes);

    void mostrarMensaje(String mensaje);
}
