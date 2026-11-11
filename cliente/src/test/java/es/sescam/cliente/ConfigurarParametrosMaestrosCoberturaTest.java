package es.sescam.cliente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import es.sescam.cliente.controller.ParametrosMaestrosController;
import es.sescam.cliente.controller.ParametrosMaestrosControllerImpl;
import es.sescam.cliente.model.HorarioAtencionDTO;
import es.sescam.cliente.model.ParametroPrioridadDTO;
import es.sescam.cliente.model.PoliticaCitacionDTO;
import es.sescam.cliente.model.ProtocoloDTO;
import es.sescam.cliente.model.TipoPruebaDTO;
import es.sescam.cliente.service.ParametrosMaestrosApiClient;
import es.sescam.cliente.service.ParametrosMaestrosApiClientRest;
import es.sescam.cliente.service.auth.RolSolicitante;
import es.sescam.cliente.service.auth.Solicitante;
import es.sescam.cliente.service.error.ApiException;
import es.sescam.cliente.view.ParametrosMaestrosView;
import es.sescam.cliente.view.ParametrosMaestrosViewImpl;

/**
 * Tests que completan la cobertura de las clases de CU-17 en el cliente:
 * todas las operaciones del API, del controlador, de la vista y de los DTO.
 */
class ConfigurarParametrosMaestrosCoberturaTest {

    private final Solicitante administrador = new Solicitante("12345678Z", RolSolicitante.ADMINISTRADOR);
    private final ParametrosMaestrosApiClient api = new ParametrosMaestrosApiClientRest("http://localhost");

    @Test
    void apiRestExponeTodasLasOperacionesParaElAdministrador() {
        TipoPruebaDTO tipo = new TipoPruebaDTO();
        ProtocoloDTO protocolo = new ProtocoloDTO();
        HorarioAtencionDTO horario = new HorarioAtencionDTO();
        PoliticaCitacionDTO politica = new PoliticaCitacionDTO();
        ParametroPrioridadDTO prioridad = new ParametroPrioridadDTO();

        assertEquals("http://localhost", api.getBaseUrl());
        assertSame(tipo, api.crearTipoPrueba(administrador, tipo));
        assertSame(tipo, api.actualizarTipoPrueba(administrador, 1L, tipo));
        assertSame(protocolo, api.crearProtocolo(administrador, protocolo));
        assertSame(protocolo, api.actualizarProtocolo(administrador, 2L, protocolo));
        assertSame(horario, api.crearHorario(administrador, horario));
        assertSame(horario, api.actualizarHorario(administrador, 3L, horario));
        assertSame(politica, api.crearPolitica(administrador, politica));
        assertSame(politica, api.actualizarPolitica(administrador, 4L, politica));
        assertSame(prioridad, api.configurarParametroPrioridad(administrador, prioridad));

        assertTrue(api.listarTiposPrueba(administrador).isEmpty());
        assertTrue(api.listarProtocolos(administrador).isEmpty());
        assertTrue(api.listarHorarios(administrador).isEmpty());
        assertTrue(api.listarPoliticas(administrador).isEmpty());
        assertTrue(api.listarParametrosPrioridad(administrador).isEmpty());
        assertEquals(List.of("BAJA", "MEDIA", "ALTA", "URGENTE"),
                api.listarNivelesPrioridad(administrador));
    }

    @Test
    void apiRestRechazaIdentidadesYDatosInvalidosEnTodasLasOperaciones() {
        Solicitante sinDni = new Solicitante("12345678Z", RolSolicitante.ADMINISTRADOR) {
            @Override
            public String getDni() {
                return null;
            }
        };
        Solicitante dniBlanco = new Solicitante("12345678Z", RolSolicitante.ADMINISTRADOR) {
            @Override
            public String getDni() {
                return "  ";
            }
        };
        Solicitante doctor = new Solicitante("87654321A", RolSolicitante.DOCTOR);

        assertEquals(401, assertThrows(ApiException.class,
                () -> api.listarTiposPrueba(null)).getError().getStatus());
        assertEquals(401, assertThrows(ApiException.class,
                () -> api.listarTiposPrueba(sinDni)).getError().getStatus());
        assertEquals(401, assertThrows(ApiException.class,
                () -> api.listarTiposPrueba(dniBlanco)).getError().getStatus());
        assertEquals(403, assertThrows(ApiException.class,
                () -> api.listarTiposPrueba(doctor)).getError().getStatus());

        assertEquals(400, assertThrows(ApiException.class,
                () -> api.crearTipoPrueba(administrador, null)).getError().getStatus());
        assertEquals(400, assertThrows(ApiException.class,
                () -> api.crearProtocolo(administrador, null)).getError().getStatus());
        assertEquals(400, assertThrows(ApiException.class,
                () -> api.crearHorario(administrador, null)).getError().getStatus());
        assertEquals(400, assertThrows(ApiException.class,
                () -> api.crearPolitica(administrador, null)).getError().getStatus());
        assertEquals(400, assertThrows(ApiException.class,
                () -> api.configurarParametroPrioridad(administrador, null)).getError().getStatus());

        assertEquals(400, assertThrows(ApiException.class,
                () -> api.actualizarTipoPrueba(administrador, null, new TipoPruebaDTO()))
                .getError().getStatus());
        assertEquals(400, assertThrows(ApiException.class,
                () -> api.actualizarProtocolo(administrador, null, new ProtocoloDTO()))
                .getError().getStatus());
        assertEquals(400, assertThrows(ApiException.class,
                () -> api.actualizarHorario(administrador, null, new HorarioAtencionDTO()))
                .getError().getStatus());
        assertEquals(400, assertThrows(ApiException.class,
                () -> api.actualizarPolitica(administrador, null, new PoliticaCitacionDTO()))
                .getError().getStatus());
    }

    @Test
    void controladorDelegaTodasLasOperacionesYActualizaLaVista() {
        VistaRegistradora vista = new VistaRegistradora();
        ParametrosMaestrosController controller = new ParametrosMaestrosControllerImpl(api, vista);

        controller.crearTipoPrueba(administrador, new TipoPruebaDTO());
        assertEquals("Tipo de prueba guardado correctamente", vista.ultimoMensaje);
        controller.editarTipoPrueba(administrador, 1L, new TipoPruebaDTO());
        assertEquals("Tipo de prueba actualizado correctamente", vista.ultimoMensaje);
        controller.crearProtocolo(administrador, new ProtocoloDTO());
        assertEquals("Protocolo guardado correctamente", vista.ultimoMensaje);
        controller.editarProtocolo(administrador, 2L, new ProtocoloDTO());
        assertEquals("Protocolo actualizado correctamente", vista.ultimoMensaje);
        controller.crearHorario(administrador, new HorarioAtencionDTO());
        assertEquals("Horario guardado correctamente", vista.ultimoMensaje);
        controller.editarHorario(administrador, 3L, new HorarioAtencionDTO());
        assertEquals("Horario actualizado correctamente", vista.ultimoMensaje);
        controller.crearPolitica(administrador, new PoliticaCitacionDTO());
        assertEquals("Política de citación guardada correctamente", vista.ultimoMensaje);
        controller.editarPolitica(administrador, 4L, new PoliticaCitacionDTO());
        assertEquals("Política de citación actualizada correctamente", vista.ultimoMensaje);
        controller.configurarParametroPrioridad(administrador, new ParametroPrioridadDTO());
        assertEquals("Nivel de prioridad configurado correctamente", vista.ultimoMensaje);

        controller.listarTiposPrueba(administrador);
        controller.listarProtocolos(administrador);
        controller.listarHorarios(administrador);
        controller.listarPoliticas(administrador);
        controller.listarParametrosPrioridad(administrador);
        controller.listarNivelesPrioridad(administrador);
        assertEquals(1, vista.tipos);
        assertEquals(1, vista.protocolos);
        assertEquals(1, vista.horarios);
        assertEquals(1, vista.politicas);
        assertEquals(1, vista.prioridades);
        assertEquals(1, vista.niveles);

        controller.mostrarTiposPrueba(Collections.emptyList());
        controller.mostrarProtocolos(Collections.emptyList());
        controller.mostrarHorarios(Collections.emptyList());
        controller.mostrarPoliticas(Collections.emptyList());
        controller.mostrarParametrosPrioridad(Collections.emptyList());
        controller.mostrarNivelesPrioridad(Collections.emptyList());
        controller.mostrarMensaje("Mensaje directo");
        assertEquals(2, vista.tipos);
        assertEquals(2, vista.protocolos);
        assertEquals(2, vista.horarios);
        assertEquals(2, vista.politicas);
        assertEquals(2, vista.prioridades);
        assertEquals(2, vista.niveles);
        assertEquals("Mensaje directo", vista.ultimoMensaje);
    }

    @Test
    void vistaImplExponeTodosLosMetodos() {
        ParametrosMaestrosView vista = new ParametrosMaestrosViewImpl();
        vista.mostrarTiposPrueba(Collections.emptyList());
        vista.mostrarProtocolos(Collections.emptyList());
        vista.mostrarHorarios(Collections.emptyList());
        vista.mostrarPoliticas(Collections.emptyList());
        vista.mostrarParametrosPrioridad(Collections.emptyList());
        vista.mostrarNivelesPrioridad(Collections.emptyList());
        vista.mostrarMensaje("Mensaje");
    }

    @Test
    void dtosPermitenLeerEscribirTodosSusAtributos() {
        TipoPruebaDTO tipo = new TipoPruebaDTO();
        tipo.setId(1L);
        tipo.setNombre("Rx");
        tipo.setModalidad("RX");
        tipo.setEquipoSalaRequeridos("Sala");
        tipo.setDuracionEstimadaMinutos(15);
        tipo.setPreparacionPrevia("Ninguna");
        tipo.setUsoContraste(false);
        tipo.setRequiereConsentimientoInformado(false);
        tipo.setRequisitosSeguridad("No aplica");
        tipo.setPrioridad("MEDIA");
        tipo.setPermiteSeguimientoPeriodico(true);
        assertEquals(1L, tipo.getId());
        assertEquals("Rx", tipo.getNombre());
        assertEquals("RX", tipo.getModalidad());
        assertEquals("Sala", tipo.getEquipoSalaRequeridos());
        assertEquals(15, tipo.getDuracionEstimadaMinutos());
        assertEquals("Ninguna", tipo.getPreparacionPrevia());
        assertEquals(false, tipo.getUsoContraste());
        assertEquals(false, tipo.getRequiereConsentimientoInformado());
        assertEquals("No aplica", tipo.getRequisitosSeguridad());
        assertEquals("MEDIA", tipo.getPrioridad());
        assertTrue(tipo.getPermiteSeguimientoPeriodico());
        assertEquals(2L, new TipoPruebaDTO(2L, "TAC", "TAC", "Sala TAC", 30, "Ayuno", true,
                true, "Dosimetría", "ALTA", false).getId());

        ProtocoloDTO protocolo = new ProtocoloDTO();
        protocolo.setId(3L);
        protocolo.setTipoPruebaId(1L);
        protocolo.setNombre("Protocolo");
        protocolo.setDescripcion("Desc");
        protocolo.setPasos("Pasos");
        protocolo.setActivo(true);
        assertEquals(3L, protocolo.getId());
        assertEquals(1L, protocolo.getTipoPruebaId());
        assertEquals("Protocolo", protocolo.getNombre());
        assertEquals("Desc", protocolo.getDescripcion());
        assertEquals("Pasos", protocolo.getPasos());
        assertTrue(protocolo.getActivo());
        assertEquals(4L, new ProtocoloDTO(4L, 1L, "P", "D", "S", true).getId());

        HorarioAtencionDTO horario = new HorarioAtencionDTO();
        horario.setId(5L);
        horario.setDiaSemana("MONDAY");
        horario.setHoraInicio(LocalTime.of(8, 0));
        horario.setHoraFin(LocalTime.of(10, 0));
        horario.setDuracionSlotMinutos(30);
        horario.setActivo(true);
        assertEquals(5L, horario.getId());
        assertEquals("MONDAY", horario.getDiaSemana());
        assertEquals(LocalTime.of(8, 0), horario.getHoraInicio());
        assertEquals(LocalTime.of(10, 0), horario.getHoraFin());
        assertEquals(30, horario.getDuracionSlotMinutos());
        assertTrue(horario.getActivo());
        assertEquals(6L, new HorarioAtencionDTO(6L, "TUESDAY", LocalTime.NOON,
                LocalTime.of(14, 0), 30, false).getId());

        PoliticaCitacionDTO politica = new PoliticaCitacionDTO();
        politica.setId(7L);
        politica.setNombre("Política");
        politica.setDescripcion("Desc");
        politica.setDiasAntelacionMinima(1);
        politica.setDiasAntelacionMaxima(30);
        politica.setMaximoCitasPorFranja(4);
        politica.setActiva(true);
        assertEquals(7L, politica.getId());
        assertEquals("Política", politica.getNombre());
        assertEquals("Desc", politica.getDescripcion());
        assertEquals(1, politica.getDiasAntelacionMinima());
        assertEquals(30, politica.getDiasAntelacionMaxima());
        assertEquals(4, politica.getMaximoCitasPorFranja());
        assertTrue(politica.getActiva());
        assertEquals(8L, new PoliticaCitacionDTO(8L, "P", "D", 1, 30, 4, true).getId());

        ParametroPrioridadDTO prioridad = new ParametroPrioridadDTO();
        prioridad.setId(9L);
        prioridad.setPrioridad("URGENTE");
        prioridad.setDescripcion("Urgente");
        prioridad.setDiasMaximosEspera(1);
        prioridad.setActivo(true);
        assertEquals(9L, prioridad.getId());
        assertEquals("URGENTE", prioridad.getPrioridad());
        assertEquals("Urgente", prioridad.getDescripcion());
        assertEquals(1, prioridad.getDiasMaximosEspera());
        assertTrue(prioridad.getActivo());
        assertEquals(10L, new ParametroPrioridadDTO(10L, "ALTA", "Alta", 3, true).getId());
    }

    static class VistaRegistradora implements ParametrosMaestrosView {

        int tipos;
        int protocolos;
        int horarios;
        int politicas;
        int prioridades;
        int niveles;
        String ultimoMensaje;

        @Override
        public void mostrarTiposPrueba(List<TipoPruebaDTO> tipos) {
            this.tipos++;
        }

        @Override
        public void mostrarProtocolos(List<ProtocoloDTO> protocolos) {
            this.protocolos++;
        }

        @Override
        public void mostrarHorarios(List<HorarioAtencionDTO> horarios) {
            this.horarios++;
        }

        @Override
        public void mostrarPoliticas(List<PoliticaCitacionDTO> politicas) {
            this.politicas++;
        }

        @Override
        public void mostrarParametrosPrioridad(List<ParametroPrioridadDTO> parametros) {
            this.prioridades++;
        }

        @Override
        public void mostrarNivelesPrioridad(List<String> niveles) {
            this.niveles++;
        }

        @Override
        public void mostrarMensaje(String mensaje) {
            this.ultimoMensaje = mensaje;
        }
    }
}
