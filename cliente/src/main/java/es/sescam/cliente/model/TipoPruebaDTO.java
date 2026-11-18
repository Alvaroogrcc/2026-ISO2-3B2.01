package es.sescam.cliente.model;

/**
 * Copia de un tipo de prueba del catálogo para viajar por la API del cliente
 * (CU-06).
 * <p>
 * Incluye los atributos que deben mostrarse y validarse al prescribir:
 * preparación previa, uso de contraste y exigencia de consentimiento informado
 * (RNF-FUN-03).
 * </p>
 */
public class TipoPruebaDTO {

    private Long id;
    private String nombre;
    private String modalidad;
    private Integer duracionEstimadaMinutos;
    private String preparacionPrevia;
    private Boolean usoContraste;
    private Boolean requiereConsentimientoInformado;

    /**
     * Crea un tipo de prueba vacío.
     */
    public TipoPruebaDTO() {
    }

    /**
     * Crea un tipo de prueba con todos sus datos.
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
}
