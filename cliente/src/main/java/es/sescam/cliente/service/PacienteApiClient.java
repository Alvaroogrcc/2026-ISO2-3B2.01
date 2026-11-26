package es.sescam.cliente.service;

import java.util.List;

import es.sescam.cliente.model.PacienteDTO;
import es.sescam.cliente.service.auth.Solicitante;
import es.sescam.cliente.service.error.ApiException;

/**
 * Contrato API específico para la entidad Paciente.
 * <p>
 * El solicitante se identifica por DNI único para controlar el acceso. Las
 * operaciones de escritura (alta, modificación y baja) solo están permitidas al
 * rol {@code ADMINISTRADOR}; las lecturas están disponibles para los roles
 * autorizados por el servidor.
 * </p>
 */
public interface PacienteApiClient {

    /**
     * Devuelve la URL base de la API.
     *
     * @return URL base configurada
     */
    String getBaseUrl();

    /**
     * Obtiene un paciente por su identificador.
     *
     * @param solicitante usuario que realiza la petición
     * @param pacienteId identificador del paciente
     * @return paciente encontrado
     * @throws ApiException si la petición no es válida, el acceso está prohibido
     *         o el paciente no existe
     */
    PacienteDTO obtenerPaciente(Solicitante solicitante, Long pacienteId);

    /**
     * Lista los pacientes.
     * <p>
     * Solo los roles {@code DOCTOR} o {@code ADMINISTRADOR} pueden listar pacientes.
     * </p>
     *
     * @param solicitante usuario que realiza la petición
     * @return lista de pacientes
     * @throws ApiException si el solicitante no está autorizado
     */
    List<PacienteDTO> listarPacientes(Solicitante solicitante);

    /**
     * Da de alta a un paciente.
     * <p>
     * Solo el rol {@code ADMINISTRADOR} puede realizar esta operación.
     * </p>
     *
     * @param solicitante usuario que realiza la petición
     * @param paciente datos del paciente
     * @return paciente creado
     * @throws ApiException si la petición no es válida, el acceso está prohibido
     *         o los datos del paciente son obligatorios
     */
    PacienteDTO crearPaciente(Solicitante solicitante, PacienteDTO paciente);

    /**
     * Modifica los datos de un paciente existente.
     * <p>
     * Solo el rol {@code ADMINISTRADOR} puede realizar esta operación.
     * </p>
     *
     * @param solicitante usuario que realiza la petición
     * @param id identificador del paciente
     * @param datos nuevos datos del paciente
     * @return paciente actualizado
     * @throws ApiException si la petición no es válida, el acceso está prohibido
     *         o el paciente no existe
     */
    PacienteDTO actualizarPaciente(Solicitante solicitante, Long id, PacienteDTO datos);

    /**
     * Da de baja lógicamente a un paciente.
     * <p>
     * Solo el rol {@code ADMINISTRADOR} puede realizar esta operación.
     * </p>
     *
     * @param solicitante usuario que realiza la petición
     * @param id identificador del paciente
     * @throws ApiException si la petición no es válida, el acceso está prohibido
     *         o el paciente no existe
     */
    void darDeBajaPaciente(Solicitante solicitante, Long id);

    /**
     * Busca un paciente por su DNI.
     *
     * @param solicitante usuario que realiza la petición
     * @param dni DNI del paciente
     * @return paciente encontrado
     * @throws ApiException si la petición no es válida, el acceso está prohibido
     *         o el paciente no existe
     */
    PacienteDTO buscarPacientePorDni(Solicitante solicitante, String dni);
}
