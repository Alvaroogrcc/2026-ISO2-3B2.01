package es.sescam.cliente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import es.sescam.cliente.controller.CitaController;
import es.sescam.cliente.controller.CitaControllerImpl;
import es.sescam.cliente.model.CitaDTO;
import es.sescam.cliente.service.CitaApiClient;
import es.sescam.cliente.service.CitaApiClientRest;
import es.sescam.cliente.service.auth.RolSolicitante;
import es.sescam.cliente.service.auth.Solicitante;
import es.sescam.cliente.service.error.ApiError;
import es.sescam.cliente.service.error.ApiException;
import es.sescam.cliente.view.CitaView;

/**
 * Pruebas del CU-13 en la capa cliente, usando dobles de la API y de la vista.
 */
class GestionCitasClienteTest {

    private static final LocalDate FECHA = LocalDate.of(2026, 11, 23);
    private static final LocalTime HORA = LocalTime.of(10, 0);

    private final Solicitante administrativo = new Solicitante("12345678A", RolSolicitante.ADMINISTRADOR);

    @Test
    void asignarCitaPideAlApiYMuestraElResultado() {
        ApiEspia api = new ApiEspia();
        CitaViewEspia vista = new CitaViewEspia();
        CitaController controller = new CitaControllerImpl(api, vista);

        controller.asignarCita(administrativo, 5L, FECHA, HORA);

        assertEquals(5L, api.ultimoCitaId);
        assertEquals(FECHA, api.ultimaFecha);
        assertEquals(HORA, api.ultimaHora);
        assertSame(api.ultimaCitaDevuelta, vista.ultimaCita);
        assertEquals("Cita asignada", vista.ultimoMensaje);
    }

    @Test
    void reprogramarYCancelarDeleganEnElApi() {
        ApiEspia api = new ApiEspia();
        CitaViewEspia vista = new CitaViewEspia();
        CitaController controller = new CitaControllerImpl(api, vista);

        controller.reprogramarCita(administrativo, 6L, FECHA.plusDays(1), HORA.plusHours(1));
        assertEquals(6L, api.ultimoCitaId);
        assertEquals(FECHA.plusDays(1), api.ultimaFecha);
        assertEquals(HORA.plusHours(1), api.ultimaHora);
        assertEquals("Cita reprogramada", vista.ultimoMensaje);

        controller.cancelarCita(administrativo, 7L);
        assertEquals(7L, api.ultimoCitaId);
        assertEquals("Cita cancelada", vista.ultimoMensaje);
    }

    @Test
    void errorDelApiSeMuestraSinCita() {
        ApiEspia api = new ApiEspia();
        api.error = new ApiException(new ApiError("FORBIDDEN", "Se requiere rol administrativo", 403));
        CitaViewEspia vista = new CitaViewEspia();
        CitaController controller = new CitaControllerImpl(api, vista);

        controller.cancelarCita(administrativo, 8L);

        assertEquals("Se requiere rol administrativo", vista.ultimoMensaje);
        assertNull(vista.ultimaCita);
    }

    @Test
    void apiRestValidaRolYDatosAntesDeLaLlamadaRemota() {
        CitaApiClient api = new CitaApiClientRest("http://localhost");
        Solicitante doctor = new Solicitante("12345678B", RolSolicitante.DOCTOR);

        ApiException sinRol = assertThrows(ApiException.class,
                () -> api.asignarCita(doctor, 9L, FECHA, HORA));
        assertEquals(403, sinRol.getError().getStatus());

        ApiException sinFecha = assertThrows(ApiException.class,
                () -> api.asignarCita(administrativo, 9L, null, HORA));
        assertEquals(400, sinFecha.getError().getStatus());

        ApiException pendiente = assertThrows(ApiException.class,
                () -> api.cancelarCita(administrativo, 9L));
        assertEquals(501, pendiente.getError().getStatus());
        assertTrue(pendiente.getError().getCode().contains("PENDIENTE"));
    }

    @Test
    void apiRestConfirmaQueAsignarYReprogramarQuedanPendientesDeHttp() {
        CitaApiClient api = new CitaApiClientRest("http://localhost");

        assertEquals(501, assertThrows(ApiException.class,
                () -> api.asignarCita(administrativo, 9L, FECHA, HORA)).getError().getStatus());
        assertEquals(501, assertThrows(ApiException.class,
                () -> api.reprogramarCita(administrativo, 9L, FECHA, HORA)).getError().getStatus());
    }

    @Test
    void apiRestValidaRolYDatosDeCancelacion() {
        CitaApiClient api = new CitaApiClientRest("http://localhost");
        Solicitante doctor = new Solicitante("12345678B", RolSolicitante.DOCTOR);

        assertEquals(403, assertThrows(ApiException.class,
                () -> api.cancelarCita(doctor, 9L)).getError().getStatus());
        assertEquals(400, assertThrows(ApiException.class,
                () -> api.cancelarCita(administrativo, null)).getError().getStatus());
        assertEquals(400, assertThrows(ApiException.class,
                () -> api.asignarCita(administrativo, null, FECHA, HORA)).getError().getStatus());
        assertEquals(400, assertThrows(ApiException.class,
                () -> api.reprogramarCita(administrativo, 9L, FECHA, null)).getError().getStatus());
    }

    @Test
    void apiRestValidaIdentidadYConsultas() {
        CitaApiClient api = new CitaApiClientRest("http://localhost");
        Solicitante paciente = new Solicitante("12345678C", RolSolicitante.PACIENTE);

        assertEquals(401, assertThrows(ApiException.class,
                () -> api.obtenerCitasPaciente(null, 1L)).getError().getStatus());
        assertEquals(400, assertThrows(ApiException.class,
                () -> api.obtenerCitasPaciente(administrativo, null)).getError().getStatus());
        assertEquals(403, assertThrows(ApiException.class,
                () -> api.obtenerCitasPaciente(paciente, 1L)).getError().getStatus());
        assertEquals(400, assertThrows(ApiException.class,
                () -> api.obtenerCita(administrativo, null)).getError().getStatus());
        assertEquals(403, assertThrows(ApiException.class,
                () -> api.obtenerCita(paciente, 1L)).getError().getStatus());
        assertEquals(404, assertThrows(ApiException.class,
                () -> api.obtenerCita(administrativo, 1L)).getError().getStatus());
        assertTrue(api.obtenerCitasPaciente(administrativo, 1L).isEmpty());
    }

    @Test
    void erroresDeAsignarYReprogramarSeMuestranSinCita() {
        ApiEspia api = new ApiEspia();
        api.error = new ApiException(new ApiError("CITA_INVALID_REQUEST", "Datos no válidos", 400));
        CitaViewEspia vista = new CitaViewEspia();
        CitaController controller = new CitaControllerImpl(api, vista);

        controller.asignarCita(administrativo, 5L, FECHA, HORA);
        assertEquals("Datos no válidos", vista.ultimoMensaje);
        assertNull(vista.ultimaCita);

        controller.reprogramarCita(administrativo, 6L, FECHA, HORA);
        assertEquals("Datos no válidos", vista.ultimoMensaje);
        assertNull(vista.ultimaCita);
    }

    static class ApiEspia implements CitaApiClient {
        Long ultimoCitaId;
        LocalDate ultimaFecha;
        LocalTime ultimaHora;
        CitaDTO ultimaCitaDevuelta = new CitaDTO(1L, FECHA, HORA, "CONFIRMADA", 10L, 20L);
        ApiException error;

        @Override
        public String getBaseUrl() { return "http://local"; }

        @Override
        public List<CitaDTO> obtenerCitasPaciente(Solicitante solicitante, Long pacienteId) {
            if (error != null) throw error;
            return List.of(ultimaCitaDevuelta);
        }

        @Override
        public CitaDTO obtenerCita(Solicitante solicitante, Long citaId) {
            if (error != null) throw error;
            return ultimaCitaDevuelta;
        }

        @Override
        public CitaDTO asignarCita(Solicitante solicitante, Long citaId, LocalDate fecha, LocalTime hora) {
            if (error != null) throw error;
            ultimoCitaId = citaId;
            ultimaFecha = fecha;
            ultimaHora = hora;
            return ultimaCitaDevuelta;
        }

        @Override
        public CitaDTO reprogramarCita(Solicitante solicitante, Long citaId, LocalDate fecha, LocalTime hora) {
            if (error != null) throw error;
            ultimoCitaId = citaId;
            ultimaFecha = fecha;
            ultimaHora = hora;
            return ultimaCitaDevuelta;
        }

        @Override
        public CitaDTO cancelarCita(Solicitante solicitante, Long citaId) {
            if (error != null) throw error;
            ultimoCitaId = citaId;
            return ultimaCitaDevuelta;
        }
    }

    static class CitaViewEspia implements CitaView {
        CitaDTO ultimaCita;
        List<CitaDTO> ultimasCitas = new ArrayList<>();
        String ultimoMensaje;

        @Override
        public void mostrarCita(CitaDTO cita) { this.ultimaCita = cita; }

        @Override
        public void mostrarCitas(List<CitaDTO> citas) { this.ultimasCitas = new ArrayList<>(citas); }

        @Override
        public void mostrarMensaje(String mensaje) { this.ultimoMensaje = mensaje; }
    }
}
