package es.sescam.cliente.model;

/**
 * Copia de la configuración de un nivel de prioridad que viaja por la API (CU-17).
 */
public class ParametroPrioridadDTO {

    private Long id;
    private String prioridad;
    private String descripcion;
    private Integer diasMaximosEspera;
    private Boolean activo;

    public ParametroPrioridadDTO() {
    }

    public ParametroPrioridadDTO(Long id, String prioridad, String descripcion,
                                 Integer diasMaximosEspera, Boolean activo) {
        this.id = id;
        this.prioridad = prioridad;
        this.descripcion = descripcion;
        this.diasMaximosEspera = diasMaximosEspera;
        this.activo = activo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getDiasMaximosEspera() { return diasMaximosEspera; }
    public void setDiasMaximosEspera(Integer diasMaximosEspera) { this.diasMaximosEspera = diasMaximosEspera; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
