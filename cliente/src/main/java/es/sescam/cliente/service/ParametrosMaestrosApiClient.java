package es.sescam.cliente.service;

import java.util.List;

import es.sescam.cliente.model.HorarioAtencionDTO;
import es.sescam.cliente.model.ParametroPrioridadDTO;
import es.sescam.cliente.model.PoliticaCitacionDTO;
import es.sescam.cliente.model.ProtocoloDTO;
import es.sescam.cliente.model.TipoPruebaDTO;
import es.sescam.cliente.service.auth.Solicitante;

/**
 * Contrato API específico para la configuración de parámetros maestros (CU-17).
 *
 * <p>Precondiciones: el {@link Solicitante} debe estar identificado por DNI y tener
 * rol {@code ADMINISTRADOR}; en caso contrario la API lanza {@code ApiException} con
 * estado 401 o 403. Las búsquedas inexistentes devuelven error 404. Roles autorizados:
 * solo administradores. Tiempo máximo esperado: inmediato (sin llamada de red en la
 * implementación actual).</p>
 */
public interface ParametrosMaestrosApiClient {

    String getBaseUrl();

    List<TipoPruebaDTO> listarTiposPrueba(Solicitante solicitante);

    TipoPruebaDTO crearTipoPrueba(Solicitante solicitante, TipoPruebaDTO tipoPrueba);

    TipoPruebaDTO actualizarTipoPrueba(Solicitante solicitante, Long id, TipoPruebaDTO datos);

    List<ProtocoloDTO> listarProtocolos(Solicitante solicitante);

    ProtocoloDTO crearProtocolo(Solicitante solicitante, ProtocoloDTO protocolo);

    ProtocoloDTO actualizarProtocolo(Solicitante solicitante, Long id, ProtocoloDTO datos);

    List<HorarioAtencionDTO> listarHorarios(Solicitante solicitante);

    HorarioAtencionDTO crearHorario(Solicitante solicitante, HorarioAtencionDTO horario);

    HorarioAtencionDTO actualizarHorario(Solicitante solicitante, Long id, HorarioAtencionDTO datos);

    List<PoliticaCitacionDTO> listarPoliticas(Solicitante solicitante);

    PoliticaCitacionDTO crearPolitica(Solicitante solicitante, PoliticaCitacionDTO politica);

    PoliticaCitacionDTO actualizarPolitica(Solicitante solicitante, Long id, PoliticaCitacionDTO datos);

    List<ParametroPrioridadDTO> listarParametrosPrioridad(Solicitante solicitante);

    ParametroPrioridadDTO configurarParametroPrioridad(Solicitante solicitante, ParametroPrioridadDTO parametro);

    List<String> listarNivelesPrioridad(Solicitante solicitante);
}
