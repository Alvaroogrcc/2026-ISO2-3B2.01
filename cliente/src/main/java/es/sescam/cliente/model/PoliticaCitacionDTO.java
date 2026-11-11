package es.sescam.cliente.model;

/**
 * Copia de una política de citación que viaja por la API (CU-17).
 */
public class PoliticaCitacionDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private Integer diasAntelacionMinima;
    private Integer diasAntelacionMaxima;
    private Integer maximoCitasPorFranja;
    private Boolean activa;

    public PoliticaCitacionDTO() {
    }

    public PoliticaCitacionDTO(Long id, String nombre, String descripcion,
                               Integer diasAntelacionMinima, Integer diasAntelacionMaxima,
                               Integer maximoCitasPorFranja, Boolean activa) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.diasAntelacionMinima = diasAntelacionMinima;
        this.diasAntelacionMaxima = diasAntelacionMaxima;
        this.maximoCitasPorFranja = maximoCitasPorFranja;
        this.activa = activa;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getDiasAntelacionMinima() { return diasAntelacionMinima; }
    public void setDiasAntelacionMinima(Integer diasAntelacionMinima) { this.diasAntelacionMinima = diasAntelacionMinima; }
    public Integer getDiasAntelacionMaxima() { return diasAntelacionMaxima; }
    public void setDiasAntelacionMaxima(Integer diasAntelacionMaxima) { this.diasAntelacionMaxima = diasAntelacionMaxima; }
    public Integer getMaximoCitasPorFranja() { return maximoCitasPorFranja; }
    public void setMaximoCitasPorFranja(Integer maximoCitasPorFranja) { this.maximoCitasPorFranja = maximoCitasPorFranja; }
    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }
}
