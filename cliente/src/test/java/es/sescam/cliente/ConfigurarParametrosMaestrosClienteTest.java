package es.sescam.cliente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
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
import es.sescam.cliente.service.error.ApiError;
import es.sescam.cliente.service.error.ApiException;
import es.sescam.cliente.view.ParametrosMaestrosView;

/**
 * Pruebas de aceptación del cliente para RF-17 → CU-17 → módulo cliente v0.1.0 (issue #36).
 */
class ConfigurarParametrosMaestrosClienteTest {

    private final Solicitante administrador = new Solicitante("12345678Z", RolSolicitante.ADMINISTRADOR);

    @Test
    void crearTipoPruebaDelegaEnApiYConfirmaEnVista() {
        ApiFalso api = new ApiFalso();
        VistaEspia vista = new VistaEspia();
        ParametrosMaestrosController controller = new ParametrosMaestrosControllerImpl(api, vista);
        TipoPruebaDTO tipo = tipoValido();

        controller.crearTipoPrueba(administrador, tipo);

        assertSame(tipo, api.ultimoTipoCreado);
        assertEquals("Tipo de prueba guardado correctamente", vista.ultimoMensaje);
    }

    @Test
    void editarTipoPruebaPasaElIdAlApi() {
        ApiFalso api = new ApiFalso();
        VistaEspia vista = new VistaEspia();
        ParametrosMaestrosController controller = new ParametrosMaestrosControllerImpl(api, vista);
        TipoPruebaDTO tipo = tipoValido();

        controller.editarTipoPrueba(administrador, 7L, tipo);

        assertEquals(7L, api.ultimoIdEditado);
        assertSame(tipo, api.ultimoTipoEditado);
        assertEquals("Tipo de prueba actualizado correctamente", vista.ultimoMensaje);
    }

    @Test
    void listarTiposPruebaMuestraLaListaDevueltaPorElApi() {
        TipoPruebaDTO tipo = tipoValido();
        ApiFalso api = new ApiFalso();
        api.tipos = List.of(tipo);
        VistaEspia vista = new VistaEspia();
        ParametrosMaestrosController controller = new ParametrosMaestrosControllerImpl(api, vista);

        controller.listarTiposPrueba(administrador);

        assertEquals(1, vista.ultimosTipos.size());
        assertSame(tipo, vista.ultimosTipos.get(0));
    }

    @Test
    void listarNivelesPrioridadMuestraLosValoresDelCatalogo() {
        ApiFalso api = new ApiFalso();
        api.niveles = List.of("BAJA", "MEDIA", "ALTA", "URGENTE");
        VistaEspia vista = new VistaEspia();
        ParametrosMaestrosController controller = new ParametrosMaestrosControllerImpl(api, vista);

        controller.listarNivelesPrioridad(administrador);

        assertEquals(4, vista.ultimosNiveles.size());
        assertTrue(vista.ultimosNiveles.contains("URGENTE"));
    }

    @Test
    void errorDelApiSeMuestraComoMensajeEnLaVista() {
        ApiFalso api = new ApiFalso();
        api.error = new ApiException(new ApiError("FORBIDDEN",
                "Solo el administrador puede configurar parámetros maestros", 403));
        VistaEspia vista = new VistaEspia();
        ParametrosMaestrosController controller = new ParametrosMaestrosControllerImpl(api, vista);

        controller.crearHorario(administrador, new HorarioAtencionDTO());

        assertEquals("Solo el administrador puede configurar parámetros maestros",
                vista.ultimoMensaje);
    }

    @Test
    void apiRestRechazaSolicitanteQueNoEsAdministrador() {
        ParametrosMaestrosApiClient api = new ParametrosMaestrosApiClientRest("http://localhost");
        Solicitante doctor = new Solicitante("87654321A", RolSolicitante.DOCTOR);

        ApiException excepcion = assertThrows(ApiException.class,
                () -> api.listarTiposPrueba(doctor));

        assertEquals(403, excepcion.getError().getStatus());
        assertEquals("FORBIDDEN", excepcion.getError().getCode());
    }

    @Test
    void apiRestValidaDatosDeEntrada() {
        ParametrosMaestrosApiClient api = new ParametrosMaestrosApiClientRest("http://localhost");

        ApiException sinDto = assertThrows(ApiException.class,
                () -> api.crearProtocolo(administrador, null));
        assertEquals(400, sinDto.getError().getStatus());

        ApiException sinId = assertThrows(ApiException.class,
                () -> api.actualizarPolitica(administrador, null, new PoliticaCitacionDTO()));
        assertEquals(400, sinId.getError().getStatus());
    }

    @Test
    void apiRestAdministradorPuedeConsultarCatalogo() {
        ParametrosMaestrosApiClient api = new ParametrosMaestrosApiClientRest("http://localhost");

        assertEquals("http://localhost", api.getBaseUrl());
        assertEquals(4, api.listarNivelesPrioridad(administrador).size());
        assertTrue(api.listarTiposPrueba(administrador).isEmpty());
    }

    private TipoPruebaDTO tipoValido() {
        return new TipoPruebaDTO(null, "Rx Tórax", "RX", "Sala Rx", 15,
                "No requiere", false, false, "No aplica", "MEDIA", true);
    }

    static class ApiFalso implements ParametrosMaestrosApiClient {

        List<TipoPruebaDTO> tipos = Collections.emptyList();
        List<String> niveles = Collections.emptyList();
        TipoPruebaDTO ultimoTipoCreado;
        TipoPruebaDTO ultimoTipoEditado;
        Long ultimoIdEditado;
        ApiException error;

        @Override
        public String getBaseUrl() { return "http://local"; }

        @Override
        public List<TipoPruebaDTO> listarTiposPrueba(Solicitante solicitante) {
            lanzarSiHayError();
            return tipos;
        }

        @Override
        public TipoPruebaDTO crearTipoPrueba(Solicitante solicitante, TipoPruebaDTO tipoPrueba) {
            lanzarSiHayError();
            this.ultimoTipoCreado = tipoPrueba;
            return tipoPrueba;
        }

        @Override
        public TipoPruebaDTO actualizarTipoPrueba(Solicitante solicitante, Long id, TipoPruebaDTO datos) {
            lanzarSiHayError();
            this.ultimoIdEditado = id;
            this.ultimoTipoEditado = datos;
            return datos;
        }

        @Override
        public List<ProtocoloDTO> listarProtocolos(Solicitante solicitante) { return Collections.emptyList(); }

        @Override
        public ProtocoloDTO crearProtocolo(Solicitante solicitante, ProtocoloDTO protocolo) { return protocolo; }

        @Override
        public ProtocoloDTO actualizarProtocolo(Solicitante solicitante, Long id, ProtocoloDTO datos) { return datos; }

        @Override
        public List<HorarioAtencionDTO> listarHorarios(Solicitante solicitante) { return Collections.emptyList(); }

        @Override
        public HorarioAtencionDTO crearHorario(Solicitante solicitante, HorarioAtencionDTO horario) {
            lanzarSiHayError();
            return horario;
        }

        @Override
        public HorarioAtencionDTO actualizarHorario(Solicitante solicitante, Long id, HorarioAtencionDTO datos) { return datos; }

        @Override
        public List<PoliticaCitacionDTO> listarPoliticas(Solicitante solicitante) { return Collections.emptyList(); }

        @Override
        public PoliticaCitacionDTO crearPolitica(Solicitante solicitante, PoliticaCitacionDTO politica) { return politica; }

        @Override
        public PoliticaCitacionDTO actualizarPolitica(Solicitante solicitante, Long id, PoliticaCitacionDTO datos) { return datos; }

        @Override
        public List<ParametroPrioridadDTO> listarParametrosPrioridad(Solicitante solicitante) { return Collections.emptyList(); }

        @Override
        public ParametroPrioridadDTO configurarParametroPrioridad(Solicitante solicitante,
                                                                  ParametroPrioridadDTO parametro) { return parametro; }

        @Override
        public List<String> listarNivelesPrioridad(Solicitante solicitante) {
            lanzarSiHayError();
            return niveles;
        }

        private void lanzarSiHayError() {
            if (error != null) {
                throw error;
            }
        }
    }

    static class VistaEspia implements ParametrosMaestrosView {

        List<TipoPruebaDTO> ultimosTipos = new ArrayList<>();
        List<String> ultimosNiveles = new ArrayList<>();
        String ultimoMensaje;

        @Override
        public void mostrarTiposPrueba(List<TipoPruebaDTO> tipos) { this.ultimosTipos = new ArrayList<>(tipos); }

        @Override
        public void mostrarProtocolos(List<ProtocoloDTO> protocolos) { }

        @Override
        public void mostrarHorarios(List<HorarioAtencionDTO> horarios) { }

        @Override
        public void mostrarPoliticas(List<PoliticaCitacionDTO> politicas) { }

        @Override
        public void mostrarParametrosPrioridad(List<ParametroPrioridadDTO> parametros) { }

        @Override
        public void mostrarNivelesPrioridad(List<String> niveles) { this.ultimosNiveles = new ArrayList<>(niveles); }

        @Override
        public void mostrarMensaje(String mensaje) { this.ultimoMensaje = mensaje; }
    }
}
