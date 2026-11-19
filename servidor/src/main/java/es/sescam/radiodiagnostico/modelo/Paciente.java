package es.sescam.radiodiagnostico.modelo;

import java.time.LocalDate;

public class Paciente extends Persona {

    private LocalDate fechaNacimiento;
    private String direccion;
    private String numeroHistoriaClinica;

    public Paciente() {
    }

    public Paciente(Long id, String nombre, String apellidos, String dni, LocalDate fechaNacimiento,
                    String telefono, String email, String direccion, String numeroHistoriaClinica) {
        super(id, nombre, apellidos, dni, telefono, email);
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.numeroHistoriaClinica = numeroHistoriaClinica;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNumeroHistoriaClinica() {
        return numeroHistoriaClinica;
    }

    public void setNumeroHistoriaClinica(String numeroHistoriaClinica) {
        this.numeroHistoriaClinica = numeroHistoriaClinica;
    }
}
