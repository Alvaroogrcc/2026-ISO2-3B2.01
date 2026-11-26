package es.sescam.servidor.controller;

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
 * Puerta de entrada del CU-17. Recibe las peticiones, delega en el servicio y
 * devuelve el resultado sin contener lógica de negocio.
 *
 * <p>Precondiciones: se requiere un usuario autenticado con rol
 * {@code ADMINISTRADOR_SISTEMA}; las excepciones de validación, acceso y búsqueda
 * se propagan desde el servicio. Roles autorizados: solo administradores.
 * Tiempo máximo esperado: inmediato.</p>
 */
public interface ParametrosMaestrosController {

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
