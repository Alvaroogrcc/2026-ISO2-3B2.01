package es.sescam.servidor.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import es.sescam.servidor.model.Cita;
import es.sescam.servidor.seguridad.Usuario;
import es.sescam.servidor.service.CitaService;

public class CitaControllerImpl implements CitaController {

    private final CitaService citaService;

    public CitaControllerImpl(CitaService citaService) {
        this.citaService = Objects.requireNonNull(citaService, "citaService");
    }

    @Override
    public Cita crearCita(Cita cita) {
        return citaService.crear(cita);
    }

    @Override
    public Optional<Cita> obtenerCita(Long id) {
        return citaService.obtenerPorId(id);
    }

    @Override
    public List<Cita> listarCitas() {
        return citaService.listar();
    }

    @Override
    public List<Cita> listarCitasPorPaciente(Long pacienteId) {
        return citaService.listarPorPaciente(pacienteId);
    }

    @Override
    public Cita asignarCita(Long citaId, LocalDate fecha, LocalTime hora, Usuario usuario) {
        return citaService.asignarCita(citaId, fecha, hora, usuario);
    }

    @Override
    public Cita reprogramarCita(Long citaId, LocalDate fecha, LocalTime hora, Usuario usuario) {
        return citaService.reprogramarCita(citaId, fecha, hora, usuario);
    }

    @Override
    public Cita cancelarCita(Long citaId, Usuario usuario) {
        return citaService.cancelarCita(citaId, usuario);
    }
}
