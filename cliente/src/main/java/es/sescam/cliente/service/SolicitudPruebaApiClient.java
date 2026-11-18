package es.sescam.cliente.service;

import java.util.List;

import es.sescam.cliente.model.SolicitudPruebaDTO;
import es.sescam.cliente.model.TipoPruebaDTO;
import es.sescam.cliente.service.auth.Solicitante;
import es.sescam.cliente.service.error.ApiException;

/**
 * Contrato API para la prescripción de pruebas diagnósticas (CU-06).
 * <p>
 * El solicitante se identifica por DNI único para controlar el acceso. La
 * prescripción solo está permitida al rol {@code DOCTOR}; el listado de
 * pendientes está disponible para {@code DOCTOR} o {@code ADMINISTRADOR}.
 * </p>
 */
public interface SolicitudPruebaApiClient {

    /**
     * Devuelve la URL base de la API.
     *
     * @return URL base configurada
     */
    String getBaseUrl();

    /**
     * Lista los tipos de prueba disponibles en el catálogo.
     *
     * @param solicitante usuario que realiza la petición
     * @return tipos de prueba del catálogo
     * @throws ApiException si el solicitante no está autenticado o no está autorizado
     */
    List<TipoPruebaDTO> listarTiposPrueba(Solicitante solicitante);

    /**
     * Prescribe una prueba diagnóstica.
     * <p>
     * Solo el rol {@code DOCTOR} puede realizar esta operación. El paciente, el
     * médico, el tipo de prueba, la prioridad, la indicación clínica y la
     * justificación son obligatorios. Si la prueba exige consentimiento informado,
     * debe constar su aceptación.
     * </p>
     *
     * @param solicitante usuario que realiza la petición
     * @param solicitud datos de la solicitud
     * @return copia de la solicitud registrada en estado {@code PENDIENTE}
     * @throws ApiException si la petición no es válida o el acceso está prohibido
     */
    SolicitudPruebaDTO prescribir(Solicitante solicitante, SolicitudPruebaDTO solicitud);

    /**
     * Lista las solicitudes pendientes de citación.
     * <p>
     * Solo los roles {@code DOCTOR} o {@code ADMINISTRADOR} pueden consultar el
     * listado.
     * </p>
     *
     * @param solicitante usuario que realiza la petición
     * @return solicitudes pendientes
     * @throws ApiException si el solicitante no está autorizado
     */
    List<SolicitudPruebaDTO> listarPendientes(Solicitante solicitante);
}
