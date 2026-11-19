package es.sescam.servidor.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import es.sescam.servidor.model.Cita;

public class CitaRepository {

    private final List<Cita> citas = new ArrayList<>();

    public List<Cita> findAll() {
        return new ArrayList<>(citas);
    }

    public Optional<Cita> findById(Long id) {
        return citas.stream().filter(c -> c.getId() != null && c.getId().equals(id)).findFirst();
    }

    public Cita save(Cita cita) {
        citas.add(cita);
        return cita;
    }
}
