package es.sescam.servidor.model;

import java.time.Instant;

/**
 * Solicitud de una prueba diagnóstica realizada por un médico (CU-06).
 * <p>
 * Además de los datos clínicos de la prescripción, conserva los atributos que el
 * catálogo de pruebas aplicó en el momento de prescribir (preparación previa, uso
 * de contraste y consentimiento informado), la versión del catálogo y el usuario
 * que los validó (RNF-FUN-03).
 * </p>
 */
public class SolicitudPrueba {

    private Long id;
    private Instant fechaSolicitud;
    private Prioridad prioridad;
    private String indicacionClinica;
    private String justificacion;
    private EstadoSolicitud estado;
    private Paciente paciente;
    private Doctor doctor;
    private TipoPrueba tipoPrueba;
    private boolean consentimientoInformadoAceptado;
    private String preparacionPrevia;
    private Boolean usoContraste;
    private Boolean requiereConsentimientoInformado;
    private String versionCatalogo;
    private String usuarioValidador;

    /**
     * Crea una solicitud de prueba vacía.
     */
    public SolicitudPrueba() {
    }

    /**
     * Crea una solicitud de prueba con todos sus datos.
     *
     * @param id identificador interno de la solicitud
     * @param fechaSolicitud fecha y hora de la prescripción
     * @param prioridad prioridad asignada a la solicitud
     * @param indicacionClinica indicación clínica de la prueba
     * @param justificacion justificación clínica de la prescripción
     * @param estado estado de la solicitud
     * @param paciente paciente para el que se solicita la prueba
     * @param doctor médico que prescribe la prueba
     * @param tipoPrueba prueba solicitada
     * @param consentimientoInformadoAceptado indica si se ha aceptado el consentimiento informado
     * @param preparacionPrevia preparación previa aplicada desde el catálogo
     * @param usoContraste indica si la prueba utiliza contraste
     * @param requiereConsentimientoInformado indica si la prueba exige consentimiento informado
     * @param versionCatalogo versión del catálogo aplicada
     * @param usuarioValidador usuario que validó los atributos del catálogo
     */
    public SolicitudPrueba(Long id, Instant fechaSolicitud, Prioridad prioridad, String indicacionClinica,
                           String justificacion, EstadoSolicitud estado, Paciente paciente, Doctor doctor,
                           TipoPrueba tipoPrueba, boolean consentimientoInformadoAceptado, String preparacionPrevia,
                           Boolean usoContraste, Boolean requiereConsentimientoInformado, String versionCatalogo,
                           String usuarioValidador) {
        this.id = id;
        this.fechaSolicitud = fechaSolicitud;
        this.prioridad = prioridad;
        this.indicacionClinica = indicacionClinica;
        this.justificacion = justificacion;
        this.estado = estado;
        this.paciente = paciente;
        this.doctor = doctor;
        this.tipoPrueba = tipoPrueba;
        this.consentimientoInformadoAceptado = consentimientoInformadoAceptado;
        this.preparacionPrevia = preparacionPrevia;
        this.usoContraste = usoContraste;
        this.requiereConsentimientoInformado = requiereConsentimientoInformado;
        this.versionCatalogo = versionCatalogo;
        this.usuarioValidador = usuarioValidador;
    }

    /**
     * Devuelve el identificador interno de la solicitud.
     *
     * @return identificador interno
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador interno de la solicitud.
     *
     * @param id identificador interno
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve la fecha y hora en que se prescribió la solicitud.
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
    public Prioridad getPrioridad() {
        return prioridad;
    }

    /**
     * Establece la prioridad de la solicitud.
     *
     * @param prioridad prioridad asignada
     */
    public void setPrioridad(Prioridad prioridad) {
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
     * @return estado de la solicitud
     */
    public EstadoSolicitud getEstado() {
        return estado;
    }

    /**
     * Establece el estado de la solicitud.
     *
     * @param estado estado de la solicitud
     */
    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    /**
     * Devuelve el paciente para el que se solicita la prueba.
     *
     * @return paciente de la solicitud
     */
    public Paciente getPaciente() {
        return paciente;
    }

    /**
     * Establece el paciente de la solicitud.
     *
     * @param paciente paciente de la solicitud
     */
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    /**
     * Devuelve el médico que prescribe la prueba.
     *
     * @return médico prescriptor
     */
    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * Establece el médico que prescribe la prueba.
     *
     * @param doctor médico prescriptor
     */
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    /**
     * Devuelve el tipo de prueba solicitado.
     *
     * @return tipo de prueba
     */
    public TipoPrueba getTipoPrueba() {
        return tipoPrueba;
    }

    /**
     * Establece el tipo de prueba solicitado.
     *
     * @param tipoPrueba tipo de prueba
     */
    public void setTipoPrueba(TipoPrueba tipoPrueba) {
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
