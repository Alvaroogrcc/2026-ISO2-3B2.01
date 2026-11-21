package es.sescam.servidor.repository;

import java.util.List;
import java.util.Optional;

import es.sescam.servidor.model.EstadoSolicitud;
import es.sescam.servidor.model.SolicitudPrueba;

/**
 * Repositorio de solicitudes de prueba diagnóstica (CU-06).
 * <p>
 * Solo almacena y recupera solicitudes; las reglas de negocio, el control de
 * acceso y la auditoría se aplican en el servicio.
 * </p>
 */
public interface SolicitudPruebaRepository {

    /**
     * Recupera todas las solicitudes de prueba registradas.
     *
     * @return lista de solicitudes
     */
    List<SolicitudPrueba> findAll();

    /**
     * Busca una solicitud por su identificador interno.
     *
     * @param id identificador de la solicitud
     * @return solicitud encontrada o {@link Optional#empty()} si no existe
     */
    Optional<SolicitudPrueba> findById(Long id);

    /**
     * Guarda una solicitud. Si no tiene identificador, se le asigna uno nuevo.
     *
     * @param solicitud solicitud a guardar
     * @return solicitud guardada
     */
    SolicitudPrueba save(SolicitudPrueba solicitud);

    /**
     * Recupera las solicitudes de un paciente.
     *
     * @param pacienteId identificador del paciente
     * @return solicitudes del paciente
     */
    List<SolicitudPrueba> findByPacienteId(Long pacienteId);

    /**
     * Recupera las solicitudes que se encuentran en un estado concreto.
     *
     * @param estado estado por el que filtrar
     * @return solicitudes en el estado indicado
     */
    List<SolicitudPrueba> findByEstado(EstadoSolicitud estado);
}
