package es.sescam.servidor.model;

/**
 * Protocolo de realización asociado a un tipo de prueba del catálogo (CU-17).
 */
public class Protocolo {

    private Long id;
    private Long tipoPruebaId;
    private String nombre;
    private String descripcion;
    private String pasos;
    private Boolean activo;

    public Protocolo() {
    }

    public Protocolo(Long id, Long tipoPruebaId, String nombre, String descripcion,
                     String pasos, Boolean activo) {
        this.id = id;
        this.tipoPruebaId = tipoPruebaId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.pasos = pasos;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTipoPruebaId() {
        return tipoPruebaId;
    }

    public void setTipoPruebaId(Long tipoPruebaId) {
        this.tipoPruebaId = tipoPruebaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPasos() {
        return pasos;
    }

    public void setPasos(String pasos) {
        this.pasos = pasos;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
