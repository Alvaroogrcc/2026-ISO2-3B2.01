package es.sescam.servidor.repository;

import java.util.List;
import java.util.Optional;

import es.sescam.servidor.model.HorarioAtencion;
import es.sescam.servidor.model.ParametroPrioridad;
import es.sescam.servidor.model.PoliticaCitacion;
import es.sescam.servidor.model.Protocolo;
import es.sescam.servidor.model.TipoPrueba;

/**
 * Contrato de persistencia en memoria de la configuración global del CU-17.
 *
 * <p>Precondiciones: las entidades pasadas a los métodos {@code save} no pueden ser nulas.
 * Las búsquedas por identificador devuelven {@link Optional#empty()} cuando el id es nulo
 * o no existe. No aplica reglas de negocio ni control de acceso: es responsabilidad del
 * servicio. Tiempo máximo esperado: inmediato (sin acceso a red).</p>
 */
public interface ParametrosMaestrosRepository {

    List<TipoPrueba> findAllTiposPrueba();

    Optional<TipoPrueba> findTipoPruebaById(Long id);

    TipoPrueba saveTipoPrueba(TipoPrueba tipoPrueba);

    List<Protocolo> findAllProtocolos();

    Optional<Protocolo> findProtocoloById(Long id);

    Protocolo saveProtocolo(Protocolo protocolo);

    List<HorarioAtencion> findAllHorarios();

    Optional<HorarioAtencion> findHorarioById(Long id);

    HorarioAtencion saveHorario(HorarioAtencion horario);

    List<PoliticaCitacion> findAllPoliticas();

    Optional<PoliticaCitacion> findPoliticaById(Long id);

    PoliticaCitacion savePolitica(PoliticaCitacion politica);

    List<ParametroPrioridad> findAllParametrosPrioridad();

    Optional<ParametroPrioridad> findParametroPrioridadById(Long id);

    ParametroPrioridad saveParametroPrioridad(ParametroPrioridad parametro);
}
