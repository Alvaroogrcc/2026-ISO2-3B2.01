package es.sescam.servidor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.sescam.servidor.model.HorarioAtencion;
import es.sescam.servidor.model.ParametroPrioridad;
import es.sescam.servidor.model.PoliticaCitacion;
import es.sescam.servidor.model.Protocolo;
import es.sescam.servidor.model.TipoPrueba;
import es.sescam.servidor.repository.ParametrosMaestrosRepository;
import es.sescam.servidor.repository.ParametrosMaestrosRepositoryMemoria;

/**
 * Tests del contrato de persistencia en memoria del CU-17.
 */
class ParametrosMaestrosRepositoryMemoriaTest {

    private ParametrosMaestrosRepository repositorio;

    @BeforeEach
    void setUp() {
        repositorio = new ParametrosMaestrosRepositoryMemoria();
    }

    @Test
    void buscarConIdNuloDevuelveVacioEnLasCincoColecciones() {
        assertTrue(repositorio.findTipoPruebaById(null).isEmpty());
        assertTrue(repositorio.findProtocoloById(null).isEmpty());
        assertTrue(repositorio.findHorarioById(null).isEmpty());
        assertTrue(repositorio.findPoliticaById(null).isEmpty());
        assertTrue(repositorio.findParametroPrioridadById(null).isEmpty());
    }

    @Test
    void guardarEntidadNulaFallaEnLasCincoColecciones() {
        assertThrows(IllegalArgumentException.class, () -> repositorio.saveTipoPrueba(null));
        assertThrows(IllegalArgumentException.class, () -> repositorio.saveProtocolo(null));
        assertThrows(IllegalArgumentException.class, () -> repositorio.saveHorario(null));
        assertThrows(IllegalArgumentException.class, () -> repositorio.savePolitica(null));
        assertThrows(IllegalArgumentException.class, () -> repositorio.saveParametroPrioridad(null));
    }

    @Test
    void guardarConIdExistenteActualizaYRespetaLaSecuencia() {
        TipoPrueba tipo = new TipoPrueba();
        tipo.setId(100L);
        assertSame(tipo, repositorio.saveTipoPrueba(tipo));
        assertEquals(101L, repositorio.saveTipoPrueba(new TipoPrueba()).getId());

        Protocolo protocolo = new Protocolo();
        protocolo.setId(200L);
        assertSame(protocolo, repositorio.saveProtocolo(protocolo));
        assertEquals(201L, repositorio.saveProtocolo(new Protocolo()).getId());

        HorarioAtencion horario = new HorarioAtencion();
        horario.setId(300L);
        assertSame(horario, repositorio.saveHorario(horario));
        assertEquals(301L, repositorio.saveHorario(new HorarioAtencion()).getId());

        PoliticaCitacion politica = new PoliticaCitacion();
        politica.setId(400L);
        assertSame(politica, repositorio.savePolitica(politica));
        assertEquals(401L, repositorio.savePolitica(new PoliticaCitacion()).getId());

        ParametroPrioridad prioridad = new ParametroPrioridad();
        prioridad.setId(500L);
        assertSame(prioridad, repositorio.saveParametroPrioridad(prioridad));
        assertEquals(501L, repositorio.saveParametroPrioridad(new ParametroPrioridad()).getId());
    }

    @Test
    void listarDevuelveLasEntidadesGuardadas() {
        repositorio.saveTipoPrueba(new TipoPrueba());
        repositorio.saveProtocolo(new Protocolo());
        repositorio.saveHorario(new HorarioAtencion());
        repositorio.savePolitica(new PoliticaCitacion());
        repositorio.saveParametroPrioridad(new ParametroPrioridad());

        List<TipoPrueba> tipos = repositorio.findAllTiposPrueba();
        assertEquals(1, tipos.size());
        assertEquals(tipos.get(0).getId(), repositorio.findTipoPruebaById(tipos.get(0).getId()).orElseThrow().getId());
        assertEquals(1, repositorio.findAllProtocolos().size());
        assertEquals(1, repositorio.findAllHorarios().size());
        assertEquals(1, repositorio.findAllPoliticas().size());
        assertEquals(1, repositorio.findAllParametrosPrioridad().size());
    }
}
