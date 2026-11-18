package es.sescam.servidor.service;

import java.util.List;
import java.util.Optional;

import es.sescam.servidor.error.AccesoDenegado;
import es.sescam.servidor.error.EntidadNoEncontrada;
import es.sescam.servidor.error.ValidacionIncorrecta;
import es.sescam.servidor.model.SolicitudPrueba;
import es.sescam.servidor.model.TipoPrueba;
import es.sescam.servidor.seguridad.Usuario;

/**
 * Contrato de servicio para la prescripción de pruebas diagnósticas (CU-06).
 * <p>
 * Todas las operaciones reciben el {@link Usuario} que las solicita para aplicar
 * el control de acceso por rol (RNF-SEG-01). Las lecturas y escrituras de datos
 * clínicos se auditan con usuario, fecha y acción (RNF-SEG-02).
 * </p>
 * <p>
 * Al prescribir se aplican los atributos del catálogo de pruebas (preparación
 * previa, uso de contraste y consentimiento informado) y se registra la versión
 * del catálogo y el usuario que los validó (RNF-FUN-03). La solicitud se envía a
 * la historia clínica electrónica simulada.
 * </p>
 * <p>
 * Roles autorizados:
 * </p>
 * <ul>
 *   <li>Prescripción: {@code MEDICO}.</li>
 *   <li>Listado de pendientes: {@code MEDICO}, {@code ADMINISTRATIVO} o
 *       {@code ADMINISTRADOR_SISTEMA}.</li>
 *   <li>Consulta por identificador y listado del catálogo: {@code MEDICO},
 *       {@code ADMINISTRATIVO} o {@code ADMINISTRADOR_SISTEMA}.</li>
 * </ul>
 */
public interface SolicitudPruebaService {

    /**
     * Prescribe una prueba diagnóstica para un paciente.
     * <p>
     * Precondiciones: el usuario debe tener rol {@code MEDICO}; la prioridad debe
     * ser {@code URGENTE}, {@code PREFERENTE} u {@code ORDINARIA}; la indicación
     * clínica y la justificación deben estar informadas; el paciente debe existir
     * y estar activo; el médico debe existir; el tipo de prueba debe existir en el
     * catálogo; y si el catálogo exige consentimiento informado, debe constar su
     * aceptación.
     * </p>
     * <p>
     * La solicitud se registra en estado {@code PENDIENTE} con la fecha actual, el
     * médico prescriptor y los atributos del catálogo aplicados. Después se audita
     * y se envía a la historia clínica electrónica.
     * </p>
     *
     * @param usuario usuario que prescribe la prueba
     * @param solicitud datos de la solicitud; el paciente, el médico y el tipo de
     *        prueba se resuelven por su identificador
     * @return solicitud persistida con su identificador y estado asignados
     * @throws AccesoDenegado si el usuario no tiene el rol {@code MEDICO}
     * @throws ValidacionIncorrecta si los datos no son válidos, el paciente no está
     *         activo o falta el consentimiento informado exigido
     * @throws EntidadNoEncontrada si el paciente, el médico o el tipo de prueba no
     *         existen
     */
    SolicitudPrueba prescribir(Usuario usuario, SolicitudPrueba solicitud);

    /**
     * Lista las solicitudes pendientes de citación.
     * <p>
     * Precondiciones: el usuario debe tener rol {@code MEDICO},
     * {@code ADMINISTRATIVO} o {@code ADMINISTRADOR_SISTEMA}. El acceso se audita.
     * </p>
     *
     * @param usuario usuario que solicita el listado
     * @return solicitudes en estado {@code PENDIENTE}
     * @throws AccesoDenegado si el usuario no tiene un rol autorizado
     */
    List<SolicitudPrueba> listarPendientes(Usuario usuario);

    /**
     * Obtiene una solicitud por su identificador interno.
     * <p>
     * Precondiciones: el usuario debe tener rol {@code MEDICO},
     * {@code ADMINISTRATIVO} o {@code ADMINISTRADOR_SISTEMA}. La consulta se audita.
     * </p>
     *
     * @param usuario usuario que solicita la consulta
     * @param id identificador de la solicitud
     * @return solicitud encontrada o {@link Optional#empty()} si no existe
     * @throws AccesoDenegado si el usuario no tiene un rol autorizado
     * @throws ValidacionIncorrecta si el identificador es nulo
     */
    Optional<SolicitudPrueba> obtenerPorId(Usuario usuario, Long id);

    /**
     * Lista los tipos de prueba disponibles en el catálogo.
     * <p>
     * Precondiciones: el usuario debe tener rol {@code MEDICO},
     * {@code ADMINISTRATIVO} o {@code ADMINISTRADOR_SISTEMA}.
     * </p>
     *
     * @param usuario usuario que solicita el catálogo
     * @return tipos de prueba disponibles
     * @throws AccesoDenegado si el usuario no tiene un rol autorizado
     */
    List<TipoPrueba> listarTiposPrueba(Usuario usuario);
}
