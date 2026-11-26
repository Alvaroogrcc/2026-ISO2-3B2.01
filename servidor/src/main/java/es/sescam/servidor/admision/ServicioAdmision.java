package es.sescam.servidor.admision;

import es.sescam.servidor.model.Paciente;

/**
 * Contrato de integración con el sistema de admisión (CU-12).
 * <p>
 * Define las notificaciones que el módulo de pacientes debe enviar cuando se
 * produce un alta, una modificación o una baja, de forma que ambos sistemas
 * mantengan los datos sincronizados.
 * </p>
 * <p>
 * Las implementaciones no deben lanzar excepciones por fallos de red: en esta
 * iteración se utiliza un stub local. Los pacientes recibidos ya han sido
 * validados y persistidos por el servicio de pacientes.
 * </p>
 */
public interface ServicioAdmision {

    /**
     * Notifica el alta de un paciente al sistema de admisión.
     *
     * @param paciente paciente dado de alta; no puede ser {@code null}
     */
    void sincronizarAlta(Paciente paciente);

    /**
     * Notifica la modificación de los datos de un paciente al sistema de admisión.
     *
     * @param paciente paciente modificado; no puede ser {@code null}
     */
    void sincronizarModificacion(Paciente paciente);

    /**
     * Notifica la baja de un paciente al sistema de admisión.
     *
     * @param paciente paciente dado de baja; no puede ser {@code null}
     */
    void sincronizarBaja(Paciente paciente);
}
