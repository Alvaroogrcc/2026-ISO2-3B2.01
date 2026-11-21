package es.sescam.cliente.controller;

import java.util.List;

import es.sescam.cliente.model.PacienteDTO;

import es.sescam.cliente.service.auth.Solicitante;

public interface PacienteController {

    void consultarPaciente(Solicitante solicitante, Long pacienteId);

    void listarPacientes(Solicitante solicitante);

    void mostrarPaciente(PacienteDTO paciente);

    void mostrarPacientes(List<PacienteDTO> pacientes);

    void mostrarMensaje(String mensaje);
}
