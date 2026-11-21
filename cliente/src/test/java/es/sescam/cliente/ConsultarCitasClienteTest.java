package es.sescam.cliente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import es.sescam.cliente.controller.CitaController;
import es.sescam.cliente.controller.CitaControllerImpl;
import es.sescam.cliente.model.CitaDTO;
import es.sescam.cliente.service.CitaApiClient;
import es.sescam.cliente.service.error.ApiError;
import es.sescam.cliente.service.error.ApiException;
import es.sescam.cliente.view.CitaView;

class ConsultarCitasClienteTest {

    @Test
    void consultarMisCitasPideAlApiClientYMuestraEnVista() {
        CitaDTO c1 = new CitaDTO(1L, null, null, "PENDIENTE", 10L, 20L);
        CitaApiClient api = new CitaApiClient() {
            @Override
            public String getBaseUrl() { return "http://local"; }
            @Override
            public List<CitaDTO> obtenerCitasPaciente(Long pacienteId) {
                assertEquals(10L, pacienteId);
                return List.of(c1);
            }
            @Override
            public CitaDTO obtenerCita(Long citaId) { return null; }
        };
        CitaViewEspia vista = new CitaViewEspia();
        CitaController controller = new CitaControllerImpl(api, vista);

        controller.consultarMisCitas(10L);

        assertNotNull(vista.ultimasCitas);
        assertEquals(1, vista.ultimasCitas.size());
        assertEquals(1L, vista.ultimasCitas.get(0).getId());
    }

    @Test
    void consultarDetalleCitaInexistenteMuestraMensaje() {
        CitaApiClient api = new CitaApiClient() {
            @Override
            public String getBaseUrl() { return "http://local"; }
            @Override
            public List<CitaDTO> obtenerCitasPaciente(Long pacienteId) { return Collections.emptyList(); }
            @Override
            public CitaDTO obtenerCita(Long citaId) { return null; }
        };
        CitaViewEspia vista = new CitaViewEspia();
        CitaController controller = new CitaControllerImpl(api, vista);

        controller.consultarDetalleCita(999L);

        assertEquals("No se ha encontrado la cita solicitada.", vista.ultimoMensaje);
    }

    @Test
    void errorDeApiSeMuestraComoMensajeBasico() {
        CitaApiClient api = new CitaApiClient() {
            @Override
            public String getBaseUrl() { return "http://local"; }
            @Override
            public List<CitaDTO> obtenerCitasPaciente(Long pacienteId) {
                throw new ApiException(new ApiError("CITA_INVALID_REQUEST", "pacienteId no puede ser nulo", 400));
            }
            @Override
            public CitaDTO obtenerCita(Long citaId) { return null; }
        };
        CitaViewEspia vista = new CitaViewEspia();
        CitaController controller = new CitaControllerImpl(api, vista);

        controller.consultarMisCitas(null);

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
