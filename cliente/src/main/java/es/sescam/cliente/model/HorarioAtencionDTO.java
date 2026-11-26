package es.sescam.cliente.model;

import java.time.LocalTime;

/**
 * Copia de una franja horaria configurable que viaja por la API (CU-17).
 */
public class HorarioAtencionDTO {

    private Long id;
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer duracionSlotMinutos;
    private Boolean activo;

    public HorarioAtencionDTO() {
    }

    public HorarioAtencionDTO(Long id, String diaSemana, LocalTime horaInicio, LocalTime horaFin,
                              Integer duracionSlotMinutos, Boolean activo) {
        this.id = id;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.duracionSlotMinutos = duracionSlotMinutos;
        this.activo = activo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDiaSemana() { return diaSemana; }
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    public Integer getDuracionSlotMinutos() { return duracionSlotMinutos; }
    public void setDuracionSlotMinutos(Integer duracionSlotMinutos) { this.duracionSlotMinutos = duracionSlotMinutos; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
