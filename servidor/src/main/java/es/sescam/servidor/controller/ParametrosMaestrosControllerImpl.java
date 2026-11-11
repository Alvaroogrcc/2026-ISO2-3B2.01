package es.sescam.servidor.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import es.sescam.servidor.model.HorarioAtencion;
import es.sescam.servidor.model.ParametroPrioridad;
import es.sescam.servidor.model.PoliticaCitacion;
import es.sescam.servidor.model.Prioridad;
import es.sescam.servidor.model.Protocolo;
import es.sescam.servidor.model.TipoPrueba;
import es.sescam.servidor.seguridad.Usuario;
import es.sescam.servidor.service.ParametrosMaestrosService;

/**
 * Implementación del controlador del CU-17: exclusivamente delegación al servicio.
 */
public class ParametrosMaestrosControllerImpl implements ParametrosMaestrosController {

    private final ParametrosMaestrosService parametrosMaestrosService;

    public ParametrosMaestrosControllerImpl(ParametrosMaestrosService parametrosMaestrosService) {
        this.parametrosMaestrosService = Objects.requireNonNull(
                parametrosMaestrosService, "parametrosMaestrosService");
    }

    @Override
    public List<TipoPrueba> listarTiposPrueba(Usuario solicitante) {
        return parametrosMaestrosService.listarTiposPrueba(solicitante);
    }

    @Override
    public Optional<TipoPrueba> obtenerTipoPrueba(Usuario solicitante, Long id) {
        return parametrosMaestrosService.obtenerTipoPrueba(solicitante, id);
    }

    @Override
    public TipoPrueba crearTipoPrueba(Usuario solicitante, TipoPrueba tipoPrueba) {
        return parametrosMaestrosService.crearTipoPrueba(solicitante, tipoPrueba);
    }

    @Override
    public TipoPrueba actualizarTipoPrueba(Usuario solicitante, Long id, TipoPrueba datos) {
        return parametrosMaestrosService.actualizarTipoPrueba(solicitante, id, datos);
    }

    @Override
    public List<Protocolo> listarProtocolos(Usuario solicitante) {
        return parametrosMaestrosService.listarProtocolos(solicitante);
    }

    @Override
    public Optional<Protocolo> obtenerProtocolo(Usuario solicitante, Long id) {
        return parametrosMaestrosService.obtenerProtocolo(solicitante, id);
    }

    @Override
    public Protocolo crearProtocolo(Usuario solicitante, Protocolo protocolo) {
        return parametrosMaestrosService.crearProtocolo(solicitante, protocolo);
    }

    @Override
    public Protocolo actualizarProtocolo(Usuario solicitante, Long id, Protocolo datos) {
        return parametrosMaestrosService.actualizarProtocolo(solicitante, id, datos);
    }

    @Override
    public List<HorarioAtencion> listarHorarios(Usuario solicitante) {
        return parametrosMaestrosService.listarHorarios(solicitante);
    }

    @Override
    public Optional<HorarioAtencion> obtenerHorario(Usuario solicitante, Long id) {
        return parametrosMaestrosService.obtenerHorario(solicitante, id);
    }

    @Override
    public HorarioAtencion crearHorario(Usuario solicitante, HorarioAtencion horario) {
        return parametrosMaestrosService.crearHorario(solicitante, horario);
    }

    @Override
    public HorarioAtencion actualizarHorario(Usuario solicitante, Long id, HorarioAtencion datos) {
        return parametrosMaestrosService.actualizarHorario(solicitante, id, datos);
    }

    @Override
    public List<PoliticaCitacion> listarPoliticas(Usuario solicitante) {
        return parametrosMaestrosService.listarPoliticas(solicitante);
    }

    @Override
    public Optional<PoliticaCitacion> obtenerPolitica(Usuario solicitante, Long id) {
        return parametrosMaestrosService.obtenerPolitica(solicitante, id);
    }

    @Override
    public PoliticaCitacion crearPolitica(Usuario solicitante, PoliticaCitacion politica) {
        return parametrosMaestrosService.crearPolitica(solicitante, politica);
    }

    @Override
    public PoliticaCitacion actualizarPolitica(Usuario solicitante, Long id, PoliticaCitacion datos) {
        return parametrosMaestrosService.actualizarPolitica(solicitante, id, datos);
    }

    @Override
    public List<ParametroPrioridad> listarParametrosPrioridad(Usuario solicitante) {
        return parametrosMaestrosService.listarParametrosPrioridad(solicitante);
    }

    @Override
    public ParametroPrioridad configurarParametroPrioridad(Usuario solicitante, ParametroPrioridad parametro) {
        return parametrosMaestrosService.configurarParametroPrioridad(solicitante, parametro);
    }

    @Override
    public List<Prioridad> listarNivelesPrioridad(Usuario solicitante) {
        return parametrosMaestrosService.listarNivelesPrioridad(solicitante);
    }
}
