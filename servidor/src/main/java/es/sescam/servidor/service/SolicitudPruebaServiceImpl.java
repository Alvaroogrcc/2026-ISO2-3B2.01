package es.sescam.servidor.service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import es.sescam.servidor.auditoria.Auditoria;
import es.sescam.servidor.catalogo.CatalogoPruebas;
import es.sescam.servidor.error.AccesoDenegado;
import es.sescam.servidor.error.EntidadNoEncontrada;
import es.sescam.servidor.error.ValidacionIncorrecta;
import es.sescam.servidor.hce.ServicioHCE;
import es.sescam.servidor.model.Doctor;
import es.sescam.servidor.model.EstadoSolicitud;
import es.sescam.servidor.model.Paciente;
import es.sescam.servidor.model.Prioridad;
import es.sescam.servidor.model.SolicitudPrueba;
import es.sescam.servidor.model.TipoPrueba;
import es.sescam.servidor.repository.DoctorRepository;
import es.sescam.servidor.repository.PacienteRepository;
import es.sescam.servidor.repository.SolicitudPruebaRepository;
import es.sescam.servidor.seguridad.Rol;
import es.sescam.servidor.seguridad.Usuario;

/**
 * Implementación del servicio de prescripción de pruebas diagnósticas (CU-06).
 * <p>
 * Aplica el control de acceso por rol, valida los datos clínicos y los atributos
 * del catálogo, registra la trazabilidad de la validación, audita cada acceso,
 * persiste la solicitud y la envía a la historia clínica electrónica simulada.
 * </p>
 */
public class SolicitudPruebaServiceImpl implements SolicitudPruebaService {

    private final SolicitudPruebaRepository solicitudPruebaRepository;
    private final PacienteRepository pacienteRepository;
    private final DoctorRepository doctorRepository;
    private final CatalogoPruebas catalogoPruebas;
    private final ServicioHCE servicioHCE;
    private final Auditoria auditoria;

    /**
     * Crea el servicio de solicitudes de prueba con sus dependencias.
     *
     * @param solicitudPruebaRepository repositorio de solicitudes de prueba
     * @param pacienteRepository repositorio de pacientes
     * @param doctorRepository repositorio de médicos
     * @param catalogoPruebas catálogo de pruebas diagnósticas
     * @param servicioHCE integración con la historia clínica electrónica
     * @param auditoria servicio de auditoría
     */
    public SolicitudPruebaServiceImpl(SolicitudPruebaRepository solicitudPruebaRepository,
                                      PacienteRepository pacienteRepository,
                                      DoctorRepository doctorRepository,
                                      CatalogoPruebas catalogoPruebas,
                                      ServicioHCE servicioHCE,
                                      Auditoria auditoria) {
        this.solicitudPruebaRepository = Objects.requireNonNull(solicitudPruebaRepository, "solicitudPruebaRepository");
        this.pacienteRepository = Objects.requireNonNull(pacienteRepository, "pacienteRepository");
        this.doctorRepository = Objects.requireNonNull(doctorRepository, "doctorRepository");
        this.catalogoPruebas = Objects.requireNonNull(catalogoPruebas, "catalogoPruebas");
        this.servicioHCE = Objects.requireNonNull(servicioHCE, "servicioHCE");
        this.auditoria = Objects.requireNonNull(auditoria, "auditoria");
    }

    @Override
    public SolicitudPrueba prescribir(Usuario usuario, SolicitudPrueba solicitud) {
        exigirAlgunRol(usuario, "Solo los médicos pueden prescribir pruebas diagnósticas", Rol.MEDICO);
        validarSolicitud(solicitud);
        Paciente paciente = resolverPaciente(solicitud);
        Doctor doctor = resolverDoctor(solicitud);
        TipoPrueba tipoPrueba = resolverTipoPrueba(solicitud);
        boolean requiereConsentimiento = Boolean.TRUE.equals(tipoPrueba.getRequiereConsentimientoInformado());
        if (requiereConsentimiento && !solicitud.isConsentimientoInformadoAceptado()) {
            throw new ValidacionIncorrecta(
                    "La prueba requiere la aceptación del consentimiento informado");
        }
        solicitud.setPaciente(paciente);
        solicitud.setDoctor(doctor);
        solicitud.setTipoPrueba(tipoPrueba);
        solicitud.setIndicacionClinica(solicitud.getIndicacionClinica().trim());
        solicitud.setJustificacion(solicitud.getJustificacion().trim());
        solicitud.setPreparacionPrevia(tipoPrueba.getPreparacionPrevia());
        solicitud.setUsoContraste(tipoPrueba.getUsoContraste());
        solicitud.setRequiereConsentimientoInformado(tipoPrueba.getRequiereConsentimientoInformado());
        solicitud.setVersionCatalogo(catalogoPruebas.getVersion());
        solicitud.setUsuarioValidador(usuario.getNombreUsuario());
        solicitud.setFechaSolicitud(Instant.now());
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        SolicitudPrueba guardada = solicitudPruebaRepository.save(solicitud);
        auditar(usuario, "solicitudPrueba/" + guardada.getId(), "PRESCRIPCION");
        servicioHCE.enviarSolicitud(guardada);
        return guardada;
    }

    @Override
    public List<SolicitudPrueba> listarPendientes(Usuario usuario) {
        exigirAlgunRol(usuario, "No tiene permisos para consultar solicitudes de prueba",
                Rol.MEDICO, Rol.ADMINISTRATIVO, Rol.ADMINISTRADOR_SISTEMA);
        auditar(usuario, "solicitudPrueba", "LECTURA");
        return solicitudPruebaRepository.findByEstado(EstadoSolicitud.PENDIENTE);
    }

    @Override
    public Optional<SolicitudPrueba> obtenerPorId(Usuario usuario, Long id) {
        exigirAlgunRol(usuario, "No tiene permisos para consultar solicitudes de prueba",
                Rol.MEDICO, Rol.ADMINISTRATIVO, Rol.ADMINISTRADOR_SISTEMA);
        if (id == null) {
            throw new ValidacionIncorrecta("El identificador de la solicitud es obligatorio");
        }
        auditar(usuario, "solicitudPrueba/" + id, "LECTURA");
        return solicitudPruebaRepository.findById(id);
    }

    @Override
    public List<TipoPrueba> listarTiposPrueba(Usuario usuario) {
        exigirAlgunRol(usuario, "No tiene permisos para consultar el catálogo de pruebas",
                Rol.MEDICO, Rol.ADMINISTRATIVO, Rol.ADMINISTRADOR_SISTEMA);
        return catalogoPruebas.listar();
    }

    private void validarSolicitud(SolicitudPrueba solicitud) {
        if (solicitud == null) {
            throw new ValidacionIncorrecta("Los datos de la solicitud son obligatorios");
        }
        Prioridad prioridad = solicitud.getPrioridad();
        if (prioridad != Prioridad.URGENTE
                && prioridad != Prioridad.PREFERENTE
                && prioridad != Prioridad.ORDINARIA) {
            throw new ValidacionIncorrecta(
                    "La prioridad debe ser URGENTE, PREFERENTE u ORDINARIA");
        }
        if (esBlanco(solicitud.getIndicacionClinica())) {
            throw new ValidacionIncorrecta("La indicación clínica es obligatoria");
        }
        if (esBlanco(solicitud.getJustificacion())) {
            throw new ValidacionIncorrecta("La justificación es obligatoria");
        }
    }

    private Paciente resolverPaciente(SolicitudPrueba solicitud) {
        if (solicitud.getPaciente() == null || solicitud.getPaciente().getId() == null) {
            throw new EntidadNoEncontrada("No se ha encontrado el paciente indicado");
        }
        Paciente paciente = pacienteRepository.findById(solicitud.getPaciente().getId())
                .orElseThrow(() -> new EntidadNoEncontrada("No se ha encontrado el paciente indicado"));
        if (!paciente.estaActivo()) {
            throw new ValidacionIncorrecta("El paciente no está activo");
        }
        return paciente;
    }

    private Doctor resolverDoctor(SolicitudPrueba solicitud) {
        if (solicitud.getDoctor() == null || solicitud.getDoctor().getId() == null) {
            throw new EntidadNoEncontrada("No se ha encontrado el médico indicado");
        }
        return doctorRepository.findById(solicitud.getDoctor().getId())
                .orElseThrow(() -> new EntidadNoEncontrada("No se ha encontrado el médico indicado"));
    }

    private TipoPrueba resolverTipoPrueba(SolicitudPrueba solicitud) {
        if (solicitud.getTipoPrueba() == null || solicitud.getTipoPrueba().getId() == null) {
            throw new EntidadNoEncontrada("No se ha encontrado el tipo de prueba indicado");
        }
        return catalogoPruebas.obtenerPorId(solicitud.getTipoPrueba().getId())
                .orElseThrow(() -> new EntidadNoEncontrada(
                        "No se ha encontrado el tipo de prueba indicado"));
    }

    private void exigirAlgunRol(Usuario usuario, String mensaje, Rol... roles) {
        if (usuario == null) {
            throw new AccesoDenegado(mensaje);
        }
        for (Rol rol : roles) {
            if (usuario.tieneRol(rol)) {
                return;
            }
        }
        throw new AccesoDenegado(mensaje);
    }

    private void auditar(Usuario usuario, String recurso, String accion) {
        auditoria.registrarAcceso(usuario.getId(), recurso, accion, Instant.now());
    }

    private boolean esBlanco(String valor) {
        return valor == null || valor.isBlank();
    }
}
