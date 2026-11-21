package es.sescam.servidor.model;

/**
 * Configuración editable de un nivel de prioridad del catálogo (CU-17).
 * Los valores posibles siguen siendo los del enum compartido {@link Prioridad}.
 */
public class ParametroPrioridad {

    private Long id;
    private Prioridad prioridad;
    private String descripcion;
    private Integer diasMaximosEspera;
    private Boolean activo;

    public ParametroPrioridad() {
    }

    public ParametroPrioridad(Long id, Prioridad prioridad, String descripcion,
                              Integer diasMaximosEspera, Boolean activo) {
        this.id = id;
        this.prioridad = prioridad;
        this.descripcion = descripcion;
        this.diasMaximosEspera = diasMaximosEspera;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getDiasMaximosEspera() {
        return diasMaximosEspera;
    }

    public void setDiasMaximosEspera(Integer diasMaximosEspera) {
        this.diasMaximosEspera = diasMaximosEspera;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
