package es.sescam.servidor.autorizacion;

import es.sescam.servidor.seguridad.Usuario;

/**
 * Contrato de autorización de acceso a datos clínicos de un paciente (CU-09).
 * <p>
 * Comprueba que el usuario mantiene una relación clínica autorizada con el
 * paciente antes de permitir el acceso a sus informes o imágenes. La
 * implementación definitiva dependerá de la historia clínica electrónica real.
 * </p>
 */
public interface ServicioAutorizacionClinica {

    /**
     * Indica si el usuario tiene una relación clínica autorizada con el paciente.
     *
     * @param usuario usuario que solicita el acceso
     * @param pacienteId identificador del paciente
     * @return {@code true} si la relación clínica está autorizada; {@code false}
     *         en caso contrario
     */
    boolean tieneRelacionClinica(Usuario usuario, Long pacienteId);
}
