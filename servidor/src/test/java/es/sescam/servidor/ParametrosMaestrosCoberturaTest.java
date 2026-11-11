package es.sescam.servidor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumSet;
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

/**
 * Tests de cobertura de las ramas y operaciones de CU-17 que faltaban en la
 * batería inicial: consultas, ediciones, validaciones rechazadas y reglas de solape.
 */
class ParametrosMaestrosCoberturaTest {

    private ParametrosMaestrosService servicio;
    private Usuario administrador;

    @BeforeEach
    void setUp() {
        ParametrosMaestrosRepository repositorio = new ParametrosMaestrosRepositoryMemoria();
        servicio = new ParametrosMaestrosServiceImpl(repositorio, new AuditoriaStub());
        administrador = new Usuario("u-admin-c", "admin.c", "Admin Cobertura",
                EnumSet.of(Rol.ADMINISTRADOR_SISTEMA));
    }

    @Test
    void servicioCubreConsultasYEscriturasDeTodasLasEntidades() {
        TipoPrueba tipo = servicio.crearTipoPrueba(administrador, tipoValido());
        Protocolo protocolo = servicio.crearProtocolo(administrador, protocoloValido(tipo.getId()));
        HorarioAtencion horario = servicio.crearHorario(administrador,
                horarioValido(DayOfWeek.MONDAY, 8, 10));
        PoliticaCitacion politica = servicio.crearPolitica(administrador, politicaValida());
        ParametroPrioridad prioridad = servicio.configurarParametroPrioridad(administrador,
                prioridadValida(Prioridad.ALTA));

        assertEquals(1, servicio.listarTiposPrueba(administrador).size());
        assertEquals(tipo.getId(), servicio.obtenerTipoPrueba(administrador, tipo.getId()).orElseThrow().getId());
        assertEquals(1, servicio.listarProtocolos(administrador).size());
        assertEquals(protocolo.getId(),
                servicio.obtenerProtocolo(administrador, protocolo.getId()).orElseThrow().getId());
        assertEquals(1, servicio.listarHorarios(administrador).size());
        assertEquals(horario.getId(), servicio.obtenerHorario(administrador, horario.getId()).orElseThrow().getId());
        assertEquals(1, servicio.listarPoliticas(administrador).size());
        assertEquals(politica.getId(),
                servicio.obtenerPolitica(administrador, politica.getId()).orElseThrow().getId());
        assertEquals(1, servicio.listarParametrosPrioridad(administrador).size());
        assertEquals(Prioridad.values().length, servicio.listarNivelesPrioridad(administrador).size());

        TipoPrueba datosTipo = tipoValido();
        datosTipo.setNombre("RM actualizada");
        assertEquals("RM actualizada",
                servicio.actualizarTipoPrueba(administrador, tipo.getId(), datosTipo).getNombre());

        Protocolo datosProtocolo = protocoloValido(tipo.getId());
        datosProtocolo.setNombre("Protocolo actualizado");
        assertEquals("Protocolo actualizado",
                servicio.actualizarProtocolo(administrador, protocolo.getId(), datosProtocolo).getNombre());

        HorarioAtencion datosHorario = horarioValido(DayOfWeek.MONDAY, 8, 10);
        datosHorario.setDuracionSlotMinutos(20);
        assertEquals(20, servicio.actualizarHorario(administrador, horario.getId(), datosHorario)
                .getDuracionSlotMinutos());

        PoliticaCitacion datosPolitica = politicaValida();
        datosPolitica.setMaximoCitasPorFranja(6);
        assertEquals(6, servicio.actualizarPolitica(administrador, politica.getId(), datosPolitica)
                .getMaximoCitasPorFranja());

        ParametroPrioridad conId = new ParametroPrioridad(prioridad.getId(), Prioridad.ALTA,
                "Alta actualizada", 5, true);
        assertEquals(5, servicio.configurarParametroPrioridad(administrador, conId).getDiasMaximosEspera());
        assertEquals("Alta actualizada", servicio.listarParametrosPrioridad(administrador)
                .get(0).getDescripcion());
    }

    @Test
    void validacionesRechazanDatosNulosOVacios() {
        assertThrows(SecurityException.class, () -> servicio.listarTiposPrueba(null));

        assertThrows(IllegalArgumentException.class, () -> servicio.crearTipoPrueba(administrador, null));
        rechazarTipo(t -> t.setNombre("  "));
        rechazarTipo(t -> t.setModalidad(null));
        rechazarTipo(t -> t.setDuracionEstimadaMinutos(null));
        rechazarTipo(t -> t.setPreparacionPrevia(null));
        rechazarTipo(t -> t.setUsoContraste(null));
        rechazarTipo(t -> t.setRequiereConsentimientoInformado(null));
        rechazarTipo(t -> t.setPrioridad(null));
        rechazarTipo(t -> t.setPermiteSeguimientoPeriodico(null));

        TipoPrueba existente = servicio.crearTipoPrueba(administrador, tipoValido());
        assertThrows(IllegalArgumentException.class, () -> servicio.crearProtocolo(administrador, null));
        rechazarProtocolo(p -> p.setNombre(" "), existente.getId());
        rechazarProtocolo(p -> p.setDescripcion(" "), existente.getId());
        rechazarProtocolo(p -> p.setPasos(" "), existente.getId());
        rechazarProtocolo(p -> p.setActivo(null), existente.getId());
        rechazarProtocolo(p -> { }, null);

        assertThrows(IllegalArgumentException.class, () -> servicio.crearHorario(administrador, null));
        rechazarHorario(h -> h.setDiaSemana(null));
        rechazarHorario(h -> h.setHoraInicio(null));
        rechazarHorario(h -> h.setHoraFin(null));
        rechazarHorario(h -> h.setDuracionSlotMinutos(null));
        rechazarHorario(h -> h.setDuracionSlotMinutos(0));
        rechazarHorario(h -> h.setActivo(null));

        assertThrows(IllegalArgumentException.class, () -> servicio.crearPolitica(administrador, null));
        rechazarPolitica(p -> p.setNombre(" "));
        rechazarPolitica(p -> p.setDiasAntelacionMinima(null));
        rechazarPolitica(p -> p.setDiasAntelacionMinima(-1));
        rechazarPolitica(p -> p.setDiasAntelacionMaxima(null));
        rechazarPolitica(p -> p.setMaximoCitasPorFranja(null));
        rechazarPolitica(p -> p.setMaximoCitasPorFranja(0));
        rechazarPolitica(p -> p.setActiva(null));

        assertThrows(IllegalArgumentException.class,
                () -> servicio.configurarParametroPrioridad(administrador, null));
        rechazarPrioridad(p -> p.setPrioridad(null));
        rechazarPrioridad(p -> p.setDiasMaximosEspera(null));
        rechazarPrioridad(p -> p.setDiasMaximosEspera(-1));
        rechazarPrioridad(p -> p.setActivo(null));
    }

    private void rechazarTipo(java.util.function.Consumer<TipoPrueba> mutacion) {
        TipoPrueba tipo = tipoValido();
        mutacion.accept(tipo);
        assertThrows(IllegalArgumentException.class, () -> servicio.crearTipoPrueba(administrador, tipo));
    }

    private void rechazarProtocolo(java.util.function.Consumer<Protocolo> mutacion, Long tipoId) {
        Protocolo protocolo = protocoloValido(tipoId);
        mutacion.accept(protocolo);
        assertThrows(IllegalArgumentException.class, () -> servicio.crearProtocolo(administrador, protocolo));
    }

    private void rechazarHorario(java.util.function.Consumer<HorarioAtencion> mutacion) {
        HorarioAtencion horario = horarioValido(DayOfWeek.FRIDAY, 8, 10);
        mutacion.accept(horario);
        assertThrows(IllegalArgumentException.class, () -> servicio.crearHorario(administrador, horario));
    }

    private void rechazarPolitica(java.util.function.Consumer<PoliticaCitacion> mutacion) {
        PoliticaCitacion politica = politicaValida();
        mutacion.accept(politica);
        assertThrows(IllegalArgumentException.class, () -> servicio.crearPolitica(administrador, politica));
    }

    private void rechazarPrioridad(java.util.function.Consumer<ParametroPrioridad> mutacion) {
        ParametroPrioridad prioridad = prioridadValida(Prioridad.MEDIA);
        mutacion.accept(prioridad);
        assertThrows(IllegalArgumentException.class,
                () -> servicio.configurarParametroPrioridad(administrador, prioridad));
    }

    @Test
    void horariosInactivosContiguosYCambiosDeDiaNoSeConsideranSolapes() {
        servicio.crearHorario(administrador, new HorarioAtencion(null, DayOfWeek.MONDAY,
                LocalTime.of(8, 0), LocalTime.of(10, 0), 30, false));
        HorarioAtencion activo = servicio.crearHorario(administrador,
                horarioValido(DayOfWeek.MONDAY, 9, 11));
        assertNotNull(activo.getId());

        HorarioAtencion martes = servicio.crearHorario(administrador,
                horarioValido(DayOfWeek.TUESDAY, 8, 10));
        servicio.crearHorario(administrador, horarioValido(DayOfWeek.WEDNESDAY, 8, 10));
        assertNotNull(servicio.crearHorario(administrador, horarioValido(DayOfWeek.WEDNESDAY, 10, 12)));
        servicio.crearHorario(administrador, horarioValido(DayOfWeek.THURSDAY, 12, 14));
        assertNotNull(servicio.crearHorario(administrador, horarioValido(DayOfWeek.THURSDAY, 8, 10)));

        assertThrows(IllegalArgumentException.class,
                () -> servicio.actualizarHorario(administrador, martes.getId(),
                        horarioValido(DayOfWeek.MONDAY, 9, 11)));
    }

    @Test
    void configurarPrioridadConOtroNivelYConIdInexistente() {
        ParametroPrioridad urgente = servicio.configurarParametroPrioridad(administrador,
                prioridadValida(Prioridad.URGENTE));
        ParametroPrioridad media = servicio.configurarParametroPrioridad(administrador,
                prioridadValida(Prioridad.MEDIA));
        assertNotEquals(urgente.getId(), media.getId());

        assertThrows(NoSuchElementException.class,
                () -> servicio.configurarParametroPrioridad(administrador,
                        new ParametroPrioridad(999L, Prioridad.ALTA, "Alta", 3, true)));
    }

    @Test
    void actualizarEntidadesInexistentesFalla() {
        TipoPrueba existente = servicio.crearTipoPrueba(administrador, tipoValido());
        assertThrows(NoSuchElementException.class, () -> servicio.actualizarProtocolo(administrador, 999L,
                protocoloValido(existente.getId())));
        assertThrows(NoSuchElementException.class, () -> servicio.actualizarHorario(administrador, 999L,
                horarioValido(DayOfWeek.MONDAY, 8, 10)));
        assertThrows(NoSuchElementException.class, () -> servicio.actualizarPolitica(administrador, 999L,
                politicaValida()));
    }

    @Test
    void modelosCu17PermitenLeerEscribirTodosSusAtributos() {
        TipoPrueba tipo = new TipoPrueba();
        tipo.setId(1L);
        tipo.setNombre("Rx");
        tipo.setModalidad(Modalidad.RX);
        tipo.setEquipoSalaRequeridos("Sala Rx");
        tipo.setDuracionEstimadaMinutos(15);
        tipo.setPreparacionPrevia("Ninguna");
        tipo.setUsoContraste(true);
        tipo.setRequiereConsentimientoInformado(true);
        tipo.setRequisitosSeguridad("Dosimetría");
        tipo.setPrioridad(Prioridad.ALTA);
        tipo.setPermiteSeguimientoPeriodico(true);
        assertEquals(1L, tipo.getId());
        assertEquals("Rx", tipo.getNombre());
        assertEquals(Modalidad.RX, tipo.getModalidad());
        assertEquals("Sala Rx", tipo.getEquipoSalaRequeridos());
        assertEquals(15, tipo.getDuracionEstimadaMinutos());
        assertEquals("Ninguna", tipo.getPreparacionPrevia());
        assertTrue(tipo.getUsoContraste());
        assertTrue(tipo.getRequiereConsentimientoInformado());
        assertEquals("Dosimetría", tipo.getRequisitosSeguridad());
        assertEquals(Prioridad.ALTA, tipo.getPrioridad());
        assertTrue(tipo.getPermiteSeguimientoPeriodico());

        Protocolo protocolo = new Protocolo();
        protocolo.setId(2L);
        protocolo.setTipoPruebaId(1L);
        protocolo.setNombre("Protocolo");
        protocolo.setDescripcion("Desc");
        protocolo.setPasos("Pasos");
        protocolo.setActivo(true);
        assertEquals(2L, protocolo.getId());
        assertEquals(1L, protocolo.getTipoPruebaId());
        assertEquals("Protocolo", protocolo.getNombre());
        assertEquals("Desc", protocolo.getDescripcion());
        assertEquals("Pasos", protocolo.getPasos());
        assertTrue(protocolo.getActivo());

        HorarioAtencion horario = new HorarioAtencion();
        horario.setId(3L);
        horario.setDiaSemana(DayOfWeek.MONDAY);
        horario.setHoraInicio(LocalTime.of(8, 0));
        horario.setHoraFin(LocalTime.of(10, 0));
        horario.setDuracionSlotMinutos(30);
        horario.setActivo(true);
        assertEquals(3L, horario.getId());
        assertEquals(DayOfWeek.MONDAY, horario.getDiaSemana());
        assertEquals(LocalTime.of(8, 0), horario.getHoraInicio());
        assertEquals(LocalTime.of(10, 0), horario.getHoraFin());
        assertEquals(30, horario.getDuracionSlotMinutos());
        assertTrue(horario.getActivo());

        PoliticaCitacion politica = new PoliticaCitacion();
        politica.setId(4L);
        politica.setNombre("Política");
        politica.setDescripcion("Desc");
        politica.setDiasAntelacionMinima(1);
        politica.setDiasAntelacionMaxima(30);
        politica.setMaximoCitasPorFranja(4);
        politica.setActiva(true);
        assertEquals(4L, politica.getId());
        assertEquals("Política", politica.getNombre());
        assertEquals("Desc", politica.getDescripcion());
        assertEquals(1, politica.getDiasAntelacionMinima());
        assertEquals(30, politica.getDiasAntelacionMaxima());
        assertEquals(4, politica.getMaximoCitasPorFranja());
        assertTrue(politica.getActiva());

        ParametroPrioridad prioridad = new ParametroPrioridad();
        prioridad.setId(5L);
        prioridad.setPrioridad(Prioridad.URGENTE);
        prioridad.setDescripcion("Urgente");
        prioridad.setDiasMaximosEspera(1);
        prioridad.setActivo(true);
        assertEquals(5L, prioridad.getId());
        assertEquals(Prioridad.URGENTE, prioridad.getPrioridad());
        assertEquals("Urgente", prioridad.getDescripcion());
        assertEquals(1, prioridad.getDiasMaximosEspera());
        assertTrue(prioridad.getActivo());
    }

    private TipoPrueba tipoValido() {
        return new TipoPrueba(null, "RM", Modalidad.RM, "Sala RM", 30, "Ayuno", false, false,
                "No aplica", Prioridad.MEDIA, true);
    }

    private Protocolo protocoloValido(Long tipoId) {
        return new Protocolo(null, tipoId, "Protocolo RM", "Desc", "Pasos", true);
    }

    private HorarioAtencion horarioValido(DayOfWeek dia, int inicio, int fin) {
        return new HorarioAtencion(null, dia, LocalTime.of(inicio, 0), LocalTime.of(fin, 0), 30, true);
    }

    private PoliticaCitacion politicaValida() {
        return new PoliticaCitacion(null, "Política", "Desc", 1, 30, 4, true);
    }

    private ParametroPrioridad prioridadValida(Prioridad prioridad) {
        return new ParametroPrioridad(null, prioridad, "Descripción", 3, true);
    }
}
