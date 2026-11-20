package es.sescam.cliente.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class CitaDTO {

    private Long id;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;
    private Long pacienteId;
    /**
     * Identificador del médico responsable que atiende/realiza la prueba en la cita.
     */
    private Long medicoResponsableId;
    private Long tipoPruebaId;

    public CitaDTO() {
    }

    public CitaDTO(Long id, LocalDate fecha, LocalTime hora, String estado,
                   Long pacienteId, Long medicoResponsableId, Long tipoPruebaId) {
        this.id = id;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.pacienteId = pacienteId;
        this.medicoResponsableId = medicoResponsableId;
        this.tipoPruebaId = tipoPruebaId;
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
    public Long getMedicoResponsableId() { return medicoResponsableId; }
    public void setMedicoResponsableId(Long medicoResponsableId) { this.medicoResponsableId = medicoResponsableId; }
    public Long getTipoPruebaId() { return tipoPruebaId; }
    public void setTipoPruebaId(Long tipoPruebaId) { this.tipoPruebaId = tipoPruebaId; }
}
