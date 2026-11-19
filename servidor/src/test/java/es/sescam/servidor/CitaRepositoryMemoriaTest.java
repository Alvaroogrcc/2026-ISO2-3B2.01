package es.sescam.servidor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import es.sescam.servidor.model.Cita;
import es.sescam.servidor.model.Doctor;
import es.sescam.servidor.model.Paciente;
import es.sescam.servidor.repository.CitaRepository;
import es.sescam.servidor.repository.CitaRepositoryMemoria;

class CitaRepositoryMemoriaTest {

    @Test
    void saveAsignaIdYGuarda() {
        CitaRepository repo = new CitaRepositoryMemoria();
        Cita cita = nuevaCita(null);
        Cita guardada = repo.save(cita);
        assertNotNull(guardada.getId());
        assertEquals(1, repo.findAll().size());
    }

    @Test
    void saveNoDuplicaAlActualizarMismaCita() {
        CitaRepository repo = new CitaRepositoryMemoria();
        Cita cita = nuevaCita(null);
        Cita guardada = repo.save(cita);
        guardada.setEstado("CONFIRMADA");
        repo.save(guardada);
        assertEquals(1, repo.findAll().size());
        Optional<Cita> encontrada = repo.findById(guardada.getId());
        assertTrue(encontrada.isPresent());
        assertEquals("CONFIRMADA", encontrada.get().getEstado());
    }

    @Test
    void findByIdDevuelveVacioSiNoExiste() {
        CitaRepository repo = new CitaRepositoryMemoria();
        assertTrue(repo.findById(999L).isEmpty());
        assertTrue(repo.findById(null).isEmpty());
    }

    @Test
    void findByDoctorIdAndFechaFiltraPorAgenda() {
        CitaRepository repo = new CitaRepositoryMemoria();
        LocalDate fecha = LocalDate.of(2026, 11, 23);
        Cita guardada = repo.save(new Cita(null, fecha, LocalTime.of(10, 0), "CONFIRMADA",
                new Paciente(1L, "Ana", "Lopez", "123", null, "600", "a@b", "dir", "HC1"),
                new Doctor(2L, "Luis", "Perez", "456", "C1", "Radio", "700", "d@b")));

        assertEquals(1, repo.findByDoctorIdAndFecha(2L, fecha).size());
        assertEquals(guardada.getId(), repo.findByDoctorIdAndFecha(2L, fecha).get(0).getId());
        assertTrue(repo.findByDoctorIdAndFecha(99L, fecha).isEmpty());
        assertTrue(repo.findByDoctorIdAndFecha(null, fecha).isEmpty());
    }

    @Test
    void findByPacienteIdConIdNuloDevuelveVacio() {
        CitaRepository repo = new CitaRepositoryMemoria();
        repo.save(nuevaCita(null));
        assertTrue(repo.findByPacienteId(null).isEmpty());
    }

    @Test
    void findByDoctorIdAndFechaConFechaNulaDevuelveVacio() {
        CitaRepository repo = new CitaRepositoryMemoria();
        repo.save(nuevaCita(null));
        assertTrue(repo.findByDoctorIdAndFecha(2L, null).isEmpty());
    }

    @Test
    void saveRechazaCitaNula() {
        CitaRepository repo = new CitaRepositoryMemoria();
        assertThrows(IllegalArgumentException.class, () -> repo.save(null));
    }

    private Cita nuevaCita(Long id) {
        Paciente paciente = new Paciente(1L, "Ana", "Lopez", "123", null, "600", "a@b", "dir", "HC1");
        Doctor doctor = new Doctor(2L, "Luis", "Perez", "456", "C1", "Radio", "700", "d@b");
        return new Cita(id, LocalDate.now(), LocalTime.of(10, 0), "PENDIENTE", paciente, doctor);
    }
}
