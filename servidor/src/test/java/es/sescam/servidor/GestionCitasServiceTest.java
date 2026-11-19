package es.sescam.servidor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.sescam.servidor.auditoria.AuditoriaStub;
import es.sescam.servidor.error.AccesoDenegado;
import es.sescam.servidor.error.EntidadNoEncontrada;
import es.sescam.servidor.error.ValidacionIncorrecta;
import es.sescam.servidor.model.Cita;
import es.sescam.servidor.model.Doctor;
import es.sescam.servidor.model.Paciente;
import es.sescam.servidor.repository.CitaRepository;
import es.sescam.servidor.repository.CitaRepositoryMemoria;
import es.sescam.servidor.seguridad.Rol;
import es.sescam.servidor.seguridad.Usuario;
import es.sescam.servidor.service.CitaService;
import es.sescam.servidor.service.CitaServiceImpl;

/**
 * Pruebas de aceptación del CU-13 sobre los contratos existentes del servidor.
 */
class GestionCitasServiceTest {

    private static final LocalDate FECHA = LocalDate.of(2026, 11, 23);
    private static final LocalTime HORA = LocalTime.of(10, 0);

    private CitaRepository repositorio;
    private AuditoriaStub auditoria;
    private CitaService servicio;
    private Doctor doctor;
    private Usuario administrativo;

    @BeforeEach
    void setUp() {
        repositorio = new CitaRepositoryMemoria();
        auditoria = new AuditoriaStub();
        servicio = new CitaServiceImpl(repositorio, auditoria);
        doctor = new Doctor(2L, "Luis", "Perez", "456", "C1", "Radio", "700", "d@b");
        administrativo = new Usuario("admin-1", "adm", "Adm", EnumSet.of(Rol.ADMINISTRATIVO));
    }

    @Test
    void asignarCitaPendienteConfirmaYAudita() {
        Cita pendiente = repositorio.save(citaPendiente());

        Cita asignada = servicio.asignarCita(pendiente.getId(), FECHA, HORA, administrativo);

        assertEquals("CONFIRMADA", asignada.getEstado());
        assertEquals(FECHA, asignada.getFecha());
        assertEquals(HORA, asignada.getHora());
        assertEquals(1, auditoria.getEventos().size());
        assertTrue(auditoria.getEventos().get(0).contains("admin-1"));
        assertTrue(auditoria.getEventos().get(0).contains("ASIGNAR"));
    }

    @Test
    void asignarCitaRechazaEstadoNoPendiente() {
        Cita confirmada = repositorio.save(citaConEstado("CONFIRMADA"));

        assertThrows(ValidacionIncorrecta.class,
                () -> servicio.asignarCita(confirmada.getId(), FECHA, HORA, administrativo));
        assertTrue(auditoria.getEventos().isEmpty());
    }

    @Test
    void reprogramarCitaCambiaFechaHoraYAudita() {
        Cita confirmada = repositorio.save(citaConEstado("CONFIRMADA"));
        LocalDate nuevaFecha = FECHA.plusDays(1);
        LocalTime nuevaHora = HORA.plusHours(1);

        Cita reprogramada = servicio.reprogramarCita(confirmada.getId(), nuevaFecha, nuevaHora, administrativo);

        assertEquals(nuevaFecha, reprogramada.getFecha());
        assertEquals(nuevaHora, reprogramada.getHora());
        assertEquals(1, auditoria.getEventos().size());
        assertTrue(auditoria.getEventos().get(0).contains("REPROGRAMAR"));
    }

    @Test
    void cancelarCitaLiberaElHuecoDeLaAgenda() {
        Cita primera = repositorio.save(citaPendiente());
        servicio.asignarCita(primera.getId(), FECHA, HORA, administrativo);
        Cita segunda = repositorio.save(citaPendiente());

        assertThrows(ValidacionIncorrecta.class,
                () -> servicio.asignarCita(segunda.getId(), FECHA, HORA, administrativo));

        servicio.cancelarCita(primera.getId(), administrativo);
        Cita asignada = servicio.asignarCita(segunda.getId(), FECHA, HORA, administrativo);

        assertEquals("CONFIRMADA", asignada.getEstado());
        assertEquals("CANCELADA", repositorio.findById(primera.getId()).orElseThrow().getEstado());
    }

    @Test
    void operarSinRolAdministrativoDeniegaYNoModifica() {
        Cita pendiente = repositorio.save(citaPendiente());
        Usuario medico = new Usuario("med-1", "med", "Med", EnumSet.of(Rol.MEDICO));

        assertThrows(AccesoDenegado.class,
                () -> servicio.asignarCita(pendiente.getId(), FECHA, HORA, medico));

        assertEquals("PENDIENTE", repositorio.findById(pendiente.getId()).orElseThrow().getEstado());
        assertTrue(auditoria.getEventos().isEmpty());
    }

    @Test
    void citaInexistenteLanzaEntidadNoEncontrada() {
        assertThrows(EntidadNoEncontrada.class,
                () -> servicio.cancelarCita(999L, administrativo));
        assertTrue(auditoria.getEventos().isEmpty());
    }

    @Test
    void validaDatosObligatorios() {
        Cita pendiente = repositorio.save(citaPendiente());

        assertThrows(ValidacionIncorrecta.class,
                () -> servicio.asignarCita(null, FECHA, HORA, administrativo));
        assertThrows(ValidacionIncorrecta.class,
                () -> servicio.asignarCita(pendiente.getId(), null, HORA, administrativo));
        assertThrows(ValidacionIncorrecta.class,
                () -> servicio.reprogramarCita(pendiente.getId(), FECHA, null, administrativo));
    }

    @Test
    void consultarYCrearCitasDelegaEnElRepositorio() {
        Cita guardada = servicio.crear(citaPendiente());

        assertEquals(guardada.getId(), servicio.obtenerPorId(guardada.getId()).orElseThrow().getId());
        assertEquals(1, servicio.listar().size());
        assertEquals(1, servicio.listarPorPaciente(1L).size());
    }

    @Test
    void listarPorPacienteSinIdLanzaValidacion() {
        assertThrows(ValidacionIncorrecta.class, () -> servicio.listarPorPaciente(null));
    }

    @Test
    void reprogramarCitaNoConfirmadaLanzaValidacion() {
        Cita pendiente = repositorio.save(citaPendiente());

        assertThrows(ValidacionIncorrecta.class,
                () -> servicio.reprogramarCita(pendiente.getId(), FECHA, HORA, administrativo));
    }

    @Test
    void cancelarCitaYaCanceladaLanzaValidacion() {
        Cita cancelada = repositorio.save(citaConEstado("CANCELADA"));

        assertThrows(ValidacionIncorrecta.class,
                () -> servicio.cancelarCita(cancelada.getId(), administrativo));
    }

    @Test
    void asignarCitaSinDoctorNoCompruebaLaAgenda() {
        Paciente paciente = new Paciente(1L, "Ana", "Lopez", "123", null, "600", "a@b", "dir", "HC1");
        Cita sinDoctor = repositorio.save(new Cita(null, null, null, "PENDIENTE", paciente, null));

        Cita asignada = servicio.asignarCita(sinDoctor.getId(), FECHA, HORA, administrativo);

        assertEquals("CONFIRMADA", asignada.getEstado());
    }

    @Test
    void usuarioNuloDeniegaLaOperacion() {
        assertThrows(AccesoDenegado.class,
                () -> servicio.cancelarCita(1L, null));
    }

    private Cita citaPendiente() {
        return citaConEstado("PENDIENTE");
    }

    private Cita citaConEstado(String estado) {
        Paciente paciente = new Paciente(1L, "Ana", "Lopez", "123", null, "600", "a@b", "dir", "HC1");
        return new Cita(null, null, null, estado, paciente, doctor);
    }
}
