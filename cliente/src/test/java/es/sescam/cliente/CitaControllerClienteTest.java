package es.sescam.cliente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import es.sescam.cliente.controller.CitaController;
import es.sescam.cliente.controller.CitaControllerImpl;
import es.sescam.cliente.model.CitaDTO;
import es.sescam.cliente.view.CitaView;

class CitaControllerClienteTest {

    @Test
    void delegaEnLaVista() {
        CitaViewEspia vista = new CitaViewEspia();
        CitaController controller = new CitaControllerImpl(vista);

        CitaDTO cita = new CitaDTO(1L, null, null, "PENDIENTE", 10L, 20L);
        controller.mostrarCita(cita);
        controller.mostrarCitas(List.of(cita));
        controller.mostrarMensaje("ok");

        assertSame(cita, vista.ultimaCita);
        assertEquals(1, vista.ultimasCitas.size());
        assertEquals("ok", vista.ultimoMensaje);
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
