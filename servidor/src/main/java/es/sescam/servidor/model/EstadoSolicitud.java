package es.sescam.servidor.model;

/**
 * Estado por el que transcurre una solicitud de prueba diagnóstica (CU-06).
 */
public enum EstadoSolicitud {

    /** La solicitud ha sido prescrita y está pendiente de citación. */
    PENDIENTE,

    /** La solicitud ha sido citada por admisión. */
    CITADA,

    /** La prueba solicitada ya se ha realizado. */
    REALIZADA,

    /** La prueba se ha realizado y su informe está disponible. */
    INFORMADA,

    /** La solicitud ha sido cancelada. */
    CANCELADA
}
