package es.sescam.servidor.repository;

import java.util.List;
import java.util.Optional;

import es.sescam.servidor.model.InformeClinico;

/**
 * Repositorio de informes clínicos (CU-09).
 * <p>
 * Solo almacena y recupera informes; las reglas de negocio, el control de acceso
 * y la auditoría se aplican en el servicio.
 * </p>
 */
public interface InformeClinicoRepository {

    /**
     * Recupera todos los informes clínicos registrados.
     *
     * @return lista de informes
     */
    List<InformeClinico> findAll();

    /**
     * Busca un informe por su identificador interno.
     *
     * @param id identificador del informe
     * @return informe encontrado o {@link Optional#empty()} si no existe
     */
    Optional<InformeClinico> findById(Long id);

    /**
     * Guarda un informe. Si no tiene identificador, se le asigna uno nuevo.
     *
     * @param informe informe a guardar
     * @return informe guardado
     */
    InformeClinico save(InformeClinico informe);

    /**
     * Recupera los informes de un paciente.
     *
     * @param pacienteId identificador del paciente
     * @return informes del paciente
     */
    List<InformeClinico> findByPacienteId(Long pacienteId);

    /**
     * Recupera los informes asociados a una solicitud de prueba.
     *
     * @param solicitudId identificador de la solicitud
     * @return informes de la solicitud
     */
    List<InformeClinico> findBySolicitudId(Long solicitudId);
}
