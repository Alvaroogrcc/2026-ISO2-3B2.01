package es.sescam.servidor.service;

import java.util.List;
import java.util.Optional;

import es.sescam.servidor.error.AccesoDenegado;
import es.sescam.servidor.error.EntidadNoEncontrada;
import es.sescam.servidor.error.ValidacionIncorrecta;
import es.sescam.servidor.model.ImagenMedica;
import es.sescam.servidor.model.InformeClinico;
import es.sescam.servidor.seguridad.Usuario;

/**
 * Contrato de servicio para la gestión de informes clínicos e imágenes médicas
 * (CU-09).
 * <p>
 * Todas las operaciones reciben el {@link Usuario} que las solicita para aplicar
 * el control de acceso por rol (RNF-SEG-01) y comprobar la relación clínica con
 * el paciente. Las lecturas y escrituras de datos clínicos se auditan con
 * usuario, fecha y acción (RNF-SEG-02).
 * </p>
 * <p>
 * Roles autorizados:
 * </p>
 * <ul>
 *   <li>Creación, modificación, firma y consulta de imágenes: {@code MEDICO} con
 *       relación clínica autorizada.</li>
 *   <li>Consulta de informes: {@code MEDICO} con relación clínica autorizada o
 *       {@code ADMINISTRADOR_SISTEMA}.</li>
 * </ul>
 */
public interface InformeClinicoService {

    /**
     * Crea un informe clínico en estado {@code BORRADOR} asociado a una solicitud
     * de prueba existente.
     * <p>
     * Precondiciones: el usuario debe tener rol {@code MEDICO}; la solicitud debe
     * existir; el usuario debe tener relación clínica autorizada con el paciente de
     * la solicitud; y el contenido debe estar informado.
     * </p>
     * <p>
     * El paciente y el médico se toman de la solicitud, la fecha se asigna al
     * momento de la creación y la operación se audita como {@code ALTA}.
     * </p>
     *
     * @param usuario usuario que crea el informe
     * @param informe datos del informe; la solicitud se resuelve por su
     *        identificador
     * @return informe persistido con su identificador y estado asignados
     * @throws AccesoDenegado si el usuario no tiene el rol requerido o no mantiene
     *         relación clínica con el paciente
     * @throws ValidacionIncorrecta si los datos o el contenido no son válidos
     * @throws EntidadNoEncontrada si la solicitud indicada no existe
     */
    InformeClinico crear(Usuario usuario, InformeClinico informe);

    /**
     * Modifica el contenido y la conclusión de un informe en estado
     * {@code BORRADOR}.
     * <p>
     * Precondiciones: el usuario debe tener rol {@code MEDICO}; el informe debe
     * existir; no puede estar firmado; el usuario debe tener relación clínica
     * autorizada con el paciente; y el contenido debe estar informado. La operación
     * se audita como {@code MODIFICACION}.
     * </p>
     *
     * @param usuario usuario que modifica el informe
     * @param id identificador del informe
     * @param datos nuevos datos del informe
     * @return informe actualizado
     * @throws AccesoDenegado si el usuario no tiene el rol requerido o no mantiene
     *         relación clínica con el paciente
     * @throws ValidacionIncorrecta si el informe está firmado o los datos no son
     *         válidos
     * @throws EntidadNoEncontrada si el informe no existe
     */
    InformeClinico actualizar(Usuario usuario, Long id, InformeClinico datos);

    /**
     * Firma un informe clínico en estado {@code BORRADOR} y lo envía a la HCE.
     * <p>
     * Precondiciones: el usuario debe tener rol {@code MEDICO}; el informe debe
     * existir; debe estar en estado {@code BORRADOR}; y el usuario debe tener
     * relación clínica autorizada con el paciente. Tras firmarlo, el informe pasa a
     * {@code FIRMADO}, se audita como {@code FIRMA} y se envía a la historia clínica
     * electrónica.
     * </p>
     *
     * @param usuario usuario que firma el informe
     * @param id identificador del informe
     * @return informe firmado
     * @throws AccesoDenegado si el usuario no tiene el rol requerido o no mantiene
     *         relación clínica con el paciente
     * @throws ValidacionIncorrecta si el informe ya está firmado
     * @throws EntidadNoEncontrada si el informe no existe
     */
    InformeClinico firmar(Usuario usuario, Long id);

    /**
     * Obtiene un informe por su identificador interno.
     * <p>
     * Precondiciones: el usuario debe tener rol {@code ADMINISTRADOR_SISTEMA} o
     * {@code MEDICO} con relación clínica autorizada con el paciente. La consulta
     * se audita como {@code LECTURA}.
     * </p>
     *
     * @param usuario usuario que solicita la consulta
     * @param id identificador del informe
     * @return informe encontrado o {@link Optional#empty()} si no existe
     * @throws AccesoDenegado si el usuario no tiene un rol autorizado o no mantiene
     *         relación clínica con el paciente
     * @throws ValidacionIncorrecta si el identificador es nulo
     */
    Optional<InformeClinico> obtenerPorId(Usuario usuario, Long id);

    /**
     * Lista los informes clínicos de un paciente.
     * <p>
     * Precondiciones: el usuario debe tener rol {@code ADMINISTRADOR_SISTEMA} o
     * {@code MEDICO} con relación clínica autorizada con el paciente. El acceso se
     * audita como {@code LECTURA}.
     * </p>
     *
     * @param usuario usuario que solicita el listado
     * @param pacienteId identificador del paciente
     * @return informes del paciente
     * @throws AccesoDenegado si el usuario no tiene un rol autorizado o no mantiene
     *         relación clínica con el paciente
     * @throws ValidacionIncorrecta si el identificador del paciente es nulo
     */
    List<InformeClinico> listarPorPaciente(Usuario usuario, Long pacienteId);

    /**
     * Lista las imágenes médicas asociadas a una solicitud de prueba.
     * <p>
     * Precondiciones: el usuario debe tener rol {@code MEDICO} y relación clínica
     * autorizada con el paciente de la solicitud. El acceso se audita como
     * {@code LECTURA_IMAGENES} y se delega en el servicio de imágenes con el
     * identificador de estudio {@code EST-<solicitudId>}.
     * </p>
     *
     * @param usuario usuario que solicita las imágenes
     * @param solicitudId identificador de la solicitud de prueba
     * @return imágenes médicas asociadas al estudio
     * @throws AccesoDenegado si el usuario no tiene el rol requerido o no mantiene
     *         relación clínica con el paciente
     * @throws ValidacionIncorrecta si el identificador de la solicitud es nulo
     * @throws EntidadNoEncontrada si la solicitud no existe
     */
    List<ImagenMedica> listarImagenes(Usuario usuario, Long solicitudId);
}
