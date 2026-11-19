package es.sescam.radiodiagnostico.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

import es.sescam.radiodiagnostico.modelo.enums.EstadoCita;

public class Cita {

    private Long id;
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoCita estado;
    private Paciente paciente;
    private Doctor doctor;
    private SolicitudPrueba solicitud;

    public Cita() {
    }

    public Cita(Long id, LocalDate fecha, LocalTime hora, EstadoCita estado,
                Paciente paciente, Doctor doctor, SolicitudPrueba solicitud) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.paciente = paciente;
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

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
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

    public SolicitudPrueba getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudPrueba solicitud) {
        this.solicitud = solicitud;
    }
}
