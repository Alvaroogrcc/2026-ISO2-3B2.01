package es.sescam.servidor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.EnumSet;

import org.junit.jupiter.api.Test;

import es.sescam.servidor.auditoria.AuditoriaStub;
import es.sescam.servidor.controller.ParametrosMaestrosController;
import es.sescam.servidor.controller.ParametrosMaestrosControllerImpl;
import es.sescam.servidor.model.Modalidad;
import es.sescam.servidor.model.Prioridad;
import es.sescam.servidor.model.TipoPrueba;
import es.sescam.servidor.repository.ParametrosMaestrosRepositoryMemoria;
import es.sescam.servidor.seguridad.Rol;
import es.sescam.servidor.seguridad.Usuario;
import es.sescam.servidor.service.ParametrosMaestrosService;
import es.sescam.servidor.service.ParametrosMaestrosServiceImpl;

class ParametrosMaestrosControllerTest {

    @Test
    void delegaCreacionYListadoEnElServicio() {
        ParametrosMaestrosService servicio = new ParametrosMaestrosServiceImpl(
                new ParametrosMaestrosRepositoryMemoria(), new AuditoriaStub());
        ParametrosMaestrosController controller = new ParametrosMaestrosControllerImpl(servicio);
        Usuario administrador = new Usuario("u-admin-2", "admin2", "Admin Sistema",
                EnumSet.of(Rol.ADMINISTRADOR_SISTEMA));
        TipoPrueba tipo = new TipoPrueba(null, "Ecografía abdominal", Modalidad.ECOGRAFIA,
                "Sala de ecografía", 20, "Ayuno de 6 horas", false, false, "No aplica",
                Prioridad.MEDIA, false);

        assertNotNull(controller.crearTipoPrueba(administrador, tipo).getId());
        assertEquals(1, controller.listarTiposPrueba(administrador).size());
    }
}
