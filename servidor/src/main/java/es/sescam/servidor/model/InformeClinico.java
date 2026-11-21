package es.sescam.servidor.model;

import java.time.Instant;

/**
 * Informe clínico emitido por un médico a partir de una solicitud de prueba
 * diagnóstica (CU-09).
 * <p>
 * El informe queda asociado a la solicitud de prueba, al paciente y al médico que
 * lo emite. Mientras está en estado {@link EstadoInforme#BORRADOR} puede
 * modificarse; una vez firmado pasa a {@link EstadoInforme#FIRMADO} y deja de ser
 * editable.
 * </p>
 */
public class InformeClinico {

    private Long id;
    private Instant fecha;
    private String contenido;
    private String conclusion;
    private EstadoInforme estado;
    private SolicitudPrueba solicitud;
    private Paciente paciente;
    private Doctor doctor;

    /**
     * Crea un informe clínico vacío.
     */
    public InformeClinico() {
    }

    /**
     * Crea un informe clínico con todos sus datos.
     *
     * @param id identificador interno del informe
     * @param fecha fecha y hora de creación del informe
     * @param contenido contenido clínico del informe
     * @param conclusion conclusión del informe
     * @param estado estado del informe
     * @param solicitud solicitud de prueba asociada
     * @param paciente paciente al que se refiere el informe
     * @param doctor médico que emite el informe
     */
    public InformeClinico(Long id, Instant fecha, String contenido, String conclusion,
                          EstadoInforme estado, SolicitudPrueba solicitud, Paciente paciente,
                          Doctor doctor) {
        this.id = id;
        this.fecha = fecha;
        this.contenido = contenido;
        this.conclusion = conclusion;
        this.estado = estado;
        this.solicitud = solicitud;
        this.paciente = paciente;
        this.doctor = doctor;
    }

    /**
     * Devuelve el identificador interno del informe.
     *
     * @return identificador interno
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador interno del informe.
     *
     * @param id identificador interno
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve la fecha y hora de creación del informe.
     *
     * @return fecha del informe
     */
    public Instant getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha y hora de creación del informe.
     *
     * @param fecha fecha del informe
     */
    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    /**
     * Devuelve el contenido clínico del informe.
     *
     * @return contenido del informe
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * Establece el contenido clínico del informe.
     *
     * @param contenido contenido del informe
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    /**
     * Devuelve la conclusión del informe.
     *
     * @return conclusión del informe
     */
    public String getConclusion() {
        return conclusion;
    }

    /**
     * Establece la conclusión del informe.
     *
     * @param conclusion conclusión del informe
     */
    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    /**
     * Devuelve el estado del informe.
     *
     * @return estado del informe
     */
    public EstadoInforme getEstado() {
        return estado;
    }

    /**
     * Establece el estado del informe.
     *
     * @param estado estado del informe
     */
    public void setEstado(EstadoInforme estado) {
        this.estado = estado;
    }

    /**
     * Devuelve la solicitud de prueba asociada al informe.
     *
     * @return solicitud de prueba
     */
    public SolicitudPrueba getSolicitud() {
        return solicitud;
    }

    /**
     * Establece la solicitud de prueba asociada al informe.
     *
     * @param solicitud solicitud de prueba
     */
    public void setSolicitud(SolicitudPrueba solicitud) {
        this.solicitud = solicitud;
    }

    /**
     * Devuelve el paciente al que se refiere el informe.
     *
     * @return paciente del informe
     */
    public Paciente getPaciente() {
        return paciente;
    }

    /**
     * Establece el paciente al que se refiere el informe.
     *
     * @param paciente paciente del informe
     */
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    /**
     * Devuelve el médico que emite el informe.
     *
     * @return médico emisor
     */
    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * Establece el médico que emite el informe.
     *
     * @param doctor médico emisor
     */
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
