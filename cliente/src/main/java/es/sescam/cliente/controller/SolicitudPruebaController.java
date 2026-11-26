package es.sescam.cliente.controller;

import es.sescam.cliente.model.SolicitudPruebaDTO;
import es.sescam.cliente.service.auth.Solicitante;

/**
 * Controlador de presentación para la prescripción de pruebas diagnósticas
 * (CU-06).
 * <p>
 * Traduce las acciones de la vista en llamadas al cliente de la API y comunica
 * los resultados a la vista. No contiene reglas de negocio ni accede al modelo
 * del servidor.
 * </p>
 */
public interface SolicitudPruebaController {

    /**
     * Prescribe una prueba diagnóstica y muestra el resultado en la vista.
     *
     * @param solicitante usuario que realiza la petición
     * @param solicitud datos de la solicitud
     */
    void prescribir(Solicitante solicitante, SolicitudPruebaDTO solicitud);

    /**
     * Solicita el listado de solicitudes pendientes y lo muestra en la vista.
     *
     * @param solicitante usuario que realiza la petición
     */
    void listarPendientes(Solicitante solicitante);

    /**
     * Carga el catálogo de tipos de prueba y lo muestra en la vista.
     *
     * @param solicitante usuario que realiza la petición
     */
    void cargarTiposPrueba(Solicitante solicitante);
}
