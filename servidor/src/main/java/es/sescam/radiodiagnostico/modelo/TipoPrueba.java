package es.sescam.radiodiagnostico.modelo;

import es.sescam.radiodiagnostico.modelo.enums.Modalidad;

public class TipoPrueba {

    private Long id;
    private String nombre;
    private Modalidad modalidad;
    private Integer duracionEstimada;
    private Boolean requiereContraste;
    private String preparacionPrevia;

    public TipoPrueba() {
    }

    public TipoPrueba(Long id, String nombre, Modalidad modalidad, Integer duracionEstimada,
                      Boolean requiereContraste, String preparacionPrevia) {
        this.id = id;
        this.nombre = nombre;
        this.modalidad = modalidad;
        this.duracionEstimada = duracionEstimada;
        this.requiereContraste = requiereContraste;
        this.preparacionPrevia = preparacionPrevia;
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

    public Integer getDuracionEstimada() {
        return duracionEstimada;
    }

    public void setDuracionEstimada(Integer duracionEstimada) {
        this.duracionEstimada = duracionEstimada;
    }

    public Boolean getRequiereContraste() {
        return requiereContraste;
    }

    public void setRequiereContraste(Boolean requiereContraste) {
        this.requiereContraste = requiereContraste;
    }

    public String getPreparacionPrevia() {
        return preparacionPrevia;
    }

    public void setPreparacionPrevia(String preparacionPrevia) {
        this.preparacionPrevia = preparacionPrevia;
    }
}
