package es.sescam.cliente.controller;

import java.util.Objects;

import es.sescam.cliente.model.SolicitudPruebaDTO;
import es.sescam.cliente.service.SolicitudPruebaApiClient;
import es.sescam.cliente.service.auth.Solicitante;
import es.sescam.cliente.service.error.ApiException;
import es.sescam.cliente.view.SolicitudPruebaView;

/**
 * Implementación del controlador de presentación de solicitudes de prueba.
 * <p>
 * Captura los errores de la API y los comunica a la vista mediante
 * {@link SolicitudPruebaView#mostrarError(String)}. Las operaciones correctas se
 * confirman con {@link SolicitudPruebaView#mostrarConfirmacion(String)}.
 * </p>
 */
public class SolicitudPruebaControllerImpl implements SolicitudPruebaController {

    private final SolicitudPruebaApiClient solicitudPruebaApiClient;
    private final SolicitudPruebaView view;

    /**
     * Crea el controlador con el cliente de la API y la vista.
     *
     * @param solicitudPruebaApiClient cliente de la API de solicitudes de prueba
     * @param view vista de solicitudes de prueba
     */
    public SolicitudPruebaControllerImpl(SolicitudPruebaApiClient solicitudPruebaApiClient,
                                         SolicitudPruebaView view) {
        this.solicitudPruebaApiClient = Objects.requireNonNull(solicitudPruebaApiClient,
                "solicitudPruebaApiClient");
        this.view = Objects.requireNonNull(view, "view");
    }

    @Override
    public void prescribir(Solicitante solicitante, SolicitudPruebaDTO solicitud) {
        try {
            solicitudPruebaApiClient.prescribir(solicitante, solicitud);
            view.mostrarConfirmacion("Solicitud de prueba registrada correctamente");
        } catch (ApiException e) {
            view.mostrarError(e.getError().getMessage());
        }
    }

    @Override
    public void listarPendientes(Solicitante solicitante) {
        try {
            view.mostrarSolicitudesPendientes(solicitudPruebaApiClient.listarPendientes(solicitante));
        } catch (ApiException e) {
            view.mostrarError(e.getError().getMessage());
        }
    }

    @Override
    public void cargarTiposPrueba(Solicitante solicitante) {
        try {
            view.mostrarTiposPrueba(solicitudPruebaApiClient.listarTiposPrueba(solicitante));
        } catch (ApiException e) {
            view.mostrarError(e.getError().getMessage());
        }
    }
}
