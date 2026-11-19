package es.sescam.radiodiagnostico.modelo;

public class Doctor extends Persona {

    private String numeroColegiado;
    private String especialidad;

    public Doctor() {
    }

    public Doctor(Long id, String nombre, String apellidos, String dni, String numeroColegiado,
                  String especialidad, String telefono, String email) {
        super(id, nombre, apellidos, dni, telefono, email);
        this.numeroColegiado = numeroColegiado;
        this.especialidad = especialidad;
    }

    public String getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(String numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}
