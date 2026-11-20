package es.sescam.servidor.repository;

import java.util.List;
import java.util.Optional;

import es.sescam.servidor.model.Cita;

public interface CitaRepository {

    List<Cita> findAll();

    Optional<Cita> findById(Long id);

    Cita save(Cita cita);
}
