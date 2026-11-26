package es.sescam.cliente.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.sescam.cliente.model.ImagenMedicaDTO;
import es.sescam.cliente.model.InformeClinicoDTO;
import es.sescam.cliente.service.InformeClinicoApiClient;
import es.sescam.cliente.service.auth.Solicitante;
import es.sescam.cliente.service.error.ApiException;
import es.sescam.cliente.view.InformeClinicoView;

/**
 * Implementación del controlador de presentación de informes clínicos.
 * <p>
 * Captura los errores de la API y los comunica a la vista mediante
 * {@link InformeClinicoView#mostrarError(String)}. Las operaciones correctas se
 * confirman con {@link InformeClinicoView#mostrarConfirmacion(String)}.
 * </p>
 */
public class InformeClinicoControllerImpl implements InformeClinicoController {

    private final InformeClinicoApiClient informeClinicoApiClient;
    private final InformeClinicoView view;

    /**
     * Crea el controlador con el cliente de la API y la vista.
     *
     * @param informeClinicoApiClient cliente de la API de informes clínicos
     * @param view vista de informes clínicos
     */
    public InformeClinicoControllerImpl(InformeClinicoApiClient informeClinicoApiClient,
                                        InformeClinicoView view) {
        this.informeClinicoApiClient = Objects.requireNonNull(informeClinicoApiClient,
                "informeClinicoApiClient");
        this.view = Objects.requireNonNull(view, "view");
    }

    @Override
    public void crear(Solicitante solicitante, InformeClinicoDTO informe) {
        try {
            InformeClinicoDTO creado = informeClinicoApiClient.crear(solicitante, informe);
            view.mostrarInforme(creado);
            view.mostrarConfirmacion("Informe clínico creado correctamente");
        } catch (ApiException e) {
            view.mostrarError(e.getError().getMessage());
        }
    }

    @Override
    public void guardar(Solicitante solicitante, Long id, InformeClinicoDTO informe) {
        try {
            InformeClinicoDTO actualizado = informeClinicoApiClient.actualizar(solicitante, id, informe);
            view.mostrarInforme(actualizado);
            view.mostrarConfirmacion("Informe clínico modificado correctamente");
        } catch (ApiException e) {
            view.mostrarError(e.getError().getMessage());
        }
    }

    @Override
    public void firmar(Solicitante solicitante, Long id) {
        try {
            informeClinicoApiClient.firmar(solicitante, id);
            view.mostrarConfirmacion("Informe clínico firmado correctamente");
        } catch (ApiException e) {
            view.mostrarError(e.getError().getMessage());
        }
    }

    @Override
    public void consultar(Solicitante solicitante, Long id) {
        try {
            view.mostrarInforme(informeClinicoApiClient.obtenerPorId(solicitante, id));
        } catch (ApiException e) {
            view.mostrarError(e.getError().getMessage());
        }
    }

    @Override
    public void listarPorPaciente(Solicitante solicitante, Long pacienteId) {
        try {
            view.mostrarInformes(informeClinicoApiClient.listarPorPaciente(solicitante, pacienteId));
        } catch (ApiException e) {
            view.mostrarError(e.getError().getMessage());
        }
    }

    @Override
    public List<ImagenMedicaDTO> cargarImagenes(Solicitante solicitante, Long solicitudId) {
        try {
            List<ImagenMedicaDTO> imagenes = informeClinicoApiClient.listarImagenes(solicitante,
                    solicitudId);
            view.mostrarImagenes(imagenes);
            return imagenes;
        } catch (ApiException e) {
            view.mostrarError(e.getError().getMessage());
            return new ArrayList<>();
        }
    }
}
