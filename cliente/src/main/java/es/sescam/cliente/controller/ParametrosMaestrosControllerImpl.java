package es.sescam.cliente.controller;

import java.util.List;
import java.util.Objects;

import es.sescam.cliente.model.HorarioAtencionDTO;
import es.sescam.cliente.model.ParametroPrioridadDTO;
import es.sescam.cliente.model.PoliticaCitacionDTO;
import es.sescam.cliente.model.ProtocoloDTO;
import es.sescam.cliente.model.TipoPruebaDTO;
import es.sescam.cliente.service.ParametrosMaestrosApiClient;
import es.sescam.cliente.service.auth.Solicitante;
import es.sescam.cliente.service.error.ApiException;
import es.sescam.cliente.view.ParametrosMaestrosView;

/**
 * Implementación del controlador de presentación del CU-17: delega en la API y
 * traduce los errores de ésta a mensajes de la vista.
 */
public class ParametrosMaestrosControllerImpl implements ParametrosMaestrosController {

    private final ParametrosMaestrosApiClient apiClient;
    private final ParametrosMaestrosView view;

    public ParametrosMaestrosControllerImpl(ParametrosMaestrosApiClient apiClient,
                                            ParametrosMaestrosView view) {
        this.apiClient = Objects.requireNonNull(apiClient, "apiClient");
        this.view = Objects.requireNonNull(view, "view");
    }

    @Override
    public void crearTipoPrueba(Solicitante solicitante, TipoPruebaDTO tipoPrueba) {
        ejecutar(() -> {
            apiClient.crearTipoPrueba(solicitante, tipoPrueba);
            view.mostrarMensaje("Tipo de prueba guardado correctamente");
        });
    }

    @Override
    public void editarTipoPrueba(Solicitante solicitante, Long id, TipoPruebaDTO datos) {
        ejecutar(() -> {
            apiClient.actualizarTipoPrueba(solicitante, id, datos);
            view.mostrarMensaje("Tipo de prueba actualizado correctamente");
        });
    }

    @Override
    public void listarTiposPrueba(Solicitante solicitante) {
        ejecutar(() -> view.mostrarTiposPrueba(apiClient.listarTiposPrueba(solicitante)));
    }

    @Override
    public void crearProtocolo(Solicitante solicitante, ProtocoloDTO protocolo) {
        ejecutar(() -> {
            apiClient.crearProtocolo(solicitante, protocolo);
            view.mostrarMensaje("Protocolo guardado correctamente");
        });
    }

    @Override
    public void editarProtocolo(Solicitante solicitante, Long id, ProtocoloDTO datos) {
        ejecutar(() -> {
            apiClient.actualizarProtocolo(solicitante, id, datos);
            view.mostrarMensaje("Protocolo actualizado correctamente");
        });
    }

    @Override
    public void listarProtocolos(Solicitante solicitante) {
        ejecutar(() -> view.mostrarProtocolos(apiClient.listarProtocolos(solicitante)));
    }

    @Override
    public void crearHorario(Solicitante solicitante, HorarioAtencionDTO horario) {
        ejecutar(() -> {
            apiClient.crearHorario(solicitante, horario);
            view.mostrarMensaje("Horario guardado correctamente");
        });
    }

    @Override
    public void editarHorario(Solicitante solicitante, Long id, HorarioAtencionDTO datos) {
        ejecutar(() -> {
            apiClient.actualizarHorario(solicitante, id, datos);
            view.mostrarMensaje("Horario actualizado correctamente");
        });
    }

    @Override
    public void listarHorarios(Solicitante solicitante) {
        ejecutar(() -> view.mostrarHorarios(apiClient.listarHorarios(solicitante)));
    }

    @Override
    public void crearPolitica(Solicitante solicitante, PoliticaCitacionDTO politica) {
        ejecutar(() -> {
            apiClient.crearPolitica(solicitante, politica);
            view.mostrarMensaje("Política de citación guardada correctamente");
        });
    }

    @Override
    public void editarPolitica(Solicitante solicitante, Long id, PoliticaCitacionDTO datos) {
        ejecutar(() -> {
            apiClient.actualizarPolitica(solicitante, id, datos);
            view.mostrarMensaje("Política de citación actualizada correctamente");
        });
    }

    @Override
    public void listarPoliticas(Solicitante solicitante) {
        ejecutar(() -> view.mostrarPoliticas(apiClient.listarPoliticas(solicitante)));
    }

    @Override
    public void configurarParametroPrioridad(Solicitante solicitante, ParametroPrioridadDTO parametro) {
        ejecutar(() -> {
            apiClient.configurarParametroPrioridad(solicitante, parametro);
            view.mostrarMensaje("Nivel de prioridad configurado correctamente");
        });
    }

    @Override
    public void listarParametrosPrioridad(Solicitante solicitante) {
        ejecutar(() -> view.mostrarParametrosPrioridad(
                apiClient.listarParametrosPrioridad(solicitante)));
    }

    @Override
    public void listarNivelesPrioridad(Solicitante solicitante) {
        ejecutar(() -> view.mostrarNivelesPrioridad(apiClient.listarNivelesPrioridad(solicitante)));
    }

    @Override
    public void mostrarTiposPrueba(List<TipoPruebaDTO> tipos) {
        view.mostrarTiposPrueba(tipos);
    }

    @Override
    public void mostrarProtocolos(List<ProtocoloDTO> protocolos) {
        view.mostrarProtocolos(protocolos);
    }

    @Override
    public void mostrarHorarios(List<HorarioAtencionDTO> horarios) {
        view.mostrarHorarios(horarios);
    }

    @Override
    public void mostrarPoliticas(List<PoliticaCitacionDTO> politicas) {
        view.mostrarPoliticas(politicas);
    }

    @Override
    public void mostrarParametrosPrioridad(List<ParametroPrioridadDTO> parametros) {
        view.mostrarParametrosPrioridad(parametros);
    }

    @Override
    public void mostrarNivelesPrioridad(List<String> niveles) {
        view.mostrarNivelesPrioridad(niveles);
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        view.mostrarMensaje(mensaje);
    }

    private void ejecutar(Runnable accion) {
        try {
            accion.run();
        } catch (ApiException e) {
            view.mostrarMensaje(e.getError().getMessage());
        }
    }
}
