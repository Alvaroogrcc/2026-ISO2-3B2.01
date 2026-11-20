package es.sescam.servidor.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import es.sescam.servidor.model.Cita;
import es.sescam.servidor.repository.CitaRepository;

public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;

    public CitaServiceImpl(CitaRepository citaRepository) {
        this.citaRepository = Objects.requireNonNull(citaRepository, "citaRepository");
    }

    @Override
    public Cita crear(Cita cita) {
        return citaRepository.save(cita);
    }

    @Override
    public Optional<Cita> obtenerPorId(Long id) {
        return citaRepository.findById(id);
    }

    @Override
    public List<Cita> listar() {
        return citaRepository.findAll();
    }
}
