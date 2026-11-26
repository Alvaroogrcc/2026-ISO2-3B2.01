package es.sescam.servidor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import es.sescam.servidor.auditoria.AuditoriaStub;

class AuditoriaStubTest {

    @Test
    void registraAccesosYEventos() {
        AuditoriaStub auditoria = new AuditoriaStub();
        auditoria.registrarAcceso("u1", "cita/1", "LECTURA", Instant.parse("2026-11-20T10:00:00Z"));
        auditoria.registrarEvento("LOGIN", "Inicio sesión", Instant.parse("2026-11-20T10:01:00Z"));
        assertEquals(2, auditoria.getEventos().size());
        assertTrue(auditoria.getEventos().get(0).startsWith("ACCESO|"));
    }
}
