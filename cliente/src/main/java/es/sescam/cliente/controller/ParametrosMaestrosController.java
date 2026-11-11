package es.sescam.cliente.controller;

import java.util.List;

import es.sescam.cliente.model.HorarioAtencionDTO;
import es.sescam.cliente.model.ParametroPrioridadDTO;
import es.sescam.cliente.model.PoliticaCitacionDTO;
import es.sescam.cliente.model.ProtocoloDTO;
import es.sescam.cliente.model.TipoPruebaDTO;
import es.sescam.cliente.service.auth.Solicitante;

/**
 * Contrato del controlador de presentación del CU-17. Conecta la vista con la API
 * de parámetros maestros; las reglas de negocio y la autorización final residen en
 * el servidor. Solo el rol ADMINISTRADOR puede utilizar estas operaciones.
 */
public interface ParametrosMaestrosController {

    void crearTipoPrueba(Solicitante solicitante, TipoPruebaDTO tipoPrueba);

    void editarTipoPrueba(Solicitante solicitante, Long id, TipoPruebaDTO datos);

    void listarTiposPrueba(Solicitante solicitante);

    void crearProtocolo(Solicitante solicitante, ProtocoloDTO protocolo);

    void editarProtocolo(Solicitante solicitante, Long id, ProtocoloDTO datos);

    void listarProtocolos(Solicitante solicitante);

    void crearHorario(Solicitante solicitante, HorarioAtencionDTO horario);

    void editarHorario(Solicitante solicitante, Long id, HorarioAtencionDTO datos);

    void listarHorarios(Solicitante solicitante);

    void crearPolitica(Solicitante solicitante, PoliticaCitacionDTO politica);

    void editarPolitica(Solicitante solicitante, Long id, PoliticaCitacionDTO datos);

    void listarPoliticas(Solicitante solicitante);

    void configurarParametroPrioridad(Solicitante solicitante, ParametroPrioridadDTO parametro);

    void listarParametrosPrioridad(Solicitante solicitante);

    void listarNivelesPrioridad(Solicitante solicitante);

    void mostrarTiposPrueba(List<TipoPruebaDTO> tipos);

    void mostrarProtocolos(List<ProtocoloDTO> protocolos);

    void mostrarHorarios(List<HorarioAtencionDTO> horarios);

    void mostrarPoliticas(List<PoliticaCitacionDTO> politicas);

    void mostrarParametrosPrioridad(List<ParametroPrioridadDTO> parametros);

    void mostrarNivelesPrioridad(List<String> niveles);

    void mostrarMensaje(String mensaje);
}
