package es.sescam.servidor.hce;

import es.sescam.servidor.model.InformeClinico;
import es.sescam.servidor.model.SolicitudPrueba;

/**
 * Contrato de integración con la historia clínica electrónica (HCE) para el
 * envío de solicitudes de prueba (CU-06) y de informes clínicos (CU-09).
 * <p>
 * Simula el intercambio HL7/FHIR previsto (RNF-FUN-02). Las implementaciones no
 * deben lanzar excepciones por fallos de red en esta iteración: se utiliza un
 * stub local. La solicitud o el informe recibidos ya han sido validados y
 * persistidos por el servicio correspondiente.
 * </p>
 */
public interface ServicioHCE {

    /**
     * Envía una solicitud de prueba a la historia clínica electrónica.
     *
     * @param solicitud solicitud prescrita; no puede ser {@code null}
     */
    void enviarSolicitud(SolicitudPrueba solicitud);

    /**
     * Envía un informe clínico firmado a la historia clínica electrónica.
     * <p>
     * Precondición: el informe debe haber sido validado, persistido y firmado por
     * el servicio de informes clínicos.
     * </p>
     *
     * @param informe informe firmado; no puede ser {@code null}
     */
    void enviarInforme(InformeClinico informe);
}
