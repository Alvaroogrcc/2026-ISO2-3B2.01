package es.sescam.cliente.model;

import java.time.LocalDate;

/**
 * Copia de los datos de un paciente para viajar por la API del cliente.
 * <p>
 * Incluye la fecha de baja y el estado activo para que las vistas puedan mostrar
 * pacientes activos y dados de baja sin acceder al modelo del servidor.
 * </p>
 */
public class PacienteDTO {

    private Long id;
    private String nombre;
    private String apellidos;
    private String dni;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String email;
    private String direccion;
    private String numeroHistoriaClinica;
    private LocalDate fechaBaja;

    /**
     * Crea un paciente vacío.
     */
    public PacienteDTO() {
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
    public PacienteDTO(Long id, String nombre, String apellidos, String dni, LocalDate fechaNacimiento,
                       String telefono, String email, String direccion, String numeroHistoriaClinica) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.numeroHistoriaClinica = numeroHistoriaClinica;
    }

    /**
     * Devuelve el identificador interno del paciente.
     *
     * @return identificador interno
     */
    public Long getId() { return id; }

    /**
     * Establece el identificador interno del paciente.
     *
     * @param id identificador interno
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Devuelve el nombre del paciente.
     *
     * @return nombre
     */
    public String getNombre() { return nombre; }

    /**
     * Establece el nombre del paciente.
     *
     * @param nombre nombre
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Devuelve los apellidos del paciente.
     *
     * @return apellidos
     */
    public String getApellidos() { return apellidos; }

    /**
     * Establece los apellidos del paciente.
     *
     * @param apellidos apellidos
     */
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    /**
     * Devuelve el DNI del paciente.
     *
     * @return DNI
     */
    public String getDni() { return dni; }

    /**
     * Establece el DNI del paciente.
     *
     * @param dni DNI
     */
    public void setDni(String dni) { this.dni = dni; }

    /**
     * Devuelve la fecha de nacimiento del paciente.
     *
     * @return fecha de nacimiento
     */
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }

    /**
     * Establece la fecha de nacimiento del paciente.
     *
     * @param fechaNacimiento fecha de nacimiento
     */
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    /**
     * Devuelve el teléfono de contacto del paciente.
     *
     * @return teléfono de contacto
     */
    public String getTelefono() { return telefono; }

    /**
     * Establece el teléfono de contacto del paciente.
     *
     * @param telefono teléfono de contacto
     */
    public void setTelefono(String telefono) { this.telefono = telefono; }

    /**
     * Devuelve el correo electrónico de contacto del paciente.
     *
     * @return correo electrónico
     */
    public String getEmail() { return email; }

    /**
     * Establece el correo electrónico de contacto del paciente.
     *
     * @param email correo electrónico
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Devuelve la dirección postal del paciente.
     *
     * @return dirección postal
     */
    public String getDireccion() { return direccion; }

    /**
     * Establece la dirección postal del paciente.
     *
     * @param direccion dirección postal
     */
    public void setDireccion(String direccion) { this.direccion = direccion; }

    /**
     * Devuelve el número de historia clínica del paciente.
     *
     * @return número de historia clínica
     */
    public String getNumeroHistoriaClinica() { return numeroHistoriaClinica; }

    /**
     * Establece el número de historia clínica del paciente.
     *
     * @param numeroHistoriaClinica número de historia clínica
     */
    public void setNumeroHistoriaClinica(String numeroHistoriaClinica) { this.numeroHistoriaClinica = numeroHistoriaClinica; }

    /**
     * Devuelve la fecha de baja del paciente.
     *
     * @return fecha de baja o {@code null} si el paciente está activo
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
    public boolean isActivo() {
        return fechaBaja == null;
    }
}
