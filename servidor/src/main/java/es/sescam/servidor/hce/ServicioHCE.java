package es.sescam.servidor.hce;

import es.sescam.servidor.model.SolicitudPrueba;

/**
 * Contrato de integración con la historia clínica electrónica (HCE) para el
 * envío de solicitudes de prueba (CU-06).
 * <p>
 * Simula el intercambio HL7/FHIR previsto (RNF-FUN-02). Las implementaciones no
 * deben lanzar excepciones por fallos de red en esta iteración: se utiliza un
 * stub local. La solicitud recibida ya ha sido validada y persistida por el
 * servicio de solicitudes de prueba.
 * </p>
 */
public interface ServicioHCE {

    /**
     * Envía una solicitud de prueba a la historia clínica electrónica.
     *
     * @param solicitud solicitud prescrita; no puede ser {@code null}
     */
    void enviarSolicitud(SolicitudPrueba solicitud);
}
