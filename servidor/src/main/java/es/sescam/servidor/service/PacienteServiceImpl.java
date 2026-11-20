package es.sescam.servidor.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import es.sescam.servidor.model.Paciente;
import es.sescam.servidor.repository.PacienteRepository;

public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteServiceImpl(PacienteRepository pacienteRepository) {
        this.pacienteRepository = Objects.requireNonNull(pacienteRepository, "pacienteRepository");
    }

    @Override
    public Paciente crear(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    @Override
    public Optional<Paciente> obtenerPorId(Long id) {
        return pacienteRepository.findById(id);
    }

    @Override
    public List<Paciente> listar() {
        return pacienteRepository.findAll();
    }
}
