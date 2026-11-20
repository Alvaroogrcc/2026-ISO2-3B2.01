package es.sescam.servidor.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Cita para la realización de una prueba diagnóstica.
 * El campo {@code medicoResponsable} representa al doctor que atiende/realiza la prueba en la cita.
 */
public class Cita {

    private Long id;
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoCita estado;
    private Paciente paciente;
    private Doctor medicoResponsable;
    private TipoPrueba tipoPrueba;

    public Cita() {
    }

    public Cita(Long id, LocalDate fecha, LocalTime hora, EstadoCita estado,
                Paciente paciente, Doctor medicoResponsable, TipoPrueba tipoPrueba) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.paciente = paciente;
        this.medicoResponsable = medicoResponsable;
        this.tipoPrueba = tipoPrueba;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }
    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public Doctor getMedicoResponsable() { return medicoResponsable; }
    public void setMedicoResponsable(Doctor medicoResponsable) { this.medicoResponsable = medicoResponsable; }
    public TipoPrueba getTipoPrueba() { return tipoPrueba; }
    public void setTipoPrueba(TipoPrueba tipoPrueba) { this.tipoPrueba = tipoPrueba; }
}
