package es.sescam.radiodiagnostico.modelo;

import java.time.LocalDate;

import es.sescam.radiodiagnostico.modelo.enums.Prioridad;

public class SolicitudPrueba {

    private Long id;
    private LocalDate fechaSolicitud;
    private Prioridad prioridad;
    private String indicacionClinica;
    private String estado;
    private Paciente paciente;
    private Doctor doctor;
    private TipoPrueba tipoPrueba;

    public SolicitudPrueba() {
    }

    public SolicitudPrueba(Long id, LocalDate fechaSolicitud, Prioridad prioridad, String indicacionClinica,
                           String estado, Paciente paciente, Doctor doctor, TipoPrueba tipoPrueba) {
        this.id = id;
        this.fechaSolicitud = fechaSolicitud;
        this.prioridad = prioridad;
        this.indicacionClinica = indicacionClinica;
        this.estado = estado;
        this.paciente = paciente;
        this.doctor = doctor;
        this.tipoPrueba = tipoPrueba;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDate fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public String getIndicacionClinica() {
        return indicacionClinica;
    }

    public void setIndicacionClinica(String indicacionClinica) {
        this.indicacionClinica = indicacionClinica;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public TipoPrueba getTipoPrueba() {
        return tipoPrueba;
    }

    public void setTipoPrueba(TipoPrueba tipoPrueba) {
        this.tipoPrueba = tipoPrueba;
    }
}
