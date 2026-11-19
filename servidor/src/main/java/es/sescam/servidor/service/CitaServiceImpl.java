package es.sescam.servidor.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import es.sescam.servidor.auditoria.Auditoria;
import es.sescam.servidor.error.AccesoDenegado;
import es.sescam.servidor.error.EntidadNoEncontrada;
import es.sescam.servidor.error.ValidacionIncorrecta;
import es.sescam.servidor.model.Cita;
import es.sescam.servidor.repository.CitaRepository;
import es.sescam.servidor.seguridad.Rol;
import es.sescam.servidor.seguridad.Usuario;

/**
 * Implementación de las reglas de negocio de citas. Los estados se gestionan como
 * cadenas para no alterar el modelo de datos existente (D-003).
 */
public class CitaServiceImpl implements CitaService {

    private static final String PENDIENTE = "PENDIENTE";
    private static final String CONFIRMADA = "CONFIRMADA";
    private static final String CANCELADA = "CANCELADA";

    private final CitaRepository citaRepository;
    private final Auditoria auditoria;

    public CitaServiceImpl(CitaRepository citaRepository, Auditoria auditoria) {
        this.citaRepository = Objects.requireNonNull(citaRepository, "citaRepository");
        this.auditoria = Objects.requireNonNull(auditoria, "auditoria");
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

    @Override
    public List<Cita> listarPorPaciente(Long pacienteId) {
        if (pacienteId == null) {
            throw new ValidacionIncorrecta("El id del paciente es obligatorio");
        }
        return citaRepository.findByPacienteId(pacienteId);
    }

    @Override
    public Cita asignarCita(Long citaId, LocalDate fecha, LocalTime hora, Usuario usuario) {
        exigirRolAdministrativo(usuario);
        Cita cita = obtenerCitaExistente(citaId);
        validarFechaHora(fecha, hora);
        if (!PENDIENTE.equals(cita.getEstado())) {
            throw new ValidacionIncorrecta("Solo se puede asignar una cita pendiente");
        }
        return moverCita(cita, fecha, hora, CONFIRMADA, usuario, "ASIGNAR");
    }

    @Override
    public Cita reprogramarCita(Long citaId, LocalDate fecha, LocalTime hora, Usuario usuario) {
        exigirRolAdministrativo(usuario);
        Cita cita = obtenerCitaExistente(citaId);
        validarFechaHora(fecha, hora);
        if (!CONFIRMADA.equals(cita.getEstado())) {
            throw new ValidacionIncorrecta("Solo se puede reprogramar una cita confirmada");
        }
        return moverCita(cita, fecha, hora, CONFIRMADA, usuario, "REPROGRAMAR");
    }

    @Override
    public Cita cancelarCita(Long citaId, Usuario usuario) {
        exigirRolAdministrativo(usuario);
        Cita cita = obtenerCitaExistente(citaId);
        if (!PENDIENTE.equals(cita.getEstado()) && !CONFIRMADA.equals(cita.getEstado())) {
            throw new ValidacionIncorrecta("La cita no está pendiente ni confirmada");
        }
        cita.setEstado(CANCELADA);
        Cita guardada = citaRepository.save(cita);
        auditar(guardada, usuario, "CANCELAR");
        return guardada;
    }

    private Cita moverCita(Cita cita, LocalDate fecha, LocalTime hora, String estado,
            Usuario usuario, String accion) {
        comprobarSolape(cita, fecha, hora);
        cita.setFecha(fecha);
        cita.setHora(hora);
        cita.setEstado(estado);
        Cita guardada = citaRepository.save(cita);
        auditar(guardada, usuario, accion);
        return guardada;
    }

    private void exigirRolAdministrativo(Usuario usuario) {
        if (usuario == null || !usuario.tieneRol(Rol.ADMINISTRATIVO)) {
            throw new AccesoDenegado("Solo el personal administrativo puede gestionar citas");
        }
    }

    private Cita obtenerCitaExistente(Long citaId) {
        if (citaId == null) {
            throw new ValidacionIncorrecta("El identificador de la cita es obligatorio");
        }
        return citaRepository.findById(citaId)
                .orElseThrow(() -> new EntidadNoEncontrada("No se ha encontrado la cita indicada"));
    }

    private void validarFechaHora(LocalDate fecha, LocalTime hora) {
        if (fecha == null || hora == null) {
            throw new ValidacionIncorrecta("La fecha y la hora son obligatorias");
        }
    }

    private void comprobarSolape(Cita cita, LocalDate fecha, LocalTime hora) {
        if (cita.getDoctor() == null || cita.getDoctor().getId() == null) {
            return;
        }
        Long doctorId = cita.getDoctor().getId();
        for (Cita otra : citaRepository.findByDoctorIdAndFecha(doctorId, fecha)) {
            if (!Objects.equals(otra.getId(), cita.getId())
                    && CONFIRMADA.equals(otra.getEstado())
                    && hora.equals(otra.getHora())) {
                throw new ValidacionIncorrecta("La hora ya está ocupada en la agenda del doctor");
            }
        }
    }

    private void auditar(Cita cita, Usuario usuario, String accion) {
        auditoria.registrarAcceso(usuario.getId(), "cita/" + cita.getId(), accion, Instant.now());
    }
}
