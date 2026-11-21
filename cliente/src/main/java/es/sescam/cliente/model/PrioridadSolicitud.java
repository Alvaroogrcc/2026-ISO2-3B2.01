package es.sescam.cliente.model;

/**
 * Prioridad que el médico puede asignar a una solicitud de prueba (CU-06).
 */
public enum PrioridadSolicitud {

    /** La prueba debe realizarse con carácter inmediato. */
    URGENTE,

    /** La prueba debe realizarse antes que las ordinarias. */
    PREFERENTE,

    /** La prueba no tiene urgencia ni preferencia. */
    ORDINARIA
}
