package es.sescam.radiodiagnostico.modelo;

public class Administrativo extends Persona {

    public Administrativo() {
    }

    public Administrativo(Long id, String nombre, String apellidos, String dni, String email, String telefono) {
        super(id, nombre, apellidos, dni, telefono, email);
    }
}
