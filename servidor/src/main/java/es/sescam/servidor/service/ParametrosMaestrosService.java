package es.sescam.servidor.service;

import java.util.List;
import java.util.Optional;

import es.sescam.servidor.model.HorarioAtencion;
import es.sescam.servidor.model.ParametroPrioridad;
import es.sescam.servidor.model.PoliticaCitacion;
import es.sescam.servidor.model.Prioridad;
import es.sescam.servidor.model.Protocolo;
import es.sescam.servidor.model.TipoPrueba;
import es.sescam.servidor.seguridad.Usuario;

/**
 * Contrato de negocio del CU-17: configuración global de catálogo de pruebas,
 * protocolos, horarios, políticas de citación y niveles de prioridad.
 *
 * <p>Precondiciones: todas las operaciones requieren un {@link Usuario} con el rol
 * {@code ADMINISTRADOR_SISTEMA}; en caso contrario se lanza {@link SecurityException}.
 * Las validaciones de entrada lanzan {@link IllegalArgumentException} y la ausencia de
 * entidades lanza {@link java.util.NoSuchElementException}. Toda operación queda
 * registrada en el contrato de auditoría. Tiempo máximo esperado: inmediato (sin red).</p>
 */
public interface ParametrosMaestrosService {

    List<TipoPrueba> listarTiposPrueba(Usuario solicitante);

    Optional<TipoPrueba> obtenerTipoPrueba(Usuario solicitante, Long id);

    TipoPrueba crearTipoPrueba(Usuario solicitante, TipoPrueba tipoPrueba);

    TipoPrueba actualizarTipoPrueba(Usuario solicitante, Long id, TipoPrueba datos);

    List<Protocolo> listarProtocolos(Usuario solicitante);

    Optional<Protocolo> obtenerProtocolo(Usuario solicitante, Long id);

    Protocolo crearProtocolo(Usuario solicitante, Protocolo protocolo);

    Protocolo actualizarProtocolo(Usuario solicitante, Long id, Protocolo datos);

    List<HorarioAtencion> listarHorarios(Usuario solicitante);

    Optional<HorarioAtencion> obtenerHorario(Usuario solicitante, Long id);

    HorarioAtencion crearHorario(Usuario solicitante, HorarioAtencion horario);

    HorarioAtencion actualizarHorario(Usuario solicitante, Long id, HorarioAtencion datos);

    List<PoliticaCitacion> listarPoliticas(Usuario solicitante);

    Optional<PoliticaCitacion> obtenerPolitica(Usuario solicitante, Long id);

    PoliticaCitacion crearPolitica(Usuario solicitante, PoliticaCitacion politica);

    PoliticaCitacion actualizarPolitica(Usuario solicitante, Long id, PoliticaCitacion datos);

    List<ParametroPrioridad> listarParametrosPrioridad(Usuario solicitante);

    ParametroPrioridad configurarParametroPrioridad(Usuario solicitante, ParametroPrioridad parametro);

    List<Prioridad> listarNivelesPrioridad(Usuario solicitante);
}
