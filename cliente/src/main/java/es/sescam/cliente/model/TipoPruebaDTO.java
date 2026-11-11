package es.sescam.cliente.model;

/**
 * Copia de un tipo de prueba del catálogo para viajar por la API del cliente
 * (CU-06 y CU-17).
 * <p>
 * Incluye los atributos que deben mostrarse y validarse al prescribir:
 * preparación previa, uso de contraste y exigencia de consentimiento informado
 * (RNF-FUN-03), además de los campos de configuración maestra del CU-17.
 * </p>
 */
public class TipoPruebaDTO {

    private Long id;
    private String nombre;
    private String modalidad;
    private String equipoSalaRequeridos;
    private Integer duracionEstimadaMinutos;
    private String preparacionPrevia;
    private Boolean usoContraste;
    private Boolean requiereConsentimientoInformado;
    private String requisitosSeguridad;
    private String prioridad;
    private Boolean permiteSeguimientoPeriodico;

    /**
     * Crea un tipo de prueba vacío.
     */
    public TipoPruebaDTO() {
    }

    /**
     * Crea un tipo de prueba con los datos necesarios para prescribirlo (CU-06).
     *
     * @param id identificador del tipo de prueba
     * @param nombre nombre de la prueba
     * @param modalidad modalidad de la prueba
     * @param duracionEstimadaMinutos duración estimada en minutos
     * @param preparacionPrevia preparación previa indicada por el catálogo
     * @param usoContraste indica si la prueba utiliza contraste
     * @param requiereConsentimientoInformado indica si exige consentimiento informado
     */
    public TipoPruebaDTO(Long id, String nombre, String modalidad, Integer duracionEstimadaMinutos,
                         String preparacionPrevia, Boolean usoContraste,
                         Boolean requiereConsentimientoInformado) {
        this.id = id;
        this.nombre = nombre;
        this.modalidad = modalidad;
        this.duracionEstimadaMinutos = duracionEstimadaMinutos;
        this.preparacionPrevia = preparacionPrevia;
        this.usoContraste = usoContraste;
        this.requiereConsentimientoInformado = requiereConsentimientoInformado;
    }

    /**
     * Crea un tipo de prueba con todos sus datos de configuración maestra (CU-17).
     *
     * @param id identificador del tipo de prueba
     * @param nombre nombre de la prueba
     * @param modalidad modalidad de la prueba
     * @param equipoSalaRequeridos equipo y sala requeridos
     * @param duracionEstimadaMinutos duración estimada en minutos
     * @param preparacionPrevia preparación previa indicada por el catálogo
     * @param usoContraste indica si la prueba utiliza contraste
     * @param requiereConsentimientoInformado indica si exige consentimiento informado
     * @param requisitosSeguridad requisitos de seguridad de la prueba
     * @param prioridad prioridad del tipo de prueba
     * @param permiteSeguimientoPeriodico indica si permite seguimiento periódico
     */
    public TipoPruebaDTO(Long id, String nombre, String modalidad, String equipoSalaRequeridos,
                         Integer duracionEstimadaMinutos, String preparacionPrevia, Boolean usoContraste,
                         Boolean requiereConsentimientoInformado, String requisitosSeguridad,
                         String prioridad, Boolean permiteSeguimientoPeriodico) {
        this.id = id;
        this.nombre = nombre;
        this.modalidad = modalidad;
        this.equipoSalaRequeridos = equipoSalaRequeridos;
        this.duracionEstimadaMinutos = duracionEstimadaMinutos;
        this.preparacionPrevia = preparacionPrevia;
        this.usoContraste = usoContraste;
        this.requiereConsentimientoInformado = requiereConsentimientoInformado;
        this.requisitosSeguridad = requisitosSeguridad;
        this.prioridad = prioridad;
        this.permiteSeguimientoPeriodico = permiteSeguimientoPeriodico;
    }

    /**
     * Devuelve el identificador del tipo de prueba.
     *
     * @return identificador
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador del tipo de prueba.
     *
     * @param id identificador
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre de la prueba.
     *
     * @return nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la prueba.
     *
     * @param nombre nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve la modalidad de la prueba.
     *
     * @return modalidad
     */
    public String getModalidad() {
        return modalidad;
    }

    /**
     * Establece la modalidad de la prueba.
     *
     * @param modalidad modalidad
     */
    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    /**
     * Devuelve el equipo y la sala requeridos por la prueba.
     *
     * @return equipo y sala requeridos
     */
    public String getEquipoSalaRequeridos() {
        return equipoSalaRequeridos;
    }

    /**
     * Establece el equipo y la sala requeridos por la prueba.
     *
     * @param equipoSalaRequeridos equipo y sala requeridos
     */
    public void setEquipoSalaRequeridos(String equipoSalaRequeridos) {
        this.equipoSalaRequeridos = equipoSalaRequeridos;
    }

    /**
     * Devuelve la duración estimada de la prueba.
     *
     * @return duración estimada en minutos
     */
    public Integer getDuracionEstimadaMinutos() {
        return duracionEstimadaMinutos;
    }

    /**
     * Establece la duración estimada de la prueba.
     *
     * @param duracionEstimadaMinutos duración estimada en minutos
     */
    public void setDuracionEstimadaMinutos(Integer duracionEstimadaMinutos) {
        this.duracionEstimadaMinutos = duracionEstimadaMinutos;
    }

    /**
     * Devuelve la preparación previa indicada por el catálogo.
     *
     * @return preparación previa
     */
    public String getPreparacionPrevia() {
        return preparacionPrevia;
    }

    /**
     * Establece la preparación previa indicada por el catálogo.
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
     * Devuelve los requisitos de seguridad de la prueba.
     *
     * @return requisitos de seguridad
     */
    public String getRequisitosSeguridad() {
        return requisitosSeguridad;
    }

    /**
     * Establece los requisitos de seguridad de la prueba.
     *
     * @param requisitosSeguridad requisitos de seguridad
     */
    public void setRequisitosSeguridad(String requisitosSeguridad) {
        this.requisitosSeguridad = requisitosSeguridad;
    }

    /**
     * Devuelve la prioridad del tipo de prueba.
     *
     * @return prioridad
     */
    public String getPrioridad() {
        return prioridad;
    }

    /**
     * Establece la prioridad del tipo de prueba.
     *
     * @param prioridad prioridad
     */
    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    /**
     * Indica si la prueba permite seguimiento periódico.
     *
     * @return {@code true} si permite seguimiento periódico
     */
    public Boolean getPermiteSeguimientoPeriodico() {
        return permiteSeguimientoPeriodico;
    }

    /**
     * Establece si la prueba permite seguimiento periódico.
     *
     * @param permiteSeguimientoPeriodico si permite seguimiento periódico
     */
    public void setPermiteSeguimientoPeriodico(Boolean permiteSeguimientoPeriodico) {
        this.permiteSeguimientoPeriodico = permiteSeguimientoPeriodico;
    }
}
