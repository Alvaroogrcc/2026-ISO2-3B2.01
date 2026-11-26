package es.sescam.servidor;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.Objects;

import es.sescam.servidor.admision.ServicioAdmisionStub;
import es.sescam.servidor.auditoria.AuditoriaStub;
import es.sescam.servidor.autorizacion.ServicioAutorizacionClinicaStub;
import es.sescam.servidor.catalogo.CatalogoPruebas;
import es.sescam.servidor.catalogo.CatalogoPruebasStub;
import es.sescam.servidor.controller.CitaController;
import es.sescam.servidor.controller.CitaControllerImpl;
import es.sescam.servidor.controller.DoctorController;
import es.sescam.servidor.controller.DoctorControllerImpl;
import es.sescam.servidor.controller.GestionUsuariosController;
import es.sescam.servidor.controller.GestionUsuariosControllerImpl;
import es.sescam.servidor.controller.InformeClinicoController;
import es.sescam.servidor.controller.InformeClinicoControllerImpl;
import es.sescam.servidor.controller.PacienteController;
import es.sescam.servidor.controller.PacienteControllerImpl;
import es.sescam.servidor.controller.ParametrosMaestrosController;
import es.sescam.servidor.controller.ParametrosMaestrosControllerImpl;
import es.sescam.servidor.controller.SolicitudPruebaController;
import es.sescam.servidor.controller.SolicitudPruebaControllerImpl;
import es.sescam.servidor.hce.ServicioHCEStub;
import es.sescam.servidor.imagenes.ServicioImagenesStub;
import es.sescam.servidor.model.Cita;
import es.sescam.servidor.model.Doctor;
import es.sescam.servidor.model.EstadoInforme;
import es.sescam.servidor.model.HorarioAtencion;
import es.sescam.servidor.model.InformeClinico;
import es.sescam.servidor.model.Modalidad;
import es.sescam.servidor.model.Paciente;
import es.sescam.servidor.model.ParametroPrioridad;
import es.sescam.servidor.model.PoliticaCitacion;
import es.sescam.servidor.model.Prioridad;
import es.sescam.servidor.model.Protocolo;
import es.sescam.servidor.model.SolicitudPrueba;
import es.sescam.servidor.model.TipoPrueba;
import es.sescam.servidor.repository.CitaRepositoryMemoria;
import es.sescam.servidor.repository.DoctorRepositoryMemoria;
import es.sescam.servidor.repository.InformeClinicoRepositoryMemoria;
import es.sescam.servidor.repository.PacienteRepositoryMemoria;
import es.sescam.servidor.repository.ParametrosMaestrosRepositoryMemoria;
import es.sescam.servidor.repository.SolicitudPruebaRepositoryMemoria;
import es.sescam.servidor.repository.UsuarioRepositoryMemoria;
import es.sescam.servidor.seguridad.ControlAcceso;
import es.sescam.servidor.seguridad.ControlAccesoBasico;
import es.sescam.servidor.seguridad.Rol;
import es.sescam.servidor.seguridad.ServicioAutenticacionSSOMemoria;
import es.sescam.servidor.seguridad.Usuario;
import es.sescam.servidor.service.CitaServiceImpl;
import es.sescam.servidor.service.DoctorServiceImpl;
import es.sescam.servidor.service.GestionUsuariosServiceImpl;
import es.sescam.servidor.service.InformeClinicoServiceImpl;
import es.sescam.servidor.service.PacienteServiceImpl;
import es.sescam.servidor.service.ParametrosMaestrosServiceImpl;
import es.sescam.servidor.service.SolicitudPruebaServiceImpl;

/**
 * Punto de entrada del servidor de radiodiagnóstico.
 * <p>
 * Construye en un único lugar (regla de dependencia 5 de {@code AGENTS.md}) todas
 * las implementaciones concretas: repositorios en memoria, auditoría, control de
 * acceso, SSO simulado, integraciones {@code Stub}, servicios y controladores.
 * Después ejecuta una demostración por consola de los casos de uso disponibles
 * con datos ficticios y anonimizados.
 * </p>
 * <p>
 * Este main no inicia ningún servidor HTTP: la API REST se incorporará en el
 * borde cuando se ratifique la decisión pendiente D-002 de {@code DECISIONS.md}.
 * </p>
 */
public final class ServidorMain {

    private static final String SEPARADOR = "------------------------------------------------------------";
    private static final String TOKEN_ADMIN = "token-u-admin";

    private final AuditoriaStub auditoria = new AuditoriaStub();
    private final ControlAcceso controlAcceso;
    private final ServicioAutenticacionSSOMemoria sso = new ServicioAutenticacionSSOMemoria();
    private final CatalogoPruebas catalogo = new CatalogoPruebasStub();
    private final ServicioHCEStub servicioHCE = new ServicioHCEStub();
    private final ServicioAdmisionStub servicioAdmision = new ServicioAdmisionStub();

    private final CitaRepositoryMemoria citaRepository = new CitaRepositoryMemoria();
    private final DoctorRepositoryMemoria doctorRepository = new DoctorRepositoryMemoria();
    private final InformeClinicoRepositoryMemoria informeRepository = new InformeClinicoRepositoryMemoria();
    private final PacienteRepositoryMemoria pacienteRepository = new PacienteRepositoryMemoria();
    private final ParametrosMaestrosRepositoryMemoria parametrosRepository =
            new ParametrosMaestrosRepositoryMemoria();
    private final SolicitudPruebaRepositoryMemoria solicitudRepository =
            new SolicitudPruebaRepositoryMemoria();
    private final UsuarioRepositoryMemoria usuarioRepository = new UsuarioRepositoryMemoria();

    private final CitaController citaController;
    private final DoctorController doctorController;
    private final GestionUsuariosController gestionUsuariosController;
    private final InformeClinicoController informeController;
    private final PacienteController pacienteController;
    private final ParametrosMaestrosController parametrosController;
    private final SolicitudPruebaController solicitudController;

    private final Usuario administrador;
    private final Usuario medico;
    private final Usuario administrativo;

    /**
     * Arranca el servidor de demostración.
     *
     * @param args argumentos de línea de comandos (no se utilizan)
     */
    public static void main(String[] args) {
        new ServidorMain().ejecutar();
    }

    /**
     * Crea el grafo de dependencias y los usuarios de demostración.
     */
    private ServidorMain() {
        this.controlAcceso = new ControlAccesoBasico(auditoria);

        this.citaController = new CitaControllerImpl(
                new CitaServiceImpl(citaRepository, auditoria));
        this.doctorController = new DoctorControllerImpl(
                new DoctorServiceImpl(doctorRepository));
        this.gestionUsuariosController = new GestionUsuariosControllerImpl(
                new GestionUsuariosServiceImpl(usuarioRepository, controlAcceso, auditoria),
                controlAcceso);
        this.informeController = new InformeClinicoControllerImpl(
                new InformeClinicoServiceImpl(informeRepository, solicitudRepository,
                        new ServicioImagenesStub(), servicioHCE,
                        new ServicioAutorizacionClinicaStub(solicitudRepository), auditoria));
        this.pacienteController = new PacienteControllerImpl(
                new PacienteServiceImpl(pacienteRepository, auditoria, servicioAdmision));
        this.parametrosController = new ParametrosMaestrosControllerImpl(
                new ParametrosMaestrosServiceImpl(parametrosRepository, auditoria));
        this.solicitudController = new SolicitudPruebaControllerImpl(
                new SolicitudPruebaServiceImpl(solicitudRepository, pacienteRepository,
                        doctorRepository, catalogo, servicioHCE, auditoria));

        this.administrador = new Usuario("u-admin", "admin.demo", "Administrador Demo",
                EnumSet.of(Rol.ADMINISTRADOR_SISTEMA));
        usuarioRepository.save(administrador);
        sso.registrarSesion(TOKEN_ADMIN, administrador);

        this.medico = registrarUsuario("u-medico", "medico.demo", "Médico Demo", Rol.MEDICO);
        this.administrativo = registrarUsuario("u-admision", "admision.demo", "Admisión Demo",
                Rol.ADMINISTRATIVO);
        registrarUsuario("u-paciente", "paciente.demo", "Paciente Demo", Rol.PACIENTE);
    }

    /**
     * Ejecuta la demostración de los casos de uso implementados.
     */
    private void ejecutar() {
        System.out.println("SESCAM - Servidor de radiodiagnóstico (demostración)");
        System.out.println("Repositorios en memoria / SSO simulado / integraciones Stub");
        System.out.println(SEPARADOR);

        ejecutarEtapa("Autenticación SSO simulada", this::demoAutenticacion);
        ejecutarEtapa("CU-18 Gestión de usuarios, roles y permisos", this::demoUsuarios);
        ejecutarEtapa("CU-12 Gestión de pacientes", this::demoPacientes);
        ejecutarEtapa("CU-01/CU-02 Gestión de médicos", this::demoDoctores);
        ejecutarEtapa("CU-17 Parámetros maestros", this::demoParametros);
        ejecutarEtapa("CU-06 Prescripción de pruebas", this::demoSolicitudes);
        ejecutarEtapa("CU-07 Gestión de citas", this::demoCitas);
        ejecutarEtapa("CU-09 Informes clínicos e imágenes", this::demoInformes);

        System.out.println(SEPARADOR);
        System.out.println("Eventos de auditoría registrados: " + auditoria.getEventos().size());
        System.out.println("Fin de la demostración.");
    }

    private void demoAutenticacion() {
        System.out.println("  Usuarios registrados: " + usuarioRepository.findAll().size());
        System.out.println("  Token SSO de demostración autenticado: "
                + sso.autenticar(TOKEN_ADMIN).isPresent());
        System.out.println("  Permisos del rol MEDICO: " + controlAcceso.permisosDeRol(Rol.MEDICO));
    }

    private void demoUsuarios() {
        Usuario creado = gestionUsuariosController.crearUsuario(administrador, "u-demo-extra",
                "usuario.demo", "Usuario Extra Demo", EnumSet.of(Rol.ADMINISTRATIVO));
        System.out.println("  Usuario creado: " + creado.getNombreUsuario() + " (" + creado.getRoles() + ")");
        System.out.println("  Usuarios en el repositorio: "
                + gestionUsuariosController.listarUsuarios(administrador).size());
        System.out.println("  Permisos consultados para ADMINISTRATIVO: "
                + gestionUsuariosController.consultarPermisosRol(administrador, Rol.ADMINISTRATIVO));
    }

    private void demoPacientes() {
        Paciente paciente = new Paciente(null, "Paciente", "Anonimo Demo", "00000001A",
                LocalDate.of(1980, 1, 15), "600000001", "paciente.demo@example.invalid",
                "Calle Ficticia, 1", "HC-DEMO-0001");
        Paciente guardado = Objects.requireNonNull(
                pacienteController.crearPaciente(administrativo, paciente), "paciente guardado");
        System.out.println("  Alta de paciente con id: " + guardado.getId());
        System.out.println("  Pacientes listados: " + pacienteController.listarPacientes(medico).size());
        System.out.println("  Búsqueda por DNI: "
                + pacienteController.buscarPacientePorDni(medico, "00000001A").isPresent());
    }

    private void demoDoctores() {
        Doctor doctor = doctorController.crearDoctor(new Doctor(null, "Medico", "Demo", "00000002B",
                "COL-DEMO-001", "Radiología", "600000002", "medico.demo@example.invalid"));
        System.out.println("  Alta de médico con id: " + doctor.getId());
        System.out.println("  Médicos listados: " + doctorController.listarDoctores().size());
    }

    private void demoParametros() {
        TipoPrueba tipo = parametrosController.crearTipoPrueba(administrador, new TipoPrueba(null,
                "Ecografía abdominal demo", Modalidad.ECOGRAFIA, "Sala ECO-DEMO", 20,
                "Acudir con vejiga llena.", false, false, "Sin requisitos especiales",
                Prioridad.ORDINARIA, false));
        System.out.println("  Tipo de prueba creado con id: " + tipo.getId());

        Protocolo protocolo = parametrosController.crearProtocolo(administrador, new Protocolo(null,
                tipo.getId(), "Protocolo ecografía demo", "Protocolo ficticio de demostración",
                "1. Verificar identidad; 2. Realizar exploración.", true));
        System.out.println("  Protocolo creado con id: " + protocolo.getId());

        HorarioAtencion horario = parametrosController.crearHorario(administrador,
                new HorarioAtencion(null, DayOfWeek.MONDAY, LocalTime.of(8, 0),
                        LocalTime.of(14, 0), 15, true));
        System.out.println("  Horario creado con id: " + horario.getId());

        PoliticaCitacion politica = parametrosController.crearPolitica(administrador,
                new PoliticaCitacion(null, "Política demo", "Política ficticia de demostración",
                        1, 30, 4, true));
        System.out.println("  Política de citación creada con id: " + politica.getId());

        ParametroPrioridad parametro = parametrosController.configurarParametroPrioridad(
                administrador, new ParametroPrioridad(null, Prioridad.URGENTE,
                        "Atención inmediata ficticia", 1, true));
        System.out.println("  Parámetro de prioridad configurado con id: " + parametro.getId());
        System.out.println("  Niveles de prioridad disponibles: "
                + parametrosController.listarNivelesPrioridad(administrador));
    }

    private void demoSolicitudes() {
        System.out.println("  Catálogo de pruebas (stub): "
                + solicitudController.listarTiposPrueba(medico).size() + " tipos");

        Paciente paciente = Objects.requireNonNull(
                pacienteController.buscarPacientePorDni(medico, "00000001A").orElse(null),
                "paciente de demostración");
        Doctor doctor = Objects.requireNonNull(doctorController.listarDoctores().stream().findFirst()
                .orElse(null), "médico de demostración");
        TipoPrueba tipo = Objects.requireNonNull(catalogo.obtenerPorId(1L).orElse(null),
                "tipo de prueba de demostración");

        SolicitudPrueba solicitud = new SolicitudPrueba();
        solicitud.setPaciente(paciente);
        solicitud.setDoctor(doctor);
        solicitud.setTipoPrueba(tipo);
        solicitud.setPrioridad(Prioridad.ORDINARIA);
        solicitud.setIndicacionClinica("Control de demostración");
        solicitud.setJustificacion("Datos ficticios de demostración");
        solicitud.setConsentimientoInformadoAceptado(false);

        SolicitudPrueba guardada = solicitudController.prescribir(medico, solicitud);
        System.out.println("  Solicitud prescrita con id: " + guardada.getId()
                + " (catálogo " + guardada.getVersionCatalogo() + ")");
        System.out.println("  Solicitudes pendientes: "
                + solicitudController.listarPendientes(administrativo).size());
        System.out.println("  Solicitudes enviadas a HCE (stub): "
                + servicioHCE.getSolicitudesEnviadas().size());
    }

    private void demoCitas() {
        Paciente paciente = Objects.requireNonNull(
                pacienteController.buscarPacientePorDni(administrativo, "00000001A").orElse(null),
                "paciente de demostración");
        Doctor doctor = Objects.requireNonNull(doctorController.listarDoctores().stream().findFirst()
                .orElse(null), "médico de demostración");
        LocalDate fecha = LocalDate.now().plusDays(7);
        LocalTime hora = LocalTime.of(10, 30);

        Cita creada = citaController.crearCita(new Cita(null, fecha, hora, "PENDIENTE",
                paciente, doctor));
        System.out.println("  Cita creada con id: " + creada.getId() + " (" + creada.getEstado() + ")");

        Cita asignada = citaController.asignarCita(creada.getId(), fecha, hora, administrativo);
        System.out.println("  Cita asignada: " + asignada.getEstado());

        Cita reprogramada = citaController.reprogramarCita(asignada.getId(), fecha,
                LocalTime.of(11, 0), administrativo);
        System.out.println("  Cita reprogramada a las " + reprogramada.getHora());

        Cita cancelada = citaController.cancelarCita(reprogramada.getId(), administrativo);
        System.out.println("  Cita cancelada: " + cancelada.getEstado());
    }

    private void demoInformes() {
        SolicitudPrueba solicitud = Objects.requireNonNull(
                solicitudController.listarPendientes(medico).stream().findFirst().orElse(null),
                "solicitud de demostración");

        InformeClinico borrador = informeController.crear(medico, new InformeClinico(null,
                Instant.now(), "Exploración ficticia de demostración", "Sin hallazgos ficticios",
                EstadoInforme.BORRADOR, solicitud, solicitud.getPaciente(), solicitud.getDoctor()));
        System.out.println("  Informe creado con id: " + borrador.getId() + " (" + borrador.getEstado() + ")");

        InformeClinico firmado = informeController.firmar(medico, borrador.getId());
        System.out.println("  Informe firmado: " + firmado.getEstado());
        System.out.println("  Informes del paciente: "
                + informeController.listarPorPaciente(medico, solicitud.getPaciente().getId()).size());
        System.out.println("  Imágenes del estudio (stub): "
                + informeController.listarImagenes(medico, solicitud.getId()).size());
        System.out.println("  Informes enviados a HCE (stub): "
                + servicioHCE.getInformesEnviados().size());
    }

    private Usuario registrarUsuario(String id, String nombreUsuario, String nombreCompleto, Rol rol) {
        Usuario usuario = Objects.requireNonNull(gestionUsuariosController.crearUsuario(
                administrador, id, nombreUsuario, nombreCompleto, EnumSet.of(rol)),
                "usuario de demostración");
        sso.registrarSesion("token-" + id, usuario);
        return usuario;
    }

    private void ejecutarEtapa(String titulo, Runnable accion) {
        System.out.println("[" + titulo + "]");
        try {
            accion.run();
        } catch (RuntimeException excepcion) {
            System.out.println("  No se pudo completar la etapa: " + excepcion.getMessage());
        }
    }
}
