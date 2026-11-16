package es.sescam.servidor.admision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import es.sescam.servidor.model.Paciente;

/**
 * Implementación stub del sistema de admisión para la Iteración 1.
 * <p>
 * No realiza llamadas de red: registra en memoria cada sincronización para poder
 * consultarla desde las pruebas o desde la clase de arranque. El registro se
 * guarda con el formato {@code ACCION|idPaciente}, por ejemplo {@code ALTA|1}.
 * </p>
 */
public class ServicioAdmisionStub implements ServicioAdmision {

    private final List<String> sincronizaciones = Collections.synchronizedList(new ArrayList<>());

    /**
     * Crea el stub de admisión sin sincronizaciones registradas.
     */
    public ServicioAdmisionStub() {
    }

    @Override
    public void sincronizarAlta(Paciente paciente) {
        registrar("ALTA", paciente);
    }

    @Override
    public void sincronizarModificacion(Paciente paciente) {
        registrar("MODIFICACION", paciente);
    }

    @Override
    public void sincronizarBaja(Paciente paciente) {
        registrar("BAJA", paciente);
    }

    /**
     * Devuelve una copia de las sincronizaciones registradas.
     *
     * @return lista de sincronizaciones en el orden en que se produjeron
     */
    public List<String> getSincronizaciones() {
        synchronized (sincronizaciones) {
            return new ArrayList<>(sincronizaciones);
        }
    }

    private void registrar(String accion, Paciente paciente) {
        Objects.requireNonNull(paciente, "paciente");
        String id = paciente.getId() == null ? "sin-id" : paciente.getId().toString();
        sincronizaciones.add(accion + "|" + id);
    }
}
