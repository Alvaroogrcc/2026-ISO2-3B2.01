package es.sescam.servidor.repository;

import java.util.List;
import java.util.Optional;

import es.sescam.servidor.model.Paciente;

/**
 * Repositorio de pacientes.
 * <p>
 * Los pacientes se conservan aunque causen baja: la baja es lógica y se representa
 * mediante la fecha de baja del modelo. Este repositorio no aplica reglas de negocio.
 * </p>
 */
public interface PacienteRepository {

    /**
     * Recupera todos los pacientes, incluidos los dados de baja.
     *
     * @return lista de pacientes
     */
    List<Paciente> findAll();

    /**
     * Busca un paciente por su identificador interno.
     *
     * @param id identificador del paciente
     * @return paciente encontrado o {@link Optional#empty()} si no existe
     */
    Optional<Paciente> findById(Long id);

    /**
     * Guarda un paciente. Si no tiene identificador, se le asigna uno nuevo.
     *
     * @param paciente paciente a guardar
     * @return paciente guardado
     */
    Paciente save(Paciente paciente);

    /**
     * Busca un paciente por su DNI.
     *
     * @param dni DNI del paciente
     * @return paciente encontrado o {@link Optional#empty()} si no existe
     */
    Optional<Paciente> findByDni(String dni);

    /**
     * Busca un paciente por su número de historia clínica.
     *
     * @param numeroHistoriaClinica número de historia clínica del paciente
     * @return paciente encontrado o {@link Optional#empty()} si no existe
     */
    Optional<Paciente> findByNumeroHistoriaClinica(String numeroHistoriaClinica);
}
