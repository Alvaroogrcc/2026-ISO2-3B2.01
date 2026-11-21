package es.sescam.servidor.model;

/**
 * Estado por el que transcurre un informe clínico (CU-09).
 */
public enum EstadoInforme {

    /** El informe está en elaboración y puede modificarse. */
    BORRADOR,

    /** El informe ha sido firmado por el médico y queda inmutable. */
    FIRMADO
}
