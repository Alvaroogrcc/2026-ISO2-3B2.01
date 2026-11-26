package es.sescam.servidor.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Franja horaria de atención configurable para la citación (CU-17).
 */
public class HorarioAtencion {

    private Long id;
    private DayOfWeek diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer duracionSlotMinutos;
    private Boolean activo;

    public HorarioAtencion() {
    }

    public HorarioAtencion(Long id, DayOfWeek diaSemana, LocalTime horaInicio, LocalTime horaFin,
                           Integer duracionSlotMinutos, Boolean activo) {
        this.id = id;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.duracionSlotMinutos = duracionSlotMinutos;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayOfWeek getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DayOfWeek diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public Integer getDuracionSlotMinutos() {
        return duracionSlotMinutos;
    }

    public void setDuracionSlotMinutos(Integer duracionSlotMinutos) {
        this.duracionSlotMinutos = duracionSlotMinutos;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
