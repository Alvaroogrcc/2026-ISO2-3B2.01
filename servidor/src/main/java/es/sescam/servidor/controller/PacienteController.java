package es.sescam.servidor.controller;

import java.util.List;
import java.util.Optional;

import es.sescam.servidor.error.AccesoDenegado;
import es.sescam.servidor.error.EntidadNoEncontrada;
import es.sescam.servidor.error.ValidacionIncorrecta;
import es.sescam.servidor.model.Paciente;
import es.sescam.servidor.seguridad.Usuario;

/**
 * Controlador de entrada para la gestión de pacientes (CU-12).
 * <p>
 * No contiene lógica de negocio: recibe la petición, delega en el servicio y
 * devuelve el resultado. El usuario que solicita la operación viaja en cada
 * llamada para que el servicio aplique el control de acceso por rol.
 * </p>
 */
public interface PacienteController {

    /**
     * Da de alta a un paciente.
     *
     * @param usuario usuario que solicita el alta
     * @param paciente datos del paciente
     * @return paciente creado
     * @throws AccesoDenegado si el usuario no tiene el rol requerido
     * @throws ValidacionIncorrecta si los datos no son válidos o están duplicados
     */
    Paciente crearPaciente(Usuario usuario, Paciente paciente);

    /**
     * Obtiene un paciente por su identificador.
     *
     * @param usuario usuario que solicita la consulta
     * @param id identificador del paciente
     * @return paciente encontrado o {@link Optional#empty()} si no existe
     * @throws AccesoDenegado si el usuario no tiene un rol autorizado
     */
    Optional<Paciente> obtenerPaciente(Usuario usuario, Long id);

    /**
     * Lista todos los pacientes.
     *
     * @param usuario usuario que solicita el listado
     * @return lista de pacientes
     * @throws AccesoDenegado si el usuario no tiene un rol autorizado
     */
    List<Paciente> listarPacientes(Usuario usuario);

    /**
     * Modifica los datos de un paciente existente.
     *
     * @param usuario usuario que solicita la modificación
     * @param id identificador del paciente
     * @param datos nuevos datos del paciente
     * @return paciente actualizado
     * @throws AccesoDenegado si el usuario no tiene el rol requerido
     * @throws EntidadNoEncontrada si el paciente no existe
     * @throws ValidacionIncorrecta si los datos no son válidos
     */
    Paciente actualizarPaciente(Usuario usuario, Long id, Paciente datos);

    /**
     * Da de baja lógicamente a un paciente.
     *
     * @param usuario usuario que solicita la baja
     * @param id identificador del paciente
     * @throws AccesoDenegado si el usuario no tiene el rol requerido
     * @throws EntidadNoEncontrada si el paciente no existe
     */
    void darDeBajaPaciente(Usuario usuario, Long id);

    /**
     * Busca un paciente por su DNI.
     *
     * @param usuario usuario que solicita la consulta
     * @param dni DNI del paciente
     * @return paciente encontrado o {@link Optional#empty()} si no existe
     * @throws AccesoDenegado si el usuario no tiene un rol autorizado
     * @throws ValidacionIncorrecta si el DNI está vacío
     */
    Optional<Paciente> buscarPacientePorDni(Usuario usuario, String dni);
}
