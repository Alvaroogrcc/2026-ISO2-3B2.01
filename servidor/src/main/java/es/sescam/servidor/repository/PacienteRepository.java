package es.sescam.servidor.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import es.sescam.servidor.model.Paciente;

public class PacienteRepository {

    private final List<Paciente> pacientes = new ArrayList<>();

    public List<Paciente> findAll() {
        return new ArrayList<>(pacientes);
    }

    public Optional<Paciente> findById(Long id) {
        return pacientes.stream().filter(p -> p.getId() != null && p.getId().equals(id)).findFirst();
    }

    public Paciente save(Paciente paciente) {
        pacientes.add(paciente);
        return paciente;
    }
}
