package es.sescam.cliente.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.sescam.cliente.model.ImagenMedicaDTO;
import es.sescam.cliente.model.InformeClinicoDTO;
import es.sescam.cliente.service.auth.RolSolicitante;
import es.sescam.cliente.service.auth.Solicitante;
import es.sescam.cliente.service.error.ApiError;
import es.sescam.cliente.service.error.ApiException;

/**
 * Implementación de contrato del cliente de informes clínicos (CU-09).
 * <p>
 * En esta iteración no se realizan llamadas HTTP reales: se aplican las
 * validaciones del contrato, se devuelven datos de demostración y se generan
 * errores coherentes ({@code 400}, {@code 401}, {@code 403} o {@code 404}). La
 * sustitución por un cliente HTTP real se hará cuando exista el borde REST del
 * servidor.
 * </p>
 */
public class InformeClinicoApiClientRest implements InformeClinicoApiClient {

    private final String baseUrl;

    /**
     * Crea el cliente con la URL base de la API.
     *
     * @param baseUrl URL base de la API
     */
    public InformeClinicoApiClientRest(String baseUrl) {
        this.baseUrl = Objects.requireNonNull(baseUrl, "baseUrl");
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public InformeClinicoDTO crear(Solicitante solicitante, InformeClinicoDTO informe) {
        validarSolicitante(solicitante);
        exigirDoctor(solicitante);
        validarInforme(informe);
        return copiar(informe);
    }

    @Override
    public InformeClinicoDTO actualizar(Solicitante solicitante, Long id, InformeClinicoDTO informe) {
        validarSolicitante(solicitante);
        exigirDoctor(solicitante);
        exigirIdentificador(id, "El identificador del informe es obligatorio");
        throw informeNoEncontrado();
    }

    @Override
    public void firmar(Solicitante solicitante, Long id) {
        validarSolicitante(solicitante);
        exigirDoctor(solicitante);
        exigirIdentificador(id, "El identificador del informe es obligatorio");
        throw informeNoEncontrado();
    }

    @Override
    public InformeClinicoDTO obtenerPorId(Solicitante solicitante, Long id) {
        validarSolicitante(solicitante);
        exigirDoctorOAdministrador(solicitante);
        exigirIdentificador(id, "El identificador del informe es obligatorio");
        throw informeNoEncontrado();
    }

    @Override
    public List<InformeClinicoDTO> listarPorPaciente(Solicitante solicitante, Long pacienteId) {
        validarSolicitante(solicitante);
        exigirDoctorOAdministrador(solicitante);
        exigirIdentificador(pacienteId, "El identificador del paciente es obligatorio");
        return new ArrayList<>();
    }

    @Override
    public List<ImagenMedicaDTO> listarImagenes(Solicitante solicitante, Long solicitudId) {
        validarSolicitante(solicitante);
        exigirDoctor(solicitante);
        exigirIdentificador(solicitudId, "El identificador de la solicitud es obligatorio");
        String estudioId = "EST-" + solicitudId;
        Instant adquisicion = Instant.now();
        List<ImagenMedicaDTO> imagenes = new ArrayList<>();
        imagenes.add(new ImagenMedicaDTO(1L, estudioId, "1", "DICOM",
                "dicom://pacs/" + estudioId + "/serie-1/imagen-1.dcm",
                "Corte axial", adquisicion));
        imagenes.add(new ImagenMedicaDTO(2L, estudioId, "2", "DICOM",
                "dicom://pacs/" + estudioId + "/serie-2/imagen-1.dcm",
                "Corte coronal", adquisicion));
        imagenes.add(new ImagenMedicaDTO(3L, estudioId, "3", "DICOM",
                "dicom://pacs/" + estudioId + "/serie-3/imagen-1.dcm",
                "Corte sagital", adquisicion));
        return imagenes;
    }

    private InformeClinicoDTO copiar(InformeClinicoDTO origen) {
        InformeClinicoDTO copia = new InformeClinicoDTO();
        copia.setId(origen.getId());
        copia.setFecha(Instant.now());
        copia.setContenido(origen.getContenido().trim());
        copia.setConclusion(origen.getConclusion() == null ? null : origen.getConclusion().trim());
        copia.setEstado("BORRADOR");
        copia.setSolicitudId(origen.getSolicitudId());
        copia.setPacienteId(origen.getPacienteId());
        copia.setDoctorId(origen.getDoctorId());
        return copia;
    }

    private void validarInforme(InformeClinicoDTO informe) {
        if (informe == null) {
            throw solicitudInvalida("Los datos del informe son obligatorios");
        }
        if (informe.getSolicitudId() == null) {
            throw solicitudInvalida("La solicitud de prueba es obligatoria");
        }
        if (estaVacio(informe.getContenido())) {
            throw solicitudInvalida("El contenido del informe es obligatorio");
        }
    }

    private void validarSolicitante(Solicitante solicitante) {
        if (solicitante == null || solicitante.getDni() == null || solicitante.getDni().isBlank()) {
            throw new ApiException(new ApiError("UNAUTHORIZED",
                    "Debe identificarse mediante un DNI válido", 401));
        }
    }

    private void exigirDoctor(Solicitante solicitante) {
        if (solicitante.getRol() != RolSolicitante.DOCTOR) {
            throw new ApiException(new ApiError("FORBIDDEN",
                    "Solo los médicos pueden gestionar informes clínicos", 403));
        }
    }

    private void exigirDoctorOAdministrador(Solicitante solicitante) {
        if (solicitante.getRol() != RolSolicitante.DOCTOR
                && solicitante.getRol() != RolSolicitante.ADMINISTRADOR) {
            throw new ApiException(new ApiError("FORBIDDEN",
                    "Solo médicos o administradores pueden consultar informes clínicos", 403));
        }
    }

    private void exigirIdentificador(Long id, String mensaje) {
        if (id == null) {
            throw solicitudInvalida(mensaje);
        }
    }

    private ApiException solicitudInvalida(String mensaje) {
        return new ApiException(new ApiError("INFORME_INVALID_REQUEST", mensaje, 400));
    }

    private ApiException informeNoEncontrado() {
        return new ApiException(new ApiError("INFORME_NO_ENCONTRADO",
                "No se ha encontrado el informe clínico indicado", 404));
    }

    private boolean estaVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}
