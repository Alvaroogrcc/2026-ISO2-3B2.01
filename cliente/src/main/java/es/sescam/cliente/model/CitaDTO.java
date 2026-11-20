package es.sescam.cliente.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class CitaDTO {

    private Long id;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;
    private Long pacienteId;
    private Long doctorId;

    public CitaDTO() {
    }

    public CitaDTO(Long id, LocalDate fecha, LocalTime hora, String estado, Long pacienteId, Long doctorId) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.pacienteId = pacienteId;
        this.doctorId = doctorId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
}
