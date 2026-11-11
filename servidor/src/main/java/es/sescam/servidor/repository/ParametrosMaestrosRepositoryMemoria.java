package es.sescam.servidor.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Function;

import es.sescam.servidor.model.HorarioAtencion;
import es.sescam.servidor.model.ParametroPrioridad;
import es.sescam.servidor.model.PoliticaCitacion;
import es.sescam.servidor.model.Protocolo;
import es.sescam.servidor.model.TipoPrueba;

/**
 * Implementación en memoria del repositorio de parámetros maestros (CU-17).
 */
public class ParametrosMaestrosRepositoryMemoria implements ParametrosMaestrosRepository {

    private final ConcurrentMap<Long, TipoPrueba> tiposPrueba = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Protocolo> protocolos = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, HorarioAtencion> horarios = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, PoliticaCitacion> politicas = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, ParametroPrioridad> parametrosPrioridad = new ConcurrentHashMap<>();

    private final AtomicLong secuenciaTipos = new AtomicLong(0);
    private final AtomicLong secuenciaProtocolos = new AtomicLong(0);
    private final AtomicLong secuenciaHorarios = new AtomicLong(0);
    private final AtomicLong secuenciaPoliticas = new AtomicLong(0);
    private final AtomicLong secuenciaPrioridades = new AtomicLong(0);

    @Override
    public List<TipoPrueba> findAllTiposPrueba() {
        return listar(tiposPrueba);
    }

    @Override
    public Optional<TipoPrueba> findTipoPruebaById(Long id) {
        return buscar(tiposPrueba, id);
    }

    @Override
    public TipoPrueba saveTipoPrueba(TipoPrueba tipoPrueba) {
        return guardar(tiposPrueba, secuenciaTipos, tipoPrueba, TipoPrueba::getId, TipoPrueba::setId);
    }

    @Override
    public List<Protocolo> findAllProtocolos() {
        return listar(protocolos);
    }

    @Override
    public Optional<Protocolo> findProtocoloById(Long id) {
        return buscar(protocolos, id);
    }

    @Override
    public Protocolo saveProtocolo(Protocolo protocolo) {
        return guardar(protocolos, secuenciaProtocolos, protocolo, Protocolo::getId, Protocolo::setId);
    }

    @Override
    public List<HorarioAtencion> findAllHorarios() {
        return listar(horarios);
    }

    @Override
    public Optional<HorarioAtencion> findHorarioById(Long id) {
        return buscar(horarios, id);
    }

    @Override
    public HorarioAtencion saveHorario(HorarioAtencion horario) {
        return guardar(horarios, secuenciaHorarios, horario, HorarioAtencion::getId, HorarioAtencion::setId);
    }

    @Override
    public List<PoliticaCitacion> findAllPoliticas() {
        return listar(politicas);
    }

    @Override
    public Optional<PoliticaCitacion> findPoliticaById(Long id) {
        return buscar(politicas, id);
    }

    @Override
    public PoliticaCitacion savePolitica(PoliticaCitacion politica) {
        return guardar(politicas, secuenciaPoliticas, politica, PoliticaCitacion::getId, PoliticaCitacion::setId);
    }

    @Override
    public List<ParametroPrioridad> findAllParametrosPrioridad() {
        return listar(parametrosPrioridad);
    }

    @Override
    public Optional<ParametroPrioridad> findParametroPrioridadById(Long id) {
        return buscar(parametrosPrioridad, id);
    }

    @Override
    public ParametroPrioridad saveParametroPrioridad(ParametroPrioridad parametro) {
        return guardar(parametrosPrioridad, secuenciaPrioridades, parametro,
                ParametroPrioridad::getId, ParametroPrioridad::setId);
    }

    private <T> List<T> listar(ConcurrentMap<Long, T> mapa) {
        return new ArrayList<>(mapa.values());
    }

    private <T> Optional<T> buscar(ConcurrentMap<Long, T> mapa, Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(mapa.get(id));
    }

    private <T> T guardar(ConcurrentMap<Long, T> mapa, AtomicLong secuencia, T entidad,
                          Function<T, Long> getId, BiConsumer<T, Long> setId) {
        if (entidad == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }
        Long id = getId.apply(entidad);
        if (id == null) {
            id = secuencia.incrementAndGet();
            setId.accept(entidad, id);
        }
        final Long idFinal = id;
        secuencia.updateAndGet(actual -> Math.max(actual, idFinal));
        mapa.put(idFinal, entidad);
        return entidad;
    }
}
