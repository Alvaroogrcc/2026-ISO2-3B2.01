package es.sescam.servidor.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import es.sescam.servidor.model.Paciente;
import es.sescam.servidor.seguridad.Usuario;
import es.sescam.servidor.service.PacienteService;

/**
 * Implementación del controlador de pacientes.
 * <p>
 * Todas las operaciones se limitan a delegar en {@link PacienteService}; las
 * reglas de negocio, el control de acceso y la auditoría viven en el servicio.
 * </p>
 */
public class PacienteControllerImpl implements PacienteController {

    private final PacienteService pacienteService;

    /**
     * Crea el controlador con el servicio de pacientes.
     *
     * @param pacienteService servicio de pacientes
     */
    public PacienteControllerImpl(PacienteService pacienteService) {
        this.pacienteService = Objects.requireNonNull(pacienteService, "pacienteService");
    }

    @Override
    public Paciente crearPaciente(Usuario usuario, Paciente paciente) {
        return pacienteService.crear(usuario, paciente);
    }

    @Override
    public Optional<Paciente> obtenerPaciente(Usuario usuario, Long id) {
        return pacienteService.obtenerPorId(usuario, id);
    }

    @Override
    public List<Paciente> listarPacientes(Usuario usuario) {
        return pacienteService.listar(usuario);
    }

    @Override
    public Paciente actualizarPaciente(Usuario usuario, Long id, Paciente datos) {
        return pacienteService.actualizar(usuario, id, datos);
    }

    @Override
    public void darDeBajaPaciente(Usuario usuario, Long id) {
        pacienteService.darDeBaja(usuario, id);
    }

    @Override
    public Optional<Paciente> buscarPacientePorDni(Usuario usuario, String dni) {
        return pacienteService.buscarPorDni(usuario, dni);
    }
}
