package es.sescam.radiodiagnostico.modelo;

import java.time.LocalDate;

public class InformeMedico {

    private Long id;
    private LocalDate fecha;
    private String contenido;
    private Doctor doctor;
    private SolicitudPrueba solicitud;

    public InformeMedico() {
    }

    public InformeMedico(Long id, LocalDate fecha, String contenido, Doctor doctor, SolicitudPrueba solicitud) {
        this.id = id;
        this.fecha = fecha;
        this.contenido = contenido;
        this.doctor = doctor;
        this.solicitud = solicitud;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public SolicitudPrueba getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudPrueba solicitud) {
        this.solicitud = solicitud;
    }
}
