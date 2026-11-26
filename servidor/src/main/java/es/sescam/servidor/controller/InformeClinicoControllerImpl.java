package es.sescam.servidor.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import es.sescam.servidor.model.ImagenMedica;
import es.sescam.servidor.model.InformeClinico;
import es.sescam.servidor.seguridad.Usuario;
import es.sescam.servidor.service.InformeClinicoService;

/**
 * Implementación del controlador de informes clínicos.
 * <p>
 * Todas las operaciones se limitan a delegar en {@link InformeClinicoService};
 * las reglas de negocio, el control de acceso y la auditoría viven en el
 * servicio.
 * </p>
 */
public class InformeClinicoControllerImpl implements InformeClinicoController {

    private final InformeClinicoService informeClinicoService;

    /**
     * Crea el controlador con el servicio de informes clínicos.
     *
     * @param informeClinicoService servicio de informes clínicos
     */
    public InformeClinicoControllerImpl(InformeClinicoService informeClinicoService) {
        this.informeClinicoService = Objects.requireNonNull(informeClinicoService,
                "informeClinicoService");
    }

    @Override
    public InformeClinico crear(Usuario usuario, InformeClinico informe) {
        return informeClinicoService.crear(usuario, informe);
    }

    @Override
    public InformeClinico actualizar(Usuario usuario, Long id, InformeClinico datos) {
        return informeClinicoService.actualizar(usuario, id, datos);
    }

    @Override
    public InformeClinico firmar(Usuario usuario, Long id) {
        return informeClinicoService.firmar(usuario, id);
    }

    @Override
    public Optional<InformeClinico> obtenerPorId(Usuario usuario, Long id) {
        return informeClinicoService.obtenerPorId(usuario, id);
    }

    @Override
    public List<InformeClinico> listarPorPaciente(Usuario usuario, Long pacienteId) {
        return informeClinicoService.listarPorPaciente(usuario, pacienteId);
    }

    @Override
    public List<ImagenMedica> listarImagenes(Usuario usuario, Long solicitudId) {
        return informeClinicoService.listarImagenes(usuario, solicitudId);
    }
}
