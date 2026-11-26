package es.sescam.servidor.model;

/**
 * Elemento del catálogo de tipos de prueba diagnóstica.
 */
public class TipoPrueba {

    private Long id;
    private String nombre;
    private Modalidad modalidad;
    private String equipoSalaRequeridos;
    private Integer duracionEstimadaMinutos;
    private String preparacionPrevia;
    private Boolean usoContraste;
    private Boolean requiereConsentimientoInformado;
    private String requisitosSeguridad;
    private Prioridad prioridad;
    private Boolean permiteSeguimientoPeriodico;

    public TipoPrueba() {
    }

    public TipoPrueba(Long id, String nombre, Modalidad modalidad, String equipoSalaRequeridos,
                      Integer duracionEstimadaMinutos, String preparacionPrevia, Boolean usoContraste,
                      Boolean requiereConsentimientoInformado, String requisitosSeguridad,
                      Prioridad prioridad, Boolean permiteSeguimientoPeriodico) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(Modalidad modalidad) {
        this.modalidad = modalidad;
    }

    public String getEquipoSalaRequeridos() {
        return equipoSalaRequeridos;
    }

    public void setEquipoSalaRequeridos(String equipoSalaRequeridos) {
        this.equipoSalaRequeridos = equipoSalaRequeridos;
    }

    public Integer getDuracionEstimadaMinutos() {
        return duracionEstimadaMinutos;
    }

    public void setDuracionEstimadaMinutos(Integer duracionEstimadaMinutos) {
        this.duracionEstimadaMinutos = duracionEstimadaMinutos;
    }

    public String getPreparacionPrevia() {
        return preparacionPrevia;
    }

    public void setPreparacionPrevia(String preparacionPrevia) {
        this.preparacionPrevia = preparacionPrevia;
    }

    public Boolean getUsoContraste() {
        return usoContraste;
    }

    public void setUsoContraste(Boolean usoContraste) {
        this.usoContraste = usoContraste;
    }

    public Boolean getRequiereConsentimientoInformado() {
        return requiereConsentimientoInformado;
    }

    public void setRequiereConsentimientoInformado(Boolean requiereConsentimientoInformado) {
        this.requiereConsentimientoInformado = requiereConsentimientoInformado;
    }

    public String getRequisitosSeguridad() {
        return requisitosSeguridad;
    }

    public void setRequisitosSeguridad(String requisitosSeguridad) {
        this.requisitosSeguridad = requisitosSeguridad;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public Boolean getPermiteSeguimientoPeriodico() {
        return permiteSeguimientoPeriodico;
    }

    public void setPermiteSeguimientoPeriodico(Boolean permiteSeguimientoPeriodico) {
        this.permiteSeguimientoPeriodico = permiteSeguimientoPeriodico;
    }
}
