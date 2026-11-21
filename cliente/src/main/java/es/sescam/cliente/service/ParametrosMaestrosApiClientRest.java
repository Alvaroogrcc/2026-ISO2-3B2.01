package es.sescam.cliente.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import es.sescam.cliente.model.HorarioAtencionDTO;
import es.sescam.cliente.model.ParametroPrioridadDTO;
import es.sescam.cliente.model.PoliticaCitacionDTO;
import es.sescam.cliente.model.ProtocoloDTO;
import es.sescam.cliente.model.TipoPruebaDTO;
import es.sescam.cliente.service.auth.RolSolicitante;
import es.sescam.cliente.service.auth.Solicitante;
import es.sescam.cliente.service.error.ApiError;
import es.sescam.cliente.service.error.ApiException;

/**
 * Implementación REST del contrato de parámetros maestros (CU-17).
 * Mantiene el patrón del resto de API clients del proyecto: valida identidad,
 * rol y datos de entrada; la llamada HTTP real queda pendiente según DECISIONS.md.
 */
public class ParametrosMaestrosApiClientRest implements ParametrosMaestrosApiClient {

    private final String baseUrl;

    public ParametrosMaestrosApiClientRest(String baseUrl) {
        this.baseUrl = Objects.requireNonNull(baseUrl, "baseUrl");
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public List<TipoPruebaDTO> listarTiposPrueba(Solicitante solicitante) {
        validarAdministrador(solicitante);
        return Collections.emptyList();
    }

    @Override
    public TipoPruebaDTO crearTipoPrueba(Solicitante solicitante, TipoPruebaDTO tipoPrueba) {
        validarAdministrador(solicitante);
        validarDto(tipoPrueba, "tipoPrueba");
        return tipoPrueba;
    }

    @Override
    public TipoPruebaDTO actualizarTipoPrueba(Solicitante solicitante, Long id, TipoPruebaDTO datos) {
        validarAdministrador(solicitante);
        validarDto(datos, "tipoPrueba");
        validarId(id, "tipoPrueba");
        return datos;
    }

    @Override
    public List<ProtocoloDTO> listarProtocolos(Solicitante solicitante) {
        validarAdministrador(solicitante);
        return Collections.emptyList();
    }

    @Override
    public ProtocoloDTO crearProtocolo(Solicitante solicitante, ProtocoloDTO protocolo) {
        validarAdministrador(solicitante);
        validarDto(protocolo, "protocolo");
        return protocolo;
    }

    @Override
    public ProtocoloDTO actualizarProtocolo(Solicitante solicitante, Long id, ProtocoloDTO datos) {
        validarAdministrador(solicitante);
        validarDto(datos, "protocolo");
        validarId(id, "protocolo");
        return datos;
    }

    @Override
    public List<HorarioAtencionDTO> listarHorarios(Solicitante solicitante) {
        validarAdministrador(solicitante);
        return Collections.emptyList();
    }

    @Override
    public HorarioAtencionDTO crearHorario(Solicitante solicitante, HorarioAtencionDTO horario) {
        validarAdministrador(solicitante);
        validarDto(horario, "horario");
        return horario;
    }

    @Override
    public HorarioAtencionDTO actualizarHorario(Solicitante solicitante, Long id, HorarioAtencionDTO datos) {
        validarAdministrador(solicitante);
        validarDto(datos, "horario");
        validarId(id, "horario");
        return datos;
    }

    @Override
    public List<PoliticaCitacionDTO> listarPoliticas(Solicitante solicitante) {
        validarAdministrador(solicitante);
        return Collections.emptyList();
    }

    @Override
    public PoliticaCitacionDTO crearPolitica(Solicitante solicitante, PoliticaCitacionDTO politica) {
        validarAdministrador(solicitante);
        validarDto(politica, "politica");
        return politica;
    }

    @Override
    public PoliticaCitacionDTO actualizarPolitica(Solicitante solicitante, Long id, PoliticaCitacionDTO datos) {
        validarAdministrador(solicitante);
        validarDto(datos, "politica");
        validarId(id, "politica");
        return datos;
    }

    @Override
    public List<ParametroPrioridadDTO> listarParametrosPrioridad(Solicitante solicitante) {
        validarAdministrador(solicitante);
        return Collections.emptyList();
    }

    @Override
    public ParametroPrioridadDTO configurarParametroPrioridad(Solicitante solicitante,
                                                              ParametroPrioridadDTO parametro) {
        validarAdministrador(solicitante);
        validarDto(parametro, "parametroPrioridad");
        return parametro;
    }

    @Override
    public List<String> listarNivelesPrioridad(Solicitante solicitante) {
        validarAdministrador(solicitante);
        return List.of("BAJA", "MEDIA", "ALTA", "URGENTE");
    }

    private void validarAdministrador(Solicitante solicitante) {
        if (solicitante == null || solicitante.getDni() == null || solicitante.getDni().isBlank()) {
            throw new ApiException(new ApiError("UNAUTHORIZED", "Debe identificarse mediante un DNI válido", 401));
        }
        if (solicitante.getRol() != RolSolicitante.ADMINISTRADOR) {
            throw new ApiException(new ApiError("FORBIDDEN",
                    "Solo el administrador puede configurar parámetros maestros", 403));
        }
    }

    private void validarDto(Object dto, String nombre) {
        if (dto == null) {
            throw new ApiException(new ApiError("CU17_INVALID_REQUEST",
                    nombre + " no puede ser nulo", 400));
        }
    }

    private void validarId(Long id, String nombre) {
        if (id == null) {
            throw new ApiException(new ApiError("CU17_INVALID_REQUEST",
                    "El id de " + nombre + " no puede ser nulo", 400));
        }
    }
}
