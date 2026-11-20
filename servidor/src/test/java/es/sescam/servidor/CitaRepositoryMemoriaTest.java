package es.sescam.servidor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    private Cita nuevaCita(Long id) {
        Paciente paciente = new Paciente(1L, "Ana", "Lopez", "123", null, "600", "a@b", "dir", "HC1");
        Doctor doctor = new Doctor(2L, "Luis", "Perez", "456", "C1", "Radio", "700", "d@b");
        return new Cita(id, LocalDate.now(), LocalTime.of(10, 0), "PENDIENTE", paciente, doctor);
    }
}
