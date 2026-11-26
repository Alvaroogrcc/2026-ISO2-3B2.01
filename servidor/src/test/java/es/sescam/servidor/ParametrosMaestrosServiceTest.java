package es.sescam.servidor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.sescam.servidor.auditoria.AuditoriaStub;
import es.sescam.servidor.model.HorarioAtencion;
import es.sescam.servidor.model.Modalidad;
import es.sescam.servidor.model.ParametroPrioridad;
import es.sescam.servidor.model.PoliticaCitacion;
import es.sescam.servidor.model.Prioridad;
import es.sescam.servidor.model.Protocolo;
import es.sescam.servidor.model.TipoPrueba;
import es.sescam.servidor.repository.ParametrosMaestrosRepository;
import es.sescam.servidor.repository.ParametrosMaestrosRepositoryMemoria;
import es.sescam.servidor.seguridad.Rol;
import es.sescam.servidor.seguridad.Usuario;
import es.sescam.servidor.service.ParametrosMaestrosService;
import es.sescam.servidor.service.ParametrosMaestrosServiceImpl;

class ParametrosMaestrosServiceTest {

    private ParametrosMaestrosRepository repositorio;
    private AuditoriaStub auditoria;
    private ParametrosMaestrosService servicio;
    private Usuario administrador;

    @BeforeEach
    void setUp() {
        repositorio = new ParametrosMaestrosRepositoryMemoria();
        auditoria = new AuditoriaStub();
        servicio = new ParametrosMaestrosServiceImpl(repositorio, auditoria);
        administrador = new Usuario("u-admin-1", "admin.sescam", "Admin Sistema",
                EnumSet.of(Rol.ADMINISTRADOR_SISTEMA));
    }

    @Test
    void administradorCreaYEditaTiposDePrueba() {
        TipoPrueba creado = servicio.crearTipoPrueba(administrador, tipoPruebaValido());
        assertNotNull(creado.getId());
        assertEquals(1, servicio.listarTiposPrueba(administrador).size());

        TipoPrueba datos = tipoPruebaValido();
        datos.setNombre("Rx Tórax actualizada");
        TipoPrueba editado = servicio.actualizarTipoPrueba(administrador, creado.getId(), datos);

        assertEquals(creado.getId(), editado.getId());
        assertEquals("Rx Tórax actualizada", editado.getNombre());
        assertTrue(servicio.obtenerTipoPrueba(administrador, creado.getId()).isPresent());
    }

    @Test
    void usuarioNoAdministradorNoPuedeConfigurarNiConsultar() {
        Usuario medico = new Usuario("u-med-1", "medico", "Médico", EnumSet.of(Rol.MEDICO));

        assertThrows(SecurityException.class,
                () -> servicio.crearTipoPrueba(medico, tipoPruebaValido()));
        assertThrows(SecurityException.class,
                () -> servicio.listarTiposPrueba(medico));
        assertThrows(SecurityException.class,
                () -> servicio.listarNivelesPrioridad(medico));
        assertThrows(SecurityException.class,
                () -> servicio.crearHorario(medico, horarioValido()));
        assertTrue(auditoria.getEventos().isEmpty());
    }

    @Test
    void tipoDePruebaConContrasteExigeRequisitosDeSeguridad() {
        TipoPrueba tipo = tipoPruebaValido();
        tipo.setUsoContraste(true);
        tipo.setRequisitosSeguridad(null);

        assertThrows(IllegalArgumentException.class,
                () -> servicio.crearTipoPrueba(administrador, tipo));

        tipo.setRequisitosSeguridad("Dosimetría y vigilancia del personal");
        assertNotNull(servicio.crearTipoPrueba(administrador, tipo).getId());
    }

    @Test
    void tipoDePruebaConConsentimientoExigePreparacionDefinida() {
        TipoPrueba tipo = tipoPruebaValido();
        tipo.setRequiereConsentimientoInformado(true);
        tipo.setPreparacionPrevia("  ");

        assertThrows(IllegalArgumentException.class,
                () -> servicio.crearTipoPrueba(administrador, tipo));

        tipo.setPreparacionPrevia("Firmar consentimiento antes de la prueba");
        assertNotNull(servicio.crearTipoPrueba(administrador, tipo).getId());
    }

    @Test
    void atributosDelCatalogoDebenEstarDefinidos() {
        TipoPrueba tipo = tipoPruebaValido();
        tipo.setUsoContraste(null);
        assertThrows(IllegalArgumentException.class,
                () -> servicio.crearTipoPrueba(administrador, tipo));

        TipoPrueba sinDuracion = tipoPruebaValido();
        sinDuracion.setDuracionEstimadaMinutos(0);
        assertThrows(IllegalArgumentException.class,
                () -> servicio.crearTipoPrueba(administrador, sinDuracion));
    }

    @Test
    void administradorConfiguraProtocolosAsociadosATipoDePrueba() {
        TipoPrueba tipo = servicio.crearTipoPrueba(administrador, tipoPruebaValido());
        Protocolo protocolo = new Protocolo(null, tipo.getId(), "Protocolo Rx",
                "Revisión de la solicitud", "Posicionar al paciente y adquirir la imagen", true);

        Protocolo guardado = servicio.crearProtocolo(administrador, protocolo);
        assertNotNull(guardado.getId());
        assertEquals(tipo.getId(), guardado.getTipoPruebaId());
        assertEquals(1, servicio.listarProtocolos(administrador).size());

        Protocolo sinTipo = new Protocolo(null, 999L, "Protocolo huérfano", "Desc", "Pasos", true);
        assertThrows(NoSuchElementException.class,
                () -> servicio.crearProtocolo(administrador, sinTipo));
    }

    @Test
    void administradorConfiguraHorariosYSeRechazanSolapes() {
        HorarioAtencion guardado = servicio.crearHorario(administrador, horarioValido());
        assertNotNull(guardado.getId());

        HorarioAtencion solapado = new HorarioAtencion(null, DayOfWeek.MONDAY,
                LocalTime.of(9, 0), LocalTime.of(11, 0), 30, true);
        assertThrows(IllegalArgumentException.class,
                () -> servicio.crearHorario(administrador, solapado));

        HorarioAtencion contiguo = new HorarioAtencion(null, DayOfWeek.MONDAY,
                LocalTime.of(10, 0), LocalTime.of(12, 0), 30, true);
        assertNotNull(servicio.crearHorario(administrador, contiguo));

        HorarioAtencion invertido = new HorarioAtencion(null, DayOfWeek.TUESDAY,
                LocalTime.of(12, 0), LocalTime.of(10, 0), 30, true);
        assertThrows(IllegalArgumentException.class,
                () -> servicio.crearHorario(administrador, invertido));
    }

    @Test
    void administradorConfiguraPoliticasDeCitacion() {
        PoliticaCitacion politica = new PoliticaCitacion(null, "Política general",
                "Citas con antelación_controlada", 1, 30, 4, true);
        assertNotNull(servicio.crearPolitica(administrador, politica).getId());

        PoliticaCitacion invalida = new PoliticaCitacion(null, "Inválida", "Desc",
                30, 1, 4, true);
        assertThrows(IllegalArgumentException.class,
                () -> servicio.crearPolitica(administrador, invalida));
    }

    @Test
    void administradorConfiguraNivelesDePrioridadSinCambiarElEnum() {
        ParametroPrioridad urgente = new ParametroPrioridad(null, Prioridad.URGENTE,
                "Atención inmediata", 1, true);
        ParametroPrioridad guardado = servicio.configurarParametroPrioridad(administrador, urgente);
        assertNotNull(guardado.getId());

        ParametroPrioridad reconfigurado = new ParametroPrioridad(null, Prioridad.URGENTE,
                "Atención inmediata", 2, true);
        ParametroPrioridad actualizado = servicio.configurarParametroPrioridad(administrador, reconfigurado);

        assertEquals(guardado.getId(), actualizado.getId());
        assertEquals(2, actualizado.getDiasMaximosEspera());
        assertEquals(1, servicio.listarParametrosPrioridad(administrador).size());
        assertEquals(List.of(Prioridad.values()), servicio.listarNivelesPrioridad(administrador));
    }

    @Test
    void cambiosQuedanAuditadosConUsuarioYAccion() {
        servicio.crearTipoPrueba(administrador, tipoPruebaValido());

        List<String> eventos = auditoria.getEventos();
        assertEquals(2, eventos.size());
        assertTrue(eventos.get(0).startsWith("ACCESO|"));
        assertTrue(eventos.get(0).contains("u-admin-1"));
        assertTrue(eventos.get(0).contains("CREACION"));
        assertTrue(eventos.get(1).contains("CU17_CONFIGURACION"));
    }

    @Test
    void edicionDelCatalogoNoAlteraConfiguracionesYaObtenidas() {
        TipoPrueba original = servicio.crearTipoPrueba(administrador, tipoPruebaValido());
        String nombreAnterior = original.getNombre();

        TipoPrueba datos = tipoPruebaValido();
        datos.setNombre("Nueva configuración");
        servicio.actualizarTipoPrueba(administrador, original.getId(), datos);

        assertEquals(nombreAnterior, original.getNombre());
        assertEquals("Nueva configuración",
                servicio.obtenerTipoPrueba(administrador, original.getId()).orElseThrow().getNombre());
    }

    @Test
    void actualizarEntidadInexistenteFalla() {
        assertThrows(NoSuchElementException.class,
                () -> servicio.actualizarTipoPrueba(administrador, 999L, tipoPruebaValido()));
    }

    private TipoPrueba tipoPruebaValido() {
        return new TipoPrueba(null, "Rx Tórax", Modalidad.RX, "Sala de radiografía", 15,
                "No requiere preparación", false, false, "No aplica", Prioridad.MEDIA, true);
    }

    private HorarioAtencion horarioValido() {
        return new HorarioAtencion(null, DayOfWeek.MONDAY,
                LocalTime.of(8, 0), LocalTime.of(10, 0), 30, true);
    }
}
