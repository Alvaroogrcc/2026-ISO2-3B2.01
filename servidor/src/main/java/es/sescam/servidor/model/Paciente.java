package es.sescam.servidor.model;

import java.time.LocalDate;

/**
 * Datos demográficos de un paciente del sistema.
 * <p>
 * La baja de un paciente es lógica: se conserva el registro y se informa la
 * {@link #getFechaBaja() fecha de baja}. Un paciente sin fecha de baja está activo.
 * </p>
 */
public class Paciente extends Persona {

    private LocalDate fechaNacimiento;
    private String direccion;
    private String numeroHistoriaClinica;
    private LocalDate fechaBaja;

    /**
     * Crea un paciente vacío.
     */
    public Paciente() {
    }

    /**
     * Crea un paciente con sus datos demográficos.
     *
     * @param id identificador interno del paciente
     * @param nombre nombre del paciente
     * @param apellidos apellidos del paciente
     * @param dni DNI del paciente
     * @param fechaNacimiento fecha de nacimiento del paciente
     * @param telefono teléfono de contacto
     * @param email correo electrónico de contacto
     * @param direccion dirección postal
     * @param numeroHistoriaClinica número de historia clínica
     */
    public Paciente(Long id, String nombre, String apellidos, String dni, LocalDate fechaNacimiento,
                    String telefono, String email, String direccion, String numeroHistoriaClinica) {
        super(id, nombre, apellidos, dni, telefono, email);
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.numeroHistoriaClinica = numeroHistoriaClinica;
    }

    /**
     * Devuelve la fecha de nacimiento del paciente.
     *
     * @return fecha de nacimiento
     */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Establece la fecha de nacimiento del paciente.
     *
     * @param fechaNacimiento fecha de nacimiento
     */
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Devuelve la dirección postal del paciente.
     *
     * @return dirección postal
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección postal del paciente.
     *
     * @param direccion dirección postal
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Devuelve el número de historia clínica del paciente.
     *
     * @return número de historia clínica
     */
    public String getNumeroHistoriaClinica() {
        return numeroHistoriaClinica;
    }

    /**
     * Establece el número de historia clínica del paciente.
     *
     * @param numeroHistoriaClinica número de historia clínica
     */
    public void setNumeroHistoriaClinica(String numeroHistoriaClinica) {
        this.numeroHistoriaClinica = numeroHistoriaClinica;
    }

    /**
     * Devuelve la fecha en la que el paciente causó baja, o {@code null} si sigue activo.
     *
     * @return fecha de baja o {@code null}
     */
    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    /**
     * Establece la fecha de baja del paciente.
     *
     * @param fechaBaja fecha de baja; {@code null} indica paciente activo
     */
    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    /**
     * Indica si el paciente está activo.
     *
     * @return {@code true} si no tiene fecha de baja; {@code false} en caso contrario
     */
    public boolean estaActivo() {
        return fechaBaja == null;
    }
}
