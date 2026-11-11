package es.sescam.servidor.service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import es.sescam.servidor.auditoria.Auditoria;
import es.sescam.servidor.model.HorarioAtencion;
import es.sescam.servidor.model.ParametroPrioridad;
import es.sescam.servidor.model.PoliticaCitacion;
import es.sescam.servidor.model.Prioridad;
import es.sescam.servidor.model.Protocolo;
import es.sescam.servidor.model.TipoPrueba;
import es.sescam.servidor.repository.ParametrosMaestrosRepository;
import es.sescam.servidor.seguridad.Rol;
import es.sescam.servidor.seguridad.Usuario;

/**
 * Implementación de las reglas de negocio del CU-17.
 */
public class ParametrosMaestrosServiceImpl implements ParametrosMaestrosService {

    private static final String RECURSO = "parametros-maestros";

    private final ParametrosMaestrosRepository repositorio;
    private final Auditoria auditoria;

    public ParametrosMaestrosServiceImpl(ParametrosMaestrosRepository repositorio, Auditoria auditoria) {
        this.repositorio = Objects.requireNonNull(repositorio, "repositorio");
        this.auditoria = Objects.requireNonNull(auditoria, "auditoria");
    }

    @Override
    public List<TipoPrueba> listarTiposPrueba(Usuario solicitante) {
        validarAdministrador(solicitante);
        auditarLectura(solicitante, "tipos-prueba");
        return repositorio.findAllTiposPrueba();
    }

    @Override
    public Optional<TipoPrueba> obtenerTipoPrueba(Usuario solicitante, Long id) {
        validarAdministrador(solicitante);
        auditarLectura(solicitante, "tipos-prueba");
        return repositorio.findTipoPruebaById(id);
    }

    @Override
    public TipoPrueba crearTipoPrueba(Usuario solicitante, TipoPrueba tipoPrueba) {
        validarAdministrador(solicitante);
        validarTipoPrueba(tipoPrueba);
        tipoPrueba.setId(null);
        TipoPrueba guardado = repositorio.saveTipoPrueba(tipoPrueba);
        auditarEscritura(solicitante, "tipos-prueba", guardado.getId(), "CREACION");
        return guardado;
    }

    @Override
    public TipoPrueba actualizarTipoPrueba(Usuario solicitante, Long id, TipoPrueba datos) {
        validarAdministrador(solicitante);
        TipoPrueba anterior = obtenerExistente(repositorio.findTipoPruebaById(id), "el tipo de prueba", id);
        validarTipoPrueba(datos);
        datos.setId(anterior.getId());
        TipoPrueba guardado = repositorio.saveTipoPrueba(datos);
        auditarEscritura(solicitante, "tipos-prueba", guardado.getId(), "EDICION");
        return guardado;
    }

    @Override
    public List<Protocolo> listarProtocolos(Usuario solicitante) {
        validarAdministrador(solicitante);
        auditarLectura(solicitante, "protocolos");
        return repositorio.findAllProtocolos();
    }

    @Override
    public Optional<Protocolo> obtenerProtocolo(Usuario solicitante, Long id) {
        validarAdministrador(solicitante);
        auditarLectura(solicitante, "protocolos");
        return repositorio.findProtocoloById(id);
    }

    @Override
    public Protocolo crearProtocolo(Usuario solicitante, Protocolo protocolo) {
        validarAdministrador(solicitante);
        validarProtocolo(protocolo);
        protocolo.setId(null);
        Protocolo guardado = repositorio.saveProtocolo(protocolo);
        auditarEscritura(solicitante, "protocolos", guardado.getId(), "CREACION");
        return guardado;
    }

    @Override
    public Protocolo actualizarProtocolo(Usuario solicitante, Long id, Protocolo datos) {
        validarAdministrador(solicitante);
        Protocolo anterior = obtenerExistente(repositorio.findProtocoloById(id), "el protocolo", id);
        validarProtocolo(datos);
        datos.setId(anterior.getId());
        Protocolo guardado = repositorio.saveProtocolo(datos);
        auditarEscritura(solicitante, "protocolos", guardado.getId(), "EDICION");
        return guardado;
    }

    @Override
    public List<HorarioAtencion> listarHorarios(Usuario solicitante) {
        validarAdministrador(solicitante);
        auditarLectura(solicitante, "horarios");
        return repositorio.findAllHorarios();
    }

    @Override
    public Optional<HorarioAtencion> obtenerHorario(Usuario solicitante, Long id) {
        validarAdministrador(solicitante);
        auditarLectura(solicitante, "horarios");
        return repositorio.findHorarioById(id);
    }

    @Override
    public HorarioAtencion crearHorario(Usuario solicitante, HorarioAtencion horario) {
        validarAdministrador(solicitante);
        validarHorario(horario);
        validarSolape(horario);
        horario.setId(null);
        HorarioAtencion guardado = repositorio.saveHorario(horario);
        auditarEscritura(solicitante, "horarios", guardado.getId(), "CREACION");
        return guardado;
    }

    @Override
    public HorarioAtencion actualizarHorario(Usuario solicitante, Long id, HorarioAtencion datos) {
        validarAdministrador(solicitante);
        HorarioAtencion anterior = obtenerExistente(repositorio.findHorarioById(id), "el horario", id);
        validarHorario(datos);
        datos.setId(anterior.getId());
        validarSolape(datos);
        HorarioAtencion guardado = repositorio.saveHorario(datos);
        auditarEscritura(solicitante, "horarios", guardado.getId(), "EDICION");
        return guardado;
    }

    @Override
    public List<PoliticaCitacion> listarPoliticas(Usuario solicitante) {
        validarAdministrador(solicitante);
        auditarLectura(solicitante, "politicas-citacion");
        return repositorio.findAllPoliticas();
    }

    @Override
    public Optional<PoliticaCitacion> obtenerPolitica(Usuario solicitante, Long id) {
        validarAdministrador(solicitante);
        auditarLectura(solicitante, "politicas-citacion");
        return repositorio.findPoliticaById(id);
    }

    @Override
    public PoliticaCitacion crearPolitica(Usuario solicitante, PoliticaCitacion politica) {
        validarAdministrador(solicitante);
        validarPolitica(politica);
        politica.setId(null);
        PoliticaCitacion guardado = repositorio.savePolitica(politica);
        auditarEscritura(solicitante, "politicas-citacion", guardado.getId(), "CREACION");
        return guardado;
    }

    @Override
    public PoliticaCitacion actualizarPolitica(Usuario solicitante, Long id, PoliticaCitacion datos) {
        validarAdministrador(solicitante);
        PoliticaCitacion anterior = obtenerExistente(repositorio.findPoliticaById(id), "la política de citación", id);
        validarPolitica(datos);
        datos.setId(anterior.getId());
        PoliticaCitacion guardado = repositorio.savePolitica(datos);
        auditarEscritura(solicitante, "politicas-citacion", guardado.getId(), "EDICION");
        return guardado;
    }

    @Override
    public List<ParametroPrioridad> listarParametrosPrioridad(Usuario solicitante) {
        validarAdministrador(solicitante);
        auditarLectura(solicitante, "prioridades");
        return repositorio.findAllParametrosPrioridad();
    }

    @Override
    public ParametroPrioridad configurarParametroPrioridad(Usuario solicitante, ParametroPrioridad parametro) {
        validarAdministrador(solicitante);
        validarParametroPrioridad(parametro);
        if (parametro.getId() == null) {
            repositorio.findAllParametrosPrioridad().stream()
                    .filter(actual -> actual.getPrioridad() == parametro.getPrioridad())
                    .findFirst()
                    .ifPresent(actual -> parametro.setId(actual.getId()));
        } else {
            obtenerExistente(repositorio.findParametroPrioridadById(parametro.getId()),
                    "el parámetro de prioridad", parametro.getId());
        }
        ParametroPrioridad guardado = repositorio.saveParametroPrioridad(parametro);
        auditarEscritura(solicitante, "prioridades", guardado.getId(), "CONFIGURACION");
        return guardado;
    }

    @Override
    public List<Prioridad> listarNivelesPrioridad(Usuario solicitante) {
        validarAdministrador(solicitante);
        auditarLectura(solicitante, "prioridades");
        return List.of(Prioridad.values());
    }

    private void validarAdministrador(Usuario solicitante) {
        if (solicitante == null || !solicitante.tieneRol(Rol.ADMINISTRADOR_SISTEMA)) {
            throw new SecurityException("Solo el administrador del sistema puede configurar parámetros maestros");
        }
    }

    private void validarTipoPrueba(TipoPrueba tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de prueba no puede ser nulo");
        }
        if (esVacio(tipo.getNombre())) {
            throw new IllegalArgumentException("El nombre del tipo de prueba es obligatorio");
        }
        if (tipo.getModalidad() == null) {
            throw new IllegalArgumentException("La modalidad es obligatoria");
        }
        if (tipo.getDuracionEstimadaMinutos() == null || tipo.getDuracionEstimadaMinutos() <= 0) {
            throw new IllegalArgumentException("La duración estimada debe ser mayor que cero");
        }
        validarAtributosCatalogo(tipo);
    }

    private void validarAtributosCatalogo(TipoPrueba tipo) {
        if (tipo.getPreparacionPrevia() == null || tipo.getUsoContraste() == null
                || tipo.getRequiereConsentimientoInformado() == null) {
            throw new IllegalArgumentException(
                    "Deben definirse la preparación previa, el uso de contraste y el consentimiento informado");
        }
        if (tipo.getPrioridad() == null || tipo.getPermiteSeguimientoPeriodico() == null) {
            throw new IllegalArgumentException("Deben definirse la prioridad y el seguimiento periódico");
        }
        if (Boolean.TRUE.equals(tipo.getUsoContraste()) && esVacio(tipo.getRequisitosSeguridad())) {
            throw new IllegalArgumentException("Un tipo de prueba con contraste debe definir los requisitos de seguridad");
        }
        if (Boolean.TRUE.equals(tipo.getRequiereConsentimientoInformado())
                && esVacio(tipo.getPreparacionPrevia())) {
            throw new IllegalArgumentException(
                    "Un tipo de prueba con consentimiento informado debe definir la preparación previa");
        }
    }

    private void validarProtocolo(Protocolo protocolo) {
        if (protocolo == null) {
            throw new IllegalArgumentException("El protocolo no puede ser nulo");
        }
        if (esVacio(protocolo.getNombre()) || esVacio(protocolo.getDescripcion())
                || esVacio(protocolo.getPasos())) {
            throw new IllegalArgumentException("Nombre, descripción y pasos del protocolo son obligatorios");
        }
        if (protocolo.getTipoPruebaId() == null) {
            throw new IllegalArgumentException("El protocolo debe asociarse a un tipo de prueba");
        }
        if (repositorio.findTipoPruebaById(protocolo.getTipoPruebaId()).isEmpty()) {
            throw new NoSuchElementException(
                    "No se ha encontrado el tipo de prueba con id " + protocolo.getTipoPruebaId());
        }
        if (protocolo.getActivo() == null) {
            throw new IllegalArgumentException("Debe indicarse si el protocolo está activo");
        }
    }

    private void validarHorario(HorarioAtencion horario) {
        if (horario == null) {
            throw new IllegalArgumentException("El horario no puede ser nulo");
        }
        if (horario.getDiaSemana() == null || horario.getHoraInicio() == null || horario.getHoraFin() == null) {
            throw new IllegalArgumentException("Día, hora de inicio y hora de fin son obligatorios");
        }
        if (!horario.getHoraInicio().isBefore(horario.getHoraFin())) {
            throw new IllegalArgumentException("La hora de inicio debe ser anterior a la hora de fin");
        }
        if (horario.getDuracionSlotMinutos() == null || horario.getDuracionSlotMinutos() <= 0) {
            throw new IllegalArgumentException("La duración del hueco debe ser mayor que cero");
        }
        if (horario.getActivo() == null) {
            throw new IllegalArgumentException("Debe indicarse si el horario está activo");
        }
    }

    private void validarSolape(HorarioAtencion horario) {
        for (HorarioAtencion otro : repositorio.findAllHorarios()) {
            if (Boolean.TRUE.equals(otro.getActivo()) && otro.getDiaSemana() == horario.getDiaSemana()
                    && !mismoId(horario, otro)
                    && horario.getHoraInicio().isBefore(otro.getHoraFin())
                    && otro.getHoraInicio().isBefore(horario.getHoraFin())) {
                throw new IllegalArgumentException("La franja se solapa con otro horario activo del mismo día");
            }
        }
    }

    private boolean mismoId(HorarioAtencion uno, HorarioAtencion otro) {
        return uno.getId() != null && uno.getId().equals(otro.getId());
    }

    private void validarPolitica(PoliticaCitacion politica) {
        if (politica == null) {
            throw new IllegalArgumentException("La política de citación no puede ser nula");
        }
        if (esVacio(politica.getNombre())) {
            throw new IllegalArgumentException("El nombre de la política de citación es obligatorio");
        }
        if (politica.getDiasAntelacionMinima() == null || politica.getDiasAntelacionMinima() < 0) {
            throw new IllegalArgumentException("La antelación mínima no puede ser negativa");
        }
        if (politica.getDiasAntelacionMaxima() == null
                || politica.getDiasAntelacionMaxima() < politica.getDiasAntelacionMinima()) {
            throw new IllegalArgumentException("La antelación máxima debe ser mayor o igual que la mínima");
        }
        if (politica.getMaximoCitasPorFranja() == null || politica.getMaximoCitasPorFranja() <= 0) {
            throw new IllegalArgumentException("El máximo de citas por franja debe ser mayor que cero");
        }
        if (politica.getActiva() == null) {
            throw new IllegalArgumentException("Debe indicarse si la política está activa");
        }
    }

    private void validarParametroPrioridad(ParametroPrioridad parametro) {
        if (parametro == null) {
            throw new IllegalArgumentException("El parámetro de prioridad no puede ser nulo");
        }
        if (parametro.getPrioridad() == null) {
            throw new IllegalArgumentException("La prioridad es obligatoria");
        }
        if (parametro.getDiasMaximosEspera() == null || parametro.getDiasMaximosEspera() < 0) {
            throw new IllegalArgumentException("Los días máximos de espera no pueden ser negativos");
        }
        if (parametro.getActivo() == null) {
            throw new IllegalArgumentException("Debe indicarse si el nivel de prioridad está activo");
        }
    }

    private <T> T obtenerExistente(Optional<T> entidad, String nombre, Long id) {
        return entidad.orElseThrow(() ->
                new NoSuchElementException("No se ha encontrado " + nombre + " con id " + id));
    }

    private void auditarLectura(Usuario solicitante, String subrecurso) {
        auditoria.registrarAcceso(solicitante.getId(), RECURSO + "/" + subrecurso,
                "LECTURA", Instant.now());
    }

    private void auditarEscritura(Usuario solicitante, String subrecurso, Long id, String accion) {
        Instant ahora = Instant.now();
        auditoria.registrarAcceso(solicitante.getId(), RECURSO + "/" + subrecurso + "/" + id,
                accion, ahora);
        auditoria.registrarEvento("CU17_CONFIGURACION",
                accion + " de " + subrecurso + " por " + solicitante.getNombreUsuario(), ahora);
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}
