package es.sescam.servidor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumSet;

import org.junit.jupiter.api.Test;

import es.sescam.servidor.auditoria.AuditoriaStub;
import es.sescam.servidor.controller.ParametrosMaestrosController;
import es.sescam.servidor.controller.ParametrosMaestrosControllerImpl;
import es.sescam.servidor.model.HorarioAtencion;
import es.sescam.servidor.model.Modalidad;
import es.sescam.servidor.model.ParametroPrioridad;
import es.sescam.servidor.model.PoliticaCitacion;
import es.sescam.servidor.model.Prioridad;
import es.sescam.servidor.model.Protocolo;
import es.sescam.servidor.model.TipoPrueba;
import es.sescam.servidor.repository.ParametrosMaestrosRepositoryMemoria;
import es.sescam.servidor.seguridad.Rol;
import es.sescam.servidor.seguridad.Usuario;
import es.sescam.servidor.service.ParametrosMaestrosService;
import es.sescam.servidor.service.ParametrosMaestrosServiceImpl;

class ParametrosMaestrosControllerTest {

    @Test
    void delegaCreacionYListadoEnElServicio() {
        ParametrosMaestrosController controller = crearController();
        Usuario administrador = administrador();
        TipoPrueba tipo = new TipoPrueba(null, "Ecografía abdominal", Modalidad.ECOGRAFIA,
                "Sala de ecografía", 20, "Ayuno de 6 horas", false, false, "No aplica",
                Prioridad.MEDIA, false);

        assertNotNull(controller.crearTipoPrueba(administrador, tipo).getId());
        assertEquals(1, controller.listarTiposPrueba(administrador).size());
    }

    @Test
    void delegaTodasLasOperacionesEnElServicio() {
        ParametrosMaestrosController controller = crearController();
        Usuario administrador = administrador();

        TipoPrueba tipo = controller.crearTipoPrueba(administrador, tipoValido());
        Protocolo protocolo = controller.crearProtocolo(administrador, protocoloValido(tipo.getId()));
        HorarioAtencion horario = controller.crearHorario(administrador, horarioValido());
        PoliticaCitacion politica = controller.crearPolitica(administrador, politicaValida());
        ParametroPrioridad prioridad = controller.configurarParametroPrioridad(administrador,
                prioridadValida());

        assertEquals(1, controller.listarTiposPrueba(administrador).size());
        assertTrue(controller.obtenerTipoPrueba(administrador, tipo.getId()).isPresent());
        assertEquals(1, controller.listarProtocolos(administrador).size());
        assertTrue(controller.obtenerProtocolo(administrador, protocolo.getId()).isPresent());
        assertEquals(1, controller.listarHorarios(administrador).size());
        assertTrue(controller.obtenerHorario(administrador, horario.getId()).isPresent());
        assertEquals(1, controller.listarPoliticas(administrador).size());
        assertTrue(controller.obtenerPolitica(administrador, politica.getId()).isPresent());
        assertEquals(1, controller.listarParametrosPrioridad(administrador).size());
        assertEquals(Prioridad.values().length, controller.listarNivelesPrioridad(administrador).size());

        TipoPrueba datosTipo = tipoValido();
        datosTipo.setNombre("Ecografía actualizada");
        assertEquals("Ecografía actualizada",
                controller.actualizarTipoPrueba(administrador, tipo.getId(), datosTipo).getNombre());

        Protocolo datosProtocolo = protocoloValido(tipo.getId());
        datosProtocolo.setNombre("Protocolo actualizado");
        assertEquals("Protocolo actualizado",
                controller.actualizarProtocolo(administrador, protocolo.getId(), datosProtocolo).getNombre());

        HorarioAtencion datosHorario = horarioValido();
        datosHorario.setDuracionSlotMinutos(20);
        assertEquals(20, controller.actualizarHorario(administrador, horario.getId(), datosHorario)
                .getDuracionSlotMinutos());

        PoliticaCitacion datosPolitica = politicaValida();
        datosPolitica.setMaximoCitasPorFranja(6);
        assertEquals(6, controller.actualizarPolitica(administrador, politica.getId(), datosPolitica)
                .getMaximoCitasPorFranja());
        assertNotNull(prioridad.getId());
    }

    private ParametrosMaestrosController crearController() {
        ParametrosMaestrosService servicio = new ParametrosMaestrosServiceImpl(
                new ParametrosMaestrosRepositoryMemoria(), new AuditoriaStub());
        return new ParametrosMaestrosControllerImpl(servicio);
    }

    private Usuario administrador() {
        return new Usuario("u-admin-2", "admin2", "Admin Sistema",
                EnumSet.of(Rol.ADMINISTRADOR_SISTEMA));
    }

    private TipoPrueba tipoValido() {
        return new TipoPrueba(null, "Ecografía", Modalidad.ECOGRAFIA, "Sala", 20,
                "Ayuno", false, false, "No aplica", Prioridad.MEDIA, true);
    }

    private Protocolo protocoloValido(Long tipoId) {
        return new Protocolo(null, tipoId, "Protocolo", "Desc", "Pasos", true);
    }

    private HorarioAtencion horarioValido() {
        return new HorarioAtencion(null, DayOfWeek.MONDAY, LocalTime.of(8, 0),
                LocalTime.of(10, 0), 30, true);
    }

    private PoliticaCitacion politicaValida() {
        return new PoliticaCitacion(null, "Política", "Desc", 1, 30, 4, true);
    }

    private ParametroPrioridad prioridadValida() {
        return new ParametroPrioridad(null, Prioridad.ALTA, "Alta", 3, true);
    }
}
