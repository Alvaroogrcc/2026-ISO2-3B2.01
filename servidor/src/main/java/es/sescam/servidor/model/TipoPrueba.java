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
    private Boolean requiereContraste;
    private Boolean requiereConsentimientoInformado;
    private String requisitosSeguridad;
    private String prioridadPorDefecto;
    private Boolean permiteSeguimientoPeriodico;

    public TipoPrueba() {
    }

    public TipoPrueba(Long id, String nombre, Modalidad modalidad, String equipoSalaRequeridos,
                      Integer duracionEstimadaMinutos, String preparacionPrevia, Boolean requiereContraste,
                      Boolean requiereConsentimientoInformado, String requisitosSeguridad,
                      String prioridadPorDefecto, Boolean permiteSeguimientoPeriodico) {
        this.id = id;
        this.nombre = nombre;
        this.modalidad = modalidad;
        this.equipoSalaRequeridos = equipoSalaRequeridos;
        this.duracionEstimadaMinutos = duracionEstimadaMinutos;
        this.preparacionPrevia = preparacionPrevia;
        this.requiereContraste = requiereContraste;
        this.requiereConsentimientoInformado = requiereConsentimientoInformado;
        this.requisitosSeguridad = requisitosSeguridad;
        this.prioridadPorDefecto = prioridadPorDefecto;
        this.permiteSeguimientoPeriodico = permiteSeguimientoPeriodico;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Modalidad getModalidad() { return modalidad; }
    public void setModalidad(Modalidad modalidad) { this.modalidad = modalidad; }
    public String getEquipoSalaRequeridos() { return equipoSalaRequeridos; }
    public void setEquipoSalaRequeridos(String equipoSalaRequeridos) { this.equipoSalaRequeridos = equipoSalaRequeridos; }
    public Integer getDuracionEstimadaMinutos() { return duracionEstimadaMinutos; }
    public void setDuracionEstimadaMinutos(Integer duracionEstimadaMinutos) { this.duracionEstimadaMinutos = duracionEstimadaMinutos; }
    public String getPreparacionPrevia() { return preparacionPrevia; }
    public void setPreparacionPrevia(String preparacionPrevia) { this.preparacionPrevia = preparacionPrevia; }
    public Boolean getRequiereContraste() { return requiereContraste; }
    public void setRequiereContraste(Boolean requiereContraste) { this.requiereContraste = requiereContraste; }
    public Boolean getRequiereConsentimientoInformado() { return requiereConsentimientoInformado; }
    public void setRequiereConsentimientoInformado(Boolean requiereConsentimientoInformado) { this.requiereConsentimientoInformado = requiereConsentimientoInformado; }
    public String getRequisitosSeguridad() { return requisitosSeguridad; }
    public void setRequisitosSeguridad(String requisitosSeguridad) { this.requisitosSeguridad = requisitosSeguridad; }
    public String getPrioridadPorDefecto() { return prioridadPorDefecto; }
    public void setPrioridadPorDefecto(String prioridadPorDefecto) { this.prioridadPorDefecto = prioridadPorDefecto; }
    public Boolean getPermiteSeguimientoPeriodico() { return permiteSeguimientoPeriodico; }
    public void setPermiteSeguimientoPeriodico(Boolean permiteSeguimientoPeriodico) { this.permiteSeguimientoPeriodico = permiteSeguimientoPeriodico; }
}
