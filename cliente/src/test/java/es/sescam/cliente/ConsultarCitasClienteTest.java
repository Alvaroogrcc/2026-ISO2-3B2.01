package es.sescam.cliente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import es.sescam.cliente.controller.CitaController;
import es.sescam.cliente.controller.CitaControllerImpl;
import es.sescam.cliente.model.CitaDTO;
import es.sescam.cliente.service.CitaApiClient;
import es.sescam.cliente.service.auth.RolSolicitante;
import es.sescam.cliente.service.auth.Solicitante;
import es.sescam.cliente.service.error.ApiError;
import es.sescam.cliente.service.error.ApiException;
import es.sescam.cliente.view.CitaView;

class ConsultarCitasClienteTest {

    @Test
    void consultarMisCitasPideAlApiClientYMuestraEnVista() {
        CitaDTO c1 = new CitaDTO(1L, null, null, "PENDIENTE", 10L, 20L);
        Solicitante doctor = new Solicitante("12345678A", RolSolicitante.DOCTOR);
        CitaApiClient api = new CitaApiClient() {
            @Override
            public String getBaseUrl() { return "http://local"; }
            @Override
            public List<CitaDTO> obtenerCitasPaciente(Solicitante solicitante, Long pacienteId) {
                assertEquals("12345678A", solicitante.getDni());
                assertEquals(10L, pacienteId);
                return List.of(c1);
            }
            @Override
            public CitaDTO obtenerCita(Solicitante solicitante, Long citaId) { return null; }
            @Override
            public CitaDTO asignarCita(Solicitante solicitante, Long citaId, LocalDate fecha, LocalTime hora) { return null; }
            @Override
            public CitaDTO reprogramarCita(Solicitante solicitante, Long citaId, LocalDate fecha, LocalTime hora) { return null; }
            @Override
            public CitaDTO cancelarCita(Solicitante solicitante, Long citaId) { return null; }
        };
        CitaViewEspia vista = new CitaViewEspia();
        CitaController controller = new CitaControllerImpl(api, vista);

        controller.consultarMisCitas(doctor, 10L);

        assertNotNull(vista.ultimasCitas);
        assertEquals(1, vista.ultimasCitas.size());
        assertEquals(1L, vista.ultimasCitas.get(0).getId());
    }

    @Test
    void consultarDetalleCitaInexistenteMuestraMensaje404() {
        Solicitante doctor = new Solicitante("12345678A", RolSolicitante.DOCTOR);
        CitaApiClient api = new CitaApiClient() {
            @Override
            public String getBaseUrl() { return "http://local"; }
            @Override
            public List<CitaDTO> obtenerCitasPaciente(Solicitante solicitante, Long pacienteId) { return Collections.emptyList(); }
            @Override
            public CitaDTO obtenerCita(Solicitante solicitante, Long citaId) {
                throw new ApiException(new ApiError("CITA_NOT_FOUND", "No se ha encontrado la cita con id " + citaId, 404));
            }
            @Override
            public CitaDTO asignarCita(Solicitante solicitante, Long citaId, LocalDate fecha, LocalTime hora) { return null; }
            @Override
            public CitaDTO reprogramarCita(Solicitante solicitante, Long citaId, LocalDate fecha, LocalTime hora) { return null; }
            @Override
            public CitaDTO cancelarCita(Solicitante solicitante, Long citaId) { return null; }
        };
        CitaViewEspia vista = new CitaViewEspia();
        CitaController controller = new CitaControllerImpl(api, vista);

        controller.consultarDetalleCita(doctor, 999L);

        assertEquals("No se ha encontrado la cita con id 999", vista.ultimoMensaje);
    }

    @Test
    void errorDeApiSeMuestraComoMensajeBasico() {
        Solicitante doctor = new Solicitante("12345678A", RolSolicitante.DOCTOR);
        CitaApiClient api = new CitaApiClient() {
            @Override
            public String getBaseUrl() { return "http://local"; }
            @Override
            public List<CitaDTO> obtenerCitasPaciente(Solicitante solicitante, Long pacienteId) {
                throw new ApiException(new ApiError("CITA_INVALID_REQUEST", "pacienteId no puede ser nulo", 400));
            }
            @Override
            public CitaDTO obtenerCita(Solicitante solicitante, Long citaId) { return null; }
            @Override
            public CitaDTO asignarCita(Solicitante solicitante, Long citaId, LocalDate fecha, LocalTime hora) { return null; }
            @Override
            public CitaDTO reprogramarCita(Solicitante solicitante, Long citaId, LocalDate fecha, LocalTime hora) { return null; }
            @Override
            public CitaDTO cancelarCita(Solicitante solicitante, Long citaId) { return null; }
        };
        CitaViewEspia vista = new CitaViewEspia();
        CitaController controller = new CitaControllerImpl(api, vista);

        controller.consultarMisCitas(doctor, null);

        assertEquals("pacienteId no puede ser nulo", vista.ultimoMensaje);
    }

    static class CitaViewEspia implements CitaView {
        CitaDTO ultimaCita;
        List<CitaDTO> ultimasCitas;
        String ultimoMensaje;
        @Override public void mostrarCita(CitaDTO cita) { this.ultimaCita = cita; }
        @Override public void mostrarCitas(List<CitaDTO> citas) { this.ultimasCitas = citas; }
        @Override public void mostrarMensaje(String mensaje) { this.ultimoMensaje = mensaje; }
    }
}
