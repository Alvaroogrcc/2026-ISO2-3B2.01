package es.sescam.servidor.model;

/**
 * Prioridad de una solicitud o de una prueba diagnóstica.
 * <p>
 * Las prioridades {@link #URGENTE}, {@link #PREFERENTE} y {@link #ORDINARIA} son
 * las admitidas al prescribir una solicitud (CU-06). Los valores {@link #BAJA},
 * {@link #MEDIA} y {@link #ALTA} se conservan por compatibilidad con el modelo
 * base existente.
 * </p>
 */
public enum Prioridad {

    /** Prioridad baja. */
    BAJA,

    /** Prioridad media. */
    MEDIA,

    /** Prioridad alta. */
    ALTA,

    /** Prioridad urgente: la prueba debe realizarse con carácter inmediato. */
    URGENTE,

    /** Prioridad preferente: la prueba debe realizarse antes que las ordinarias. */
    PREFERENTE,

    /** Prioridad ordinaria: sin urgencia ni preferencia. */
    ORDINARIA
}
