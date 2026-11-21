package es.sescam.cliente.service;

import java.util.List;

import es.sescam.cliente.model.ImagenMedicaDTO;
import es.sescam.cliente.model.InformeClinicoDTO;
import es.sescam.cliente.service.auth.Solicitante;
import es.sescam.cliente.service.error.ApiException;

/**
 * Contrato API para la gestión de informes clínicos e imágenes médicas (CU-09).
 * <p>
 * El solicitante se identifica por DNI único para controlar el acceso. La
 * creación, modificación, firma y consulta de imágenes están reservadas al rol
 * {@code DOCTOR}; la consulta de informes está disponible para {@code DOCTOR} o
 * {@code ADMINISTRADOR}.
 * </p>
 */
public interface InformeClinicoApiClient {

    /**
     * Devuelve la URL base de la API.
     *
     * @return URL base configurada
     */
    String getBaseUrl();

    /**
     * Crea un informe clínico en estado {@code BORRADOR}.
     * <p>
     * Solo el rol {@code DOCTOR} puede realizar esta operación. La solicitud y el
     * contenido son obligatorios.
     * </p>
     *
     * @param solicitante usuario que realiza la petición
     * @param informe datos del informe
     * @return copia del informe registrado
     * @throws ApiException si la petición no es válida o el acceso está prohibido
     */
    InformeClinicoDTO crear(Solicitante solicitante, InformeClinicoDTO informe);

    /**
     * Modifica un informe clínico existente.
     * <p>
     * Solo el rol {@code DOCTOR} puede realizar esta operación.
     * </p>
     *
     * @param solicitante usuario que realiza la petición
     * @param id identificador del informe
     * @param informe nuevos datos del informe
     * @return informe actualizado
     * @throws ApiException si la petición no es válida, el acceso está prohibido o
     *         el informe no existe
     */
    InformeClinicoDTO actualizar(Solicitante solicitante, Long id, InformeClinicoDTO informe);

    /**
     * Firma un informe clínico existente.
     * <p>
     * Solo el rol {@code DOCTOR} puede realizar esta operación.
     * </p>
     *
     * @param solicitante usuario que realiza la petición
     * @param id identificador del informe
     * @throws ApiException si la petición no es válida, el acceso está prohibido o
     *         el informe no existe
     */
    void firmar(Solicitante solicitante, Long id);

    /**
     * Obtiene un informe clínico por su identificador.
     * <p>
     * Los roles {@code DOCTOR} o {@code ADMINISTRADOR} pueden realizar la consulta.
     * </p>
     *
     * @param solicitante usuario que realiza la petición
     * @param id identificador del informe
     * @return informe encontrado
     * @throws ApiException si la petición no es válida, el acceso está prohibido o
     *         el informe no existe
     */
    InformeClinicoDTO obtenerPorId(Solicitante solicitante, Long id);

    /**
     * Lista los informes clínicos de un paciente.
     * <p>
     * Los roles {@code DOCTOR} o {@code ADMINISTRADOR} pueden realizar la consulta.
     * </p>
     *
     * @param solicitante usuario que realiza la petición
     * @param pacienteId identificador del paciente
     * @return informes del paciente
     * @throws ApiException si la petición no es válida o el acceso está prohibido
     */
    List<InformeClinicoDTO> listarPorPaciente(Solicitante solicitante, Long pacienteId);

    /**
     * Lista las imágenes médicas asociadas a una solicitud de prueba.
     * <p>
     * Solo el rol {@code DOCTOR} puede realizar la consulta.
     * </p>
     *
     * @param solicitante usuario que realiza la petición
     * @param solicitudId identificador de la solicitud de prueba
     * @return imágenes DICOM del estudio
     * @throws ApiException si la petición no es válida o el acceso está prohibido
     */
    List<ImagenMedicaDTO> listarImagenes(Solicitante solicitante, Long solicitudId);
}
