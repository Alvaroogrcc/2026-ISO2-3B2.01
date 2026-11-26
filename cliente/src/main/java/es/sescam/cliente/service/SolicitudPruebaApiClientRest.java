package es.sescam.cliente.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import es.sescam.cliente.model.PrioridadSolicitud;
import es.sescam.cliente.model.SolicitudPruebaDTO;
import es.sescam.cliente.model.TipoPruebaDTO;
import es.sescam.cliente.service.auth.RolSolicitante;
import es.sescam.cliente.service.auth.Solicitante;
import es.sescam.cliente.service.error.ApiError;
import es.sescam.cliente.service.error.ApiException;

/**
 * Implementación de contrato del cliente de solicitudes de prueba (CU-06).
 * <p>
 * En esta iteración no se realizan llamadas HTTP reales: se aplican las
 * validaciones del contrato, se utiliza un catálogo de demostración y se
 * devuelven errores coherentes ({@code 400}, {@code 401} o {@code 403}). La
 * sustitución por un cliente HTTP real se hará cuando exista el borde REST del
 * servidor.
 * </p>
 */
public class SolicitudPruebaApiClientRest implements SolicitudPruebaApiClient {

    /** Versión del catálogo de demostración. */
    public static final String VERSION_CATALOGO = "v0.1.0";

    private final String baseUrl;
    private final List<TipoPruebaDTO> tiposPrueba;

    /**
     * Crea el cliente con la URL base de la API y su catálogo de demostración.
     *
     * @param baseUrl URL base de la API
     */
    public SolicitudPruebaApiClientRest(String baseUrl) {
        this.baseUrl = Objects.requireNonNull(baseUrl, "baseUrl");
        List<TipoPruebaDTO> catalogo = new ArrayList<>();
        catalogo.add(new TipoPruebaDTO(1L, "Radiografía de tórax", "RX", 10,
                "No requiere preparación previa.", false, false));
        catalogo.add(new TipoPruebaDTO(2L, "TAC abdominal con contraste", "TAC", 30,
                "Ayuno de 6 horas y analítica renal reciente.", true, true));
        catalogo.add(new TipoPruebaDTO(3L, "Resonancia magnética lumbar", "RM", 45,
                "Retirar objetos metálicos y acudir sin maquillaje.", false, false));
        this.tiposPrueba = Collections.unmodifiableList(catalogo);
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public List<TipoPruebaDTO> listarTiposPrueba(Solicitante solicitante) {
        validarSolicitante(solicitante);
        if (solicitante.getRol() != RolSolicitante.DOCTOR
                && solicitante.getRol() != RolSolicitante.ADMINISTRADOR) {
            throw new ApiException(new ApiError("FORBIDDEN",
                    "Solo doctores o administradores pueden consultar el catálogo de pruebas", 403));
        }
        return new ArrayList<>(tiposPrueba);
    }

    @Override
    public SolicitudPruebaDTO prescribir(Solicitante solicitante, SolicitudPruebaDTO solicitud) {
        validarSolicitante(solicitante);
        exigirDoctor(solicitante);
        validarSolicitud(solicitud);
        TipoPruebaDTO tipoPrueba = buscarTipoPrueba(solicitud.getTipoPrueba().getId());
        if (Boolean.TRUE.equals(tipoPrueba.getRequiereConsentimientoInformado())
                && !solicitud.isConsentimientoInformadoAceptado()) {
            throw new ApiException(new ApiError("SOLICITUD_INVALID_REQUEST",
                    "La prueba requiere la aceptación del consentimiento informado", 400));
        }
        return copiar(solicitud, tipoPrueba, solicitante);
    }

    @Override
    public List<SolicitudPruebaDTO> listarPendientes(Solicitante solicitante) {
        validarSolicitante(solicitante);
        if (solicitante.getRol() != RolSolicitante.DOCTOR
                && solicitante.getRol() != RolSolicitante.ADMINISTRADOR) {
            throw new ApiException(new ApiError("FORBIDDEN",
                    "Solo doctores o administradores pueden consultar solicitudes pendientes", 403));
        }
        return Collections.emptyList();
    }

    private void validarSolicitante(Solicitante solicitante) {
        if (solicitante == null || solicitante.getDni() == null || solicitante.getDni().isBlank()) {
            throw new ApiException(new ApiError("UNAUTHORIZED", "Debe identificarse mediante un DNI válido", 401));
        }
    }

    private void exigirDoctor(Solicitante solicitante) {
        if (solicitante.getRol() != RolSolicitante.DOCTOR) {
            throw new ApiException(new ApiError("FORBIDDEN",
                    "Solo los médicos pueden prescribir pruebas diagnósticas", 403));
        }
    }

    private void validarSolicitud(SolicitudPruebaDTO solicitud) {
        if (solicitud == null) {
            throw solicitudInvalida("Los datos de la solicitud son obligatorios");
        }
        if (solicitud.getPacienteId() == null) {
            throw solicitudInvalida("El paciente es obligatorio");
        }
        if (solicitud.getDoctorId() == null) {
            throw solicitudInvalida("El médico es obligatorio");
        }
        if (solicitud.getTipoPrueba() == null || solicitud.getTipoPrueba().getId() == null) {
            throw solicitudInvalida("El tipo de prueba es obligatorio");
        }
        if (solicitud.getPrioridad() == null) {
            throw solicitudInvalida("La prioridad es obligatoria");
        }
        if (estaVacio(solicitud.getIndicacionClinica())) {
            throw solicitudInvalida("La indicación clínica es obligatoria");
        }
        if (estaVacio(solicitud.getJustificacion())) {
            throw solicitudInvalida("La justificación es obligatoria");
        }
    }

    private TipoPruebaDTO buscarTipoPrueba(Long id) {
        for (TipoPruebaDTO tipo : tiposPrueba) {
            if (id.equals(tipo.getId())) {
                return tipo;
            }
        }
        throw solicitudInvalida("El tipo de prueba indicado no existe en el catálogo");
    }

    private SolicitudPruebaDTO copiar(SolicitudPruebaDTO origen, TipoPruebaDTO tipoPrueba,
                                      Solicitante solicitante) {
        SolicitudPruebaDTO copia = new SolicitudPruebaDTO();
        copia.setId(origen.getId());
        copia.setFechaSolicitud(Instant.now());
        copia.setPrioridad(origen.getPrioridad());
        copia.setIndicacionClinica(origen.getIndicacionClinica().trim());
        copia.setJustificacion(origen.getJustificacion().trim());
        copia.setEstado("PENDIENTE");
        copia.setPacienteId(origen.getPacienteId());
        copia.setDoctorId(origen.getDoctorId());
        copia.setTipoPrueba(tipoPrueba);
        copia.setConsentimientoInformadoAceptado(origen.isConsentimientoInformadoAceptado());
        copia.setPreparacionPrevia(tipoPrueba.getPreparacionPrevia());
        copia.setUsoContraste(tipoPrueba.getUsoContraste());
        copia.setRequiereConsentimientoInformado(tipoPrueba.getRequiereConsentimientoInformado());
        copia.setVersionCatalogo(VERSION_CATALOGO);
        copia.setUsuarioValidador(solicitante.getDni());
        return copia;
    }

    private ApiException solicitudInvalida(String mensaje) {
        return new ApiException(new ApiError("SOLICITUD_INVALID_REQUEST", mensaje, 400));
    }

    private boolean estaVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}
