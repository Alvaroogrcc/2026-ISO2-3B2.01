package es.sescam.servidor.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import es.sescam.servidor.model.SolicitudPrueba;
import es.sescam.servidor.model.TipoPrueba;
import es.sescam.servidor.seguridad.Usuario;
import es.sescam.servidor.service.SolicitudPruebaService;

/**
 * Implementación del controlador de solicitudes de prueba.
 * <p>
 * Todas las operaciones se limitan a delegar en {@link SolicitudPruebaService};
 * las reglas de negocio, el control de acceso y la auditoría viven en el servicio.
 * </p>
 */
public class SolicitudPruebaControllerImpl implements SolicitudPruebaController {

    private final SolicitudPruebaService solicitudPruebaService;

    /**
     * Crea el controlador con el servicio de solicitudes de prueba.
     *
     * @param solicitudPruebaService servicio de solicitudes de prueba
     */
    public SolicitudPruebaControllerImpl(SolicitudPruebaService solicitudPruebaService) {
        this.solicitudPruebaService = Objects.requireNonNull(solicitudPruebaService, "solicitudPruebaService");
    }

    @Override
    public SolicitudPrueba prescribir(Usuario usuario, SolicitudPrueba solicitud) {
        return solicitudPruebaService.prescribir(usuario, solicitud);
    }

    @Override
    public List<SolicitudPrueba> listarPendientes(Usuario usuario) {
        return solicitudPruebaService.listarPendientes(usuario);
    }

    @Override
    public Optional<SolicitudPrueba> obtenerPorId(Usuario usuario, Long id) {
        return solicitudPruebaService.obtenerPorId(usuario, id);
    }

    @Override
    public List<TipoPrueba> listarTiposPrueba(Usuario usuario) {
        return solicitudPruebaService.listarTiposPrueba(usuario);
    }
}
