package es.sescam.servidor.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import es.sescam.servidor.model.Cita;

public interface CitaRepository {

    List<Cita> findAll();

    Optional<Cita> findById(Long id);

    Cita save(Cita cita);

    List<Cita> findByPacienteId(Long pacienteId);

    /**
     * Devuelve las citas de un doctor para una fecha concreta, sin aplicar reglas de negocio.
     *
     * @param doctorId identificador del doctor; si es nulo devuelve una lista vacía
     * @param fecha fecha de las citas buscadas
     * @return citas del doctor en esa fecha; nunca nulo
     */
    List<Cita> findByDoctorIdAndFecha(Long doctorId, LocalDate fecha);
}
