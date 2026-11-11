package es.sescam.cliente.view;

import java.util.List;

import es.sescam.cliente.model.HorarioAtencionDTO;
import es.sescam.cliente.model.ParametroPrioridadDTO;
import es.sescam.cliente.model.PoliticaCitacionDTO;
import es.sescam.cliente.model.ProtocoloDTO;
import es.sescam.cliente.model.TipoPruebaDTO;

/**
 * Contrato de vista para la configuración de parámetros maestros (CU-17).
 * La vista se limita a mostrar datos y mensajes; no valida ni decide.
 */
public interface ParametrosMaestrosView {

    void mostrarTiposPrueba(List<TipoPruebaDTO> tipos);

    void mostrarProtocolos(List<ProtocoloDTO> protocolos);

    void mostrarHorarios(List<HorarioAtencionDTO> horarios);

    void mostrarPoliticas(List<PoliticaCitacionDTO> politicas);

    void mostrarParametrosPrioridad(List<ParametroPrioridadDTO> parametros);

    void mostrarNivelesPrioridad(List<String> niveles);

    void mostrarMensaje(String mensaje);
}
