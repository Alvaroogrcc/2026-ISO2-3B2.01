package es.sescam.cliente.model;

import java.time.Instant;

/**
 * Copia de una solicitud de prueba diagnóstica para viajar por la API del
 * cliente (CU-06).
 * <p>
 * Incluye los atributos del catálogo aplicados en la prescripción, la versión del
 * catálogo y el usuario que los validó (RNF-FUN-03).
 * </p>
 */
public class SolicitudPruebaDTO {

    private Long id;
    private Instant fechaSolicitud;
    private PrioridadSolicitud prioridad;
    private String indicacionClinica;
    private String justificacion;
    private String estado;
    private Long pacienteId;
    private Long doctorId;
    private TipoPruebaDTO tipoPrueba;
    private boolean consentimientoInformadoAceptado;
    private String preparacionPrevia;
    private Boolean usoContraste;
    private Boolean requiereConsentimientoInformado;
    private String versionCatalogo;
    private String usuarioValidador;

    /**
     * Crea una solicitud de prueba vacía.
     */
    public SolicitudPruebaDTO() {
    }

    /**
     * Crea una solicitud de prueba con todos sus datos.
     *
     * @param id identificador de la solicitud
     * @param fechaSolicitud fecha y hora de la prescripción
     * @param prioridad prioridad asignada
     * @param indicacionClinica indicación clínica
     * @param justificacion justificación clínica
     * @param estado estado de la solicitud
     * @param pacienteId identificador del paciente
     * @param doctorId identificador del médico prescriptor
     * @param tipoPrueba tipo de prueba solicitado
     * @param consentimientoInformadoAceptado indica si se ha aceptado el consentimiento
     * @param preparacionPrevia preparación previa aplicada desde el catálogo
     * @param usoContraste indica si la prueba utiliza contraste
     * @param requiereConsentimientoInformado indica si la prueba exige consentimiento
     * @param versionCatalogo versión del catálogo aplicada
     * @param usuarioValidador usuario que validó los atributos del catálogo
     */
    public SolicitudPruebaDTO(Long id, Instant fechaSolicitud, PrioridadSolicitud prioridad,
                              String indicacionClinica, String justificacion, String estado, Long pacienteId,
                              Long doctorId, TipoPruebaDTO tipoPrueba, boolean consentimientoInformadoAceptado,
                              String preparacionPrevia, Boolean usoContraste,
                              Boolean requiereConsentimientoInformado, String versionCatalogo,
                              String usuarioValidador) {
        this.id = id;
        this.fechaSolicitud = fechaSolicitud;
        this.prioridad = prioridad;
        this.indicacionClinica = indicacionClinica;
        this.justificacion = justificacion;
        this.estado = estado;
        this.pacienteId = pacienteId;
        this.doctorId = doctorId;
        this.tipoPrueba = tipoPrueba;
        this.consentimientoInformadoAceptado = consentimientoInformadoAceptado;
        this.preparacionPrevia = preparacionPrevia;
        this.usoContraste = usoContraste;
        this.requiereConsentimientoInformado = requiereConsentimientoInformado;
        this.versionCatalogo = versionCatalogo;
        this.usuarioValidador = usuarioValidador;
    }

    /**
     * Devuelve el identificador de la solicitud.
     *
     * @return identificador
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador de la solicitud.
     *
     * @param id identificador
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve la fecha y hora de la solicitud.
     *
     * @return fecha de la solicitud
     */
    public Instant getFechaSolicitud() {
        return fechaSolicitud;
    }

    /**
     * Establece la fecha y hora de la solicitud.
     *
     * @param fechaSolicitud fecha de la solicitud
     */
    public void setFechaSolicitud(Instant fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    /**
     * Devuelve la prioridad de la solicitud.
     *
     * @return prioridad asignada
     */
    public PrioridadSolicitud getPrioridad() {
        return prioridad;
    }

    /**
     * Establece la prioridad de la solicitud.
     *
     * @param prioridad prioridad asignada
     */
    public void setPrioridad(PrioridadSolicitud prioridad) {
        this.prioridad = prioridad;
    }

    /**
     * Devuelve la indicación clínica de la prueba.
     *
     * @return indicación clínica
     */
    public String getIndicacionClinica() {
        return indicacionClinica;
    }

    /**
     * Establece la indicación clínica de la prueba.
     *
     * @param indicacionClinica indicación clínica
     */
    public void setIndicacionClinica(String indicacionClinica) {
        this.indicacionClinica = indicacionClinica;
    }

    /**
     * Devuelve la justificación clínica de la prescripción.
     *
     * @return justificación clínica
     */
    public String getJustificacion() {
        return justificacion;
    }

    /**
     * Establece la justificación clínica de la prescripción.
     *
     * @param justificacion justificación clínica
     */
    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    /**
     * Devuelve el estado de la solicitud.
     *
     * @return estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado de la solicitud.
     *
     * @param estado estado
     */
    public void setEstado(String estado) {
        this.estado = estado;
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
     * Devuelve el identificador del médico prescriptor.
     *
     * @return identificador del médico
     */
    public Long getDoctorId() {
        return doctorId;
    }

    /**
     * Establece el identificador del médico prescriptor.
     *
     * @param doctorId identificador del médico
     */
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * Devuelve el tipo de prueba solicitado.
     *
     * @return tipo de prueba
     */
    public TipoPruebaDTO getTipoPrueba() {
        return tipoPrueba;
    }

    /**
     * Establece el tipo de prueba solicitado.
     *
     * @param tipoPrueba tipo de prueba
     */
    public void setTipoPrueba(TipoPruebaDTO tipoPrueba) {
        this.tipoPrueba = tipoPrueba;
    }

    /**
     * Indica si se ha aceptado el consentimiento informado.
     *
     * @return {@code true} si consta la aceptación; {@code false} en caso contrario
     */
    public boolean isConsentimientoInformadoAceptado() {
        return consentimientoInformadoAceptado;
    }

    /**
     * Establece si consta la aceptación del consentimiento informado.
     *
     * @param consentimientoInformadoAceptado aceptación del consentimiento
     */
    public void setConsentimientoInformadoAceptado(boolean consentimientoInformadoAceptado) {
        this.consentimientoInformadoAceptado = consentimientoInformadoAceptado;
    }

    /**
     * Devuelve la preparación previa aplicada desde el catálogo.
     *
     * @return preparación previa
     */
    public String getPreparacionPrevia() {
        return preparacionPrevia;
    }

    /**
     * Establece la preparación previa aplicada desde el catálogo.
     *
     * @param preparacionPrevia preparación previa
     */
    public void setPreparacionPrevia(String preparacionPrevia) {
        this.preparacionPrevia = preparacionPrevia;
    }

    /**
     * Indica si la prueba utiliza contraste.
     *
     * @return {@code true} si usa contraste; {@code false} o {@code null} en caso contrario
     */
    public Boolean getUsoContraste() {
        return usoContraste;
    }

    /**
     * Establece si la prueba utiliza contraste.
     *
     * @param usoContraste uso de contraste
     */
    public void setUsoContraste(Boolean usoContraste) {
        this.usoContraste = usoContraste;
    }

    /**
     * Indica si la prueba exige consentimiento informado.
     *
     * @return {@code true} si exige consentimiento; {@code false} o {@code null} en caso contrario
     */
    public Boolean getRequiereConsentimientoInformado() {
        return requiereConsentimientoInformado;
    }

    /**
     * Establece si la prueba exige consentimiento informado.
     *
     * @param requiereConsentimientoInformado exigencia de consentimiento
     */
    public void setRequiereConsentimientoInformado(Boolean requiereConsentimientoInformado) {
        this.requiereConsentimientoInformado = requiereConsentimientoInformado;
    }

    /**
     * Devuelve la versión del catálogo aplicada.
     *
     * @return versión del catálogo
     */
    public String getVersionCatalogo() {
        return versionCatalogo;
    }

    /**
     * Establece la versión del catálogo aplicada.
     *
     * @param versionCatalogo versión del catálogo
     */
    public void setVersionCatalogo(String versionCatalogo) {
        this.versionCatalogo = versionCatalogo;
    }

    /**
     * Devuelve el usuario que validó los atributos del catálogo.
     *
     * @return usuario validador
     */
    public String getUsuarioValidador() {
        return usuarioValidador;
    }

    /**
     * Establece el usuario que validó los atributos del catálogo.
     *
     * @param usuarioValidador usuario validador
     */
    public void setUsuarioValidador(String usuarioValidador) {
        this.usuarioValidador = usuarioValidador;
    }
}
