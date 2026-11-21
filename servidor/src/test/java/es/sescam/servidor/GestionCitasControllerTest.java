package es.sescam.servidor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import es.sescam.servidor.controller.CitaController;
import es.sescam.servidor.controller.CitaControllerImpl;
import es.sescam.servidor.model.Cita;
import es.sescam.servidor.seguridad.Rol;
import es.sescam.servidor.seguridad.Usuario;
import es.sescam.servidor.service.CitaService;

/**
 * Pruebas de la puerta de entrada del CU-13. El servicio se sustituye por un doble
 * para comprobar que el controller solo delega.
 */
class GestionCitasControllerTest {

    @Test
    void delegaAsignarReprogramarYCancelarEnElServicio() {
        Cita cita = new Cita(1L, null, null, "PENDIENTE", null, null);
        ServicioEspia servicio = new ServicioEspia(cita);
        CitaController controller = new CitaControllerImpl(servicio);
        Usuario administrativo = new Usuario("admin-1", "adm", "Adm", EnumSet.of(Rol.ADMINISTRATIVO));
        LocalDate fecha = LocalDate.of(2026, 11, 23);
        LocalTime hora = LocalTime.of(10, 0);

        assertSame(cita, controller.asignarCita(1L, fecha, hora, administrativo));
        assertSame(cita, controller.reprogramarCita(1L, fecha.plusDays(1), hora, administrativo));
        assertSame(cita, controller.cancelarCita(1L, administrativo));

        assertEquals(List.of("ASIGNAR", "REPROGRAMAR", "CANCELAR"), servicio.operaciones);
        assertEquals(1L, servicio.ultimoCitaId);
        assertEquals(fecha.plusDays(1), servicio.ultimaFecha);
        assertEquals(hora, servicio.ultimaHora);
        assertSame(administrativo, servicio.ultimoUsuario);
    }

    @Test
    void delegaLasConsultasEnElServicio() {
        Cita cita = new Cita(1L, null, null, "PENDIENTE", null, null);
        CitaController controller = new CitaControllerImpl(new ServicioEspia(cita));

        assertSame(cita, controller.crearCita(cita));
        assertSame(cita, controller.obtenerCita(1L).orElseThrow());
        assertEquals(1, controller.listarCitas().size());
        assertEquals(1, controller.listarCitasPorPaciente(10L).size());
    }

    static class ServicioEspia implements CitaService {

        final List<String> operaciones = new ArrayList<>();
        final Cita cita;
        Long ultimoCitaId;
        LocalDate ultimaFecha;
        LocalTime ultimaHora;
        Usuario ultimoUsuario;

        ServicioEspia(Cita cita) {
            this.cita = cita;
        }

        private Cita registrar(String operacion, Long citaId, LocalDate fecha, LocalTime hora, Usuario usuario) {
            operaciones.add(operacion);
            ultimoCitaId = citaId;
            ultimaFecha = fecha;
            ultimaHora = hora;
            ultimoUsuario = usuario;
            return cita;
        }

        @Override
        public Cita crear(Cita cita) {
            return cita;
        }

        @Override
        public Optional<Cita> obtenerPorId(Long id) {
            return Optional.of(cita);
        }

        @Override
        public List<Cita> listar() {
            return List.of(cita);
        }

        @Override
        public List<Cita> listarPorPaciente(Long pacienteId) {
            return List.of(cita);
        }

        @Override
        public Cita asignarCita(Long citaId, LocalDate fecha, LocalTime hora, Usuario usuario) {
            return registrar("ASIGNAR", citaId, fecha, hora, usuario);
        }

        @Override
        public Cita reprogramarCita(Long citaId, LocalDate fecha, LocalTime hora, Usuario usuario) {
            return registrar("REPROGRAMAR", citaId, fecha, hora, usuario);
        }

        @Override
        public Cita cancelarCita(Long citaId, Usuario usuario) {
            return registrar("CANCELAR", citaId, ultimaFecha, ultimaHora, usuario);
        }
    }
}
