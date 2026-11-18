package es.sescam.servidor.hce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import es.sescam.servidor.model.SolicitudPrueba;

/**
 * Implementación stub de la integración con la HCE para la Iteración 1 (CU-06).
 * <p>
 * No realiza llamadas de red: registra en memoria cada solicitud enviada para
 * poder consultarla desde las pruebas o desde la clase de arranque.
 * </p>
 */
public class ServicioHCEStub implements ServicioHCE {

    private final List<SolicitudPrueba> solicitudesEnviadas = Collections.synchronizedList(new ArrayList<>());

    /**
     * Crea el stub de la HCE sin solicitudes enviadas.
     */
    public ServicioHCEStub() {
    }

    @Override
    public void enviarSolicitud(SolicitudPrueba solicitud) {
        solicitudesEnviadas.add(Objects.requireNonNull(solicitud, "solicitud"));
    }

    /**
     * Devuelve una copia de las solicitudes enviadas a la HCE.
     *
     * @return solicitudes enviadas en el orden en que se registraron
     */
    public List<SolicitudPrueba> getSolicitudesEnviadas() {
        synchronized (solicitudesEnviadas) {
            return new ArrayList<>(solicitudesEnviadas);
        }
    }
}
