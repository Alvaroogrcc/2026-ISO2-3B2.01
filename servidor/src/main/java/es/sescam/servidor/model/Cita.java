package es.sescam.servidor.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Cita {

    private Long id;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;
    private Paciente paciente;
    private Doctor doctor;

    public Cita() {
    }

    public Cita(Long id, LocalDate fecha, LocalTime hora, String estado, Paciente paciente, Doctor doctor) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.paciente = paciente;
        this.doctor = doctor;
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
}
