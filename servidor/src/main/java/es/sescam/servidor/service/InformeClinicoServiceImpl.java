package es.sescam.servidor.service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import es.sescam.servidor.auditoria.Auditoria;
import es.sescam.servidor.autorizacion.ServicioAutorizacionClinica;
import es.sescam.servidor.error.AccesoDenegado;
import es.sescam.servidor.error.EntidadNoEncontrada;
import es.sescam.servidor.error.ValidacionIncorrecta;
import es.sescam.servidor.hce.ServicioHCE;
import es.sescam.servidor.imagenes.ServicioImagenes;
import es.sescam.servidor.model.EstadoInforme;
import es.sescam.servidor.model.ImagenMedica;
import es.sescam.servidor.model.InformeClinico;
import es.sescam.servidor.model.Paciente;
import es.sescam.servidor.model.SolicitudPrueba;
import es.sescam.servidor.repository.InformeClinicoRepository;
import es.sescam.servidor.repository.SolicitudPruebaRepository;
import es.sescam.servidor.seguridad.Rol;
import es.sescam.servidor.seguridad.Usuario;

/**
 * Implementación del servicio de informes clínicos e imágenes médicas (CU-09).
 * <p>
 * Aplica el control de acceso por rol, comprueba la relación clínica del médico
 * con el paciente, valida los datos del informe, audita cada acceso y envía los
 * informes firmados a la historia clínica electrónica simulada.
 * </p>
 */
public class InformeClinicoServiceImpl implements InformeClinicoService {

    private final InformeClinicoRepository informeClinicoRepository;
    private final SolicitudPruebaRepository solicitudPruebaRepository;
    private final ServicioImagenes servicioImagenes;
    private final ServicioHCE servicioHCE;
    private final ServicioAutorizacionClinica servicioAutorizacionClinica;
    private final Auditoria auditoria;

    /**
     * Crea el servicio de informes clínicos con sus dependencias.
     *
     * @param informeClinicoRepository repositorio de informes clínicos
     * @param solicitudPruebaRepository repositorio de solicitudes de prueba
     * @param servicioImagenes servicio de imágenes médicas
     * @param servicioHCE integración con la historia clínica electrónica
     * @param servicioAutorizacionClinica servicio de autorización clínica
     * @param auditoria servicio de auditoría
     */
    public InformeClinicoServiceImpl(InformeClinicoRepository informeClinicoRepository,
                                     SolicitudPruebaRepository solicitudPruebaRepository,
                                     ServicioImagenes servicioImagenes,
                                     ServicioHCE servicioHCE,
                                     ServicioAutorizacionClinica servicioAutorizacionClinica,
                                     Auditoria auditoria) {
        this.informeClinicoRepository = Objects.requireNonNull(informeClinicoRepository,
                "informeClinicoRepository");
        this.solicitudPruebaRepository = Objects.requireNonNull(solicitudPruebaRepository,
                "solicitudPruebaRepository");
        this.servicioImagenes = Objects.requireNonNull(servicioImagenes, "servicioImagenes");
        this.servicioHCE = Objects.requireNonNull(servicioHCE, "servicioHCE");
        this.servicioAutorizacionClinica = Objects.requireNonNull(servicioAutorizacionClinica,
                "servicioAutorizacionClinica");
        this.auditoria = Objects.requireNonNull(auditoria, "auditoria");
    }

    @Override
    public InformeClinico crear(Usuario usuario, InformeClinico informe) {
        exigirMedico(usuario, "Solo los médicos pueden crear informes clínicos");
        if (informe == null) {
            throw new ValidacionIncorrecta("Los datos del informe son obligatorios");
        }
        SolicitudPrueba solicitud = resolverSolicitud(informe);
        Paciente paciente = obtenerPaciente(solicitud);
        comprobarRelacionClinica(usuario, paciente.getId());
        validarContenido(informe.getContenido());
        informe.setId(null);
        informe.setSolicitud(solicitud);
        informe.setPaciente(paciente);
        informe.setDoctor(solicitud.getDoctor());
        informe.setEstado(EstadoInforme.BORRADOR);
        informe.setFecha(Instant.now());
        informe.setContenido(informe.getContenido().trim());
        normalizarConclusion(informe);
        InformeClinico guardado = informeClinicoRepository.save(informe);
        auditar(usuario, "informeClinico/" + guardado.getId(), "ALTA");
        return guardado;
    }

    @Override
    public InformeClinico actualizar(Usuario usuario, Long id, InformeClinico datos) {
        exigirMedico(usuario, "Solo los médicos pueden modificar informes clínicos");
        InformeClinico existente = resolverInforme(id);
        if (existente.getEstado() == EstadoInforme.FIRMADO) {
            throw new ValidacionIncorrecta("El informe está firmado y no puede modificarse");
        }
        comprobarRelacionClinica(usuario, obtenerPaciente(existente).getId());
        if (datos == null) {
            throw new ValidacionIncorrecta("Los datos del informe son obligatorios");
        }
        validarContenido(datos.getContenido());
        existente.setContenido(datos.getContenido().trim());
        existente.setConclusion(datos.getConclusion() == null ? null : datos.getConclusion().trim());
        InformeClinico guardado = informeClinicoRepository.save(existente);
        auditar(usuario, "informeClinico/" + guardado.getId(), "MODIFICACION");
        return guardado;
    }

    @Override
    public InformeClinico firmar(Usuario usuario, Long id) {
        exigirMedico(usuario, "Solo los médicos pueden firmar informes clínicos");
        InformeClinico existente = resolverInforme(id);
        if (existente.getEstado() != EstadoInforme.BORRADOR) {
            throw new ValidacionIncorrecta("El informe no está en estado BORRADOR");
        }
        comprobarRelacionClinica(usuario, obtenerPaciente(existente).getId());
        existente.setEstado(EstadoInforme.FIRMADO);
        InformeClinico guardado = informeClinicoRepository.save(existente);
        auditar(usuario, "informeClinico/" + guardado.getId(), "FIRMA");
        servicioHCE.enviarInforme(guardado);
        return guardado;
    }

    @Override
    public Optional<InformeClinico> obtenerPorId(Usuario usuario, Long id) {
        exigirRolLectura(usuario);
        if (id == null) {
            throw new ValidacionIncorrecta("El identificador del informe es obligatorio");
        }
        auditar(usuario, "informeClinico/" + id, "LECTURA");
        Optional<InformeClinico> encontrado = informeClinicoRepository.findById(id);
        if (encontrado.isPresent() && !esAdministrador(usuario)) {
            comprobarRelacionClinica(usuario, obtenerPaciente(encontrado.get()).getId());
        }
        return encontrado;
    }

    @Override
    public List<InformeClinico> listarPorPaciente(Usuario usuario, Long pacienteId) {
        exigirRolLectura(usuario);
        if (pacienteId == null) {
            throw new ValidacionIncorrecta("El identificador del paciente es obligatorio");
        }
        if (!esAdministrador(usuario)) {
            comprobarRelacionClinica(usuario, pacienteId);
        }
        auditar(usuario, "informeClinico/paciente/" + pacienteId, "LECTURA");
        return informeClinicoRepository.findByPacienteId(pacienteId);
    }

    @Override
    public List<ImagenMedica> listarImagenes(Usuario usuario, Long solicitudId) {
        exigirMedico(usuario, "Solo los médicos pueden consultar imágenes médicas");
        if (solicitudId == null) {
            throw new ValidacionIncorrecta("El identificador de la solicitud es obligatorio");
        }
        SolicitudPrueba solicitud = solicitudPruebaRepository.findById(solicitudId)
                .orElseThrow(() -> new EntidadNoEncontrada(
                        "No se ha encontrado la solicitud de prueba indicada"));
        Paciente paciente = obtenerPaciente(solicitud);
        comprobarRelacionClinica(usuario, paciente.getId());
        auditar(usuario, "solicitudPrueba/" + solicitudId + "/imagenes", "LECTURA_IMAGENES");
        return servicioImagenes.listarImagenes("EST-" + solicitudId);
    }

    private SolicitudPrueba resolverSolicitud(InformeClinico informe) {
        if (informe.getSolicitud() == null || informe.getSolicitud().getId() == null) {
            throw new EntidadNoEncontrada("No se ha encontrado la solicitud de prueba indicada");
        }
        Long solicitudId = informe.getSolicitud().getId();
        return solicitudPruebaRepository.findById(solicitudId)
                .orElseThrow(() -> new EntidadNoEncontrada(
                        "No se ha encontrado la solicitud de prueba indicada"));
    }

    private InformeClinico resolverInforme(Long id) {
        if (id == null) {
            throw new EntidadNoEncontrada("No se ha encontrado el informe indicado");
        }
        return informeClinicoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontrada("No se ha encontrado el informe indicado"));
    }

    private Paciente obtenerPaciente(SolicitudPrueba solicitud) {
        if (solicitud.getPaciente() == null || solicitud.getPaciente().getId() == null) {
            throw new EntidadNoEncontrada("No se ha encontrado el paciente de la solicitud");
        }
        return solicitud.getPaciente();
    }

    private Paciente obtenerPaciente(InformeClinico informe) {
        if (informe.getPaciente() == null || informe.getPaciente().getId() == null) {
            throw new EntidadNoEncontrada("No se ha encontrado el paciente del informe");
        }
        return informe.getPaciente();
    }

    private void comprobarRelacionClinica(Usuario usuario, Long pacienteId) {
        if (pacienteId == null
                || !servicioAutorizacionClinica.tieneRelacionClinica(usuario, pacienteId)) {
            throw new AccesoDenegado(
                    "No tiene relación clínica autorizada con el paciente indicado");
        }
    }

    private void validarContenido(String contenido) {
        if (contenido == null || contenido.isBlank()) {
            throw new ValidacionIncorrecta("El contenido del informe es obligatorio");
        }
    }

    private void normalizarConclusion(InformeClinico informe) {
        if (informe.getConclusion() != null) {
            informe.setConclusion(informe.getConclusion().trim());
        }
    }

    private void exigirMedico(Usuario usuario, String mensaje) {
        if (usuario == null || !usuario.tieneRol(Rol.MEDICO)) {
            throw new AccesoDenegado(mensaje);
        }
    }

    private void exigirRolLectura(Usuario usuario) {
        if (usuario == null
                || !(usuario.tieneRol(Rol.MEDICO)
                || usuario.tieneRol(Rol.ADMINISTRADOR_SISTEMA))) {
            throw new AccesoDenegado("No tiene permisos para consultar informes clínicos");
        }
    }

    private boolean esAdministrador(Usuario usuario) {
        return usuario != null && usuario.tieneRol(Rol.ADMINISTRADOR_SISTEMA);
    }

    private void auditar(Usuario usuario, String recurso, String accion) {
        auditoria.registrarAcceso(usuario.getId(), recurso, accion, Instant.now());
    }
}
