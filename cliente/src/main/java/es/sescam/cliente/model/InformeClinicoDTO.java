package es.sescam.cliente.model;

import java.time.Instant;

/**
 * Copia de un informe clínico para viajar por la API del cliente (CU-09).
 * <p>
 * Incluye las referencias a la solicitud, al paciente y al médico mediante sus
 * identificadores, de modo que el cliente no necesita acceder al modelo del
 * servidor.
 * </p>
 */
public class InformeClinicoDTO {

    private Long id;
    private Instant fecha;
    private String contenido;
    private String conclusion;
    private String estado;
    private Long solicitudId;
    private Long pacienteId;
    private Long doctorId;

    /**
     * Crea un informe clínico vacío.
     */
    public InformeClinicoDTO() {
    }

    /**
     * Crea un informe clínico con todos sus datos.
     *
     * @param id identificador del informe
     * @param fecha fecha y hora del informe
     * @param contenido contenido clínico del informe
     * @param conclusion conclusión del informe
     * @param estado estado del informe
     * @param solicitudId identificador de la solicitud de prueba
     * @param pacienteId identificador del paciente
     * @param doctorId identificador del médico emisor
     */
    public InformeClinicoDTO(Long id, Instant fecha, String contenido, String conclusion,
                             String estado, Long solicitudId, Long pacienteId, Long doctorId) {
        this.id = id;
        this.fecha = fecha;
        this.contenido = contenido;
        this.conclusion = conclusion;
        this.estado = estado;
        this.solicitudId = solicitudId;
        this.pacienteId = pacienteId;
        this.doctorId = doctorId;
    }

    /**
     * Devuelve el identificador del informe.
     *
     * @return identificador
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador del informe.
     *
     * @param id identificador
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve la fecha y hora del informe.
     *
     * @return fecha del informe
     */
    public Instant getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha y hora del informe.
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
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado del informe.
     *
     * @param estado estado del informe
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Devuelve el identificador de la solicitud de prueba asociada.
     *
     * @return identificador de la solicitud
     */
    public Long getSolicitudId() {
        return solicitudId;
    }

    /**
     * Establece el identificador de la solicitud de prueba asociada.
     *
     * @param solicitudId identificador de la solicitud
     */
    public void setSolicitudId(Long solicitudId) {
        this.solicitudId = solicitudId;
    }

    /**
     * Devuelve el identificador del paciente.
     *
     * @return identificador del paciente
     */
    public Long getPacienteId() {
        return pacienteId;
    }

    /**
     * Establece el identificador del paciente.
     *
     * @param pacienteId identificador del paciente
     */
    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    /**
     * Devuelve el identificador del médico emisor.
     *
     * @return identificador del médico
     */
    public Long getDoctorId() {
        return doctorId;
    }

    /**
     * Establece el identificador del médico emisor.
     *
     * @param doctorId identificador del médico
     */
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }
}
