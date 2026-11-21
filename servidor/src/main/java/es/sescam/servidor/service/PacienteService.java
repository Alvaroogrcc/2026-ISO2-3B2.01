package es.sescam.servidor.service;

import java.util.List;
import java.util.Optional;

import es.sescam.servidor.error.AccesoDenegado;
import es.sescam.servidor.error.EntidadNoEncontrada;
import es.sescam.servidor.error.ValidacionIncorrecta;
import es.sescam.servidor.model.Paciente;
import es.sescam.servidor.seguridad.Usuario;

/**
 * Contrato de servicio para la gestión de pacientes (CU-12).
 * <p>
 * Todas las operaciones reciben el {@link Usuario} que las solicita para aplicar
 * el control de acceso por rol (RNF-SEG-01). Las lecturas de datos clínicos se
 * auditan con usuario, fecha y acción (RNF-SEG-02). Las altas, modificaciones y
 * bajas se sincronizan con el sistema de admisión.
 * </p>
 * <p>
 * Roles autorizados:
 * </p>
 * <ul>
 *   <li>Altas, modificaciones y bajas: {@code ADMINISTRATIVO}.</li>
 *   <li>Consultas y listados: {@code MEDICO}, {@code ADMINISTRATIVO} o
 *       {@code ADMINISTRADOR_SISTEMA}.</li>
 * </ul>
 */
public interface PacienteService {

    /**
     * Da de alta a un paciente con sus datos demográficos.
     * <p>
     * Precondiciones: el usuario debe tener rol {@code ADMINISTRATIVO}; el nombre,
     * los apellidos y el DNI deben estar informados; la fecha de nacimiento no puede
     * ser futura; el DNI y, si se informa, el número de historia clínica no pueden
     * estar duplicados.
     * </p>
     *
     * @param usuario usuario que solicita el alta
     * @param paciente datos del paciente a crear
     * @return paciente persistido con su identificador asignado
     * @throws AccesoDenegado si el usuario no tiene el rol requerido
     * @throws ValidacionIncorrecta si los datos no son válidos o están duplicados
     */
    Paciente crear(Usuario usuario, Paciente paciente);

    /**
     * Modifica los datos demográficos de un paciente existente.
     * <p>
     * Precondiciones: el usuario debe tener rol {@code ADMINISTRATIVO}; el paciente
     * debe existir y estar activo; los datos deben superar las mismas validaciones
     * que en el alta. El DNI y el número de historia clínica pueden coincidir con
     * los del propio paciente.
     * </p>
     *
     * @param usuario usuario que solicita la modificación
     * @param id identificador del paciente a modificar
     * @param datos nuevos datos del paciente
     * @return paciente actualizado
     * @throws AccesoDenegado si el usuario no tiene el rol requerido
     * @throws EntidadNoEncontrada si el paciente no existe
     * @throws ValidacionIncorrecta si los datos no son válidos, están duplicados o
     *         el paciente está dado de baja
     */
    Paciente actualizar(Usuario usuario, Long id, Paciente datos);

    /**
     * Da de baja lógicamente a un paciente, conservando su trazabilidad.
     * <p>
     * Precondiciones: el usuario debe tener rol {@code ADMINISTRATIVO} y el paciente
     * debe existir. La operación asigna la fecha de baja actual y sincroniza la baja
     * con el sistema de admisión; no elimina el registro.
     * </p>
     *
     * @param usuario usuario que solicita la baja
     * @param id identificador del paciente a dar de baja
     * @throws AccesoDenegado si el usuario no tiene el rol requerido
     * @throws EntidadNoEncontrada si el paciente no existe
     */
    void darDeBaja(Usuario usuario, Long id);

    /**
     * Obtiene un paciente por su identificador interno.
     * <p>
     * Precondiciones: el usuario debe tener rol {@code MEDICO},
     * {@code ADMINISTRATIVO} o {@code ADMINISTRADOR_SISTEMA}. La consulta se audita.
     * </p>
     *
     * @param usuario usuario que solicita la consulta
     * @param id identificador del paciente
     * @return paciente encontrado o {@link Optional#empty()} si no existe
     * @throws AccesoDenegado si el usuario no tiene un rol autorizado
     * @throws ValidacionIncorrecta si el identificador es nulo
     */
    Optional<Paciente> obtenerPorId(Usuario usuario, Long id);

    /**
     * Busca un paciente por su DNI.
     * <p>
     * Precondiciones: el usuario debe tener rol {@code MEDICO},
     * {@code ADMINISTRATIVO} o {@code ADMINISTRADOR_SISTEMA}. La consulta se audita.
     * </p>
     *
     * @param usuario usuario que solicita la consulta
     * @param dni DNI del paciente
     * @return paciente encontrado o {@link Optional#empty()} si no existe
     * @throws AccesoDenegado si el usuario no tiene un rol autorizado
     * @throws ValidacionIncorrecta si el DNI está vacío
     */
    Optional<Paciente> buscarPorDni(Usuario usuario, String dni);

    /**
     * Lista todos los pacientes, incluidos los dados de baja.
     * <p>
     * Precondiciones: el usuario debe tener rol {@code MEDICO},
     * {@code ADMINISTRATIVO} o {@code ADMINISTRADOR_SISTEMA}. El acceso se audita.
     * </p>
     *
     * @param usuario usuario que solicita el listado
     * @return lista de pacientes
     * @throws AccesoDenegado si el usuario no tiene un rol autorizado
     */
    List<Paciente> listar(Usuario usuario);
}
