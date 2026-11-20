package es.sescam.servidor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import es.sescam.servidor.model.Paciente;
import es.sescam.servidor.repository.PacienteRepository;
import es.sescam.servidor.repository.PacienteRepositoryMemoria;

class PacienteRepositoryMemoriaTest {

    @Test
    void saveAsignaIdYNoDuplica() {
        PacienteRepository repo = new PacienteRepositoryMemoria();
        Paciente paciente = new Paciente(null, "Ana", "Lopez", "123", null, "600", "a@b", "dir", "HC1");
        Paciente guardado = repo.save(paciente);
        assertNotNull(guardado.getId());
        guardado.setDireccion("nueva");
        repo.save(guardado);
        assertEquals(1, repo.findAll().size());
        assertTrue(repo.findById(guardado.getId()).isPresent());
    }
}
