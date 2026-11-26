package es.sescam.cliente.controller;

import java.util.List;

import es.sescam.cliente.model.PacienteDTO;
import es.sescam.cliente.service.auth.Solicitante;

/**
 * Controlador de presentación para la gestión de pacientes (CU-12).
 * <p>
 * Traduce las acciones de la vista en llamadas al cliente de la API y comunica
 * los resultados a la vista. No contiene reglas de negocio ni accede al modelo
 * del servidor.
 * </p>
 */
public interface PacienteController {

    /**
     * Consulta un paciente por su identificador y lo muestra en la vista.
     *
     * @param solicitante usuario que realiza la petición
     * @param pacienteId identificador del paciente
     */
    void consultarPaciente(Solicitante solicitante, Long pacienteId);

    /**
     * Solicita el listado de pacientes y lo muestra en la vista.
     *
     * @param solicitante usuario que realiza la petición
     */
    void listarPacientes(Solicitante solicitante);

    /**
     * Da de alta a un paciente.
     *
     * @param solicitante usuario que realiza la petición
     * @param paciente datos del paciente
     */
    void crearPaciente(Solicitante solicitante, PacienteDTO paciente);

    /**
     * Modifica los datos de un paciente existente.
     *
     * @param solicitante usuario que realiza la petición
     * @param id identificador del paciente
     * @param datos nuevos datos del paciente
     */
    void actualizarPaciente(Solicitante solicitante, Long id, PacienteDTO datos);

    /**
     * Da de baja lógicamente a un paciente.
     *
     * @param solicitante usuario que realiza la petición
     * @param id identificador del paciente
     */
    void darDeBajaPaciente(Solicitante solicitante, Long id);

    /**
     * Busca un paciente por su DNI y lo muestra en la vista.
     *
     * @param solicitante usuario que realiza la petición
     * @param dni DNI del paciente
     */
    void buscarPorDni(Solicitante solicitante, String dni);

    /**
     * Muestra un paciente en la vista.
     *
     * @param paciente paciente a mostrar
     */
    void mostrarPaciente(PacienteDTO paciente);

    /**
     * Muestra una lista de pacientes en la vista.
     *
     * @param pacientes pacientes a mostrar
     */
    void mostrarPacientes(List<PacienteDTO> pacientes);

    /**
     * Muestra un mensaje informativo en la vista.
     *
     * @param mensaje texto a mostrar
     */
    void mostrarMensaje(String mensaje);
}
