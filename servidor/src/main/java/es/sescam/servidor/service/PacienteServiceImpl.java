package es.sescam.servidor.service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import es.sescam.servidor.admision.ServicioAdmision;
import es.sescam.servidor.auditoria.Auditoria;
import es.sescam.servidor.error.AccesoDenegado;
import es.sescam.servidor.error.EntidadNoEncontrada;
import es.sescam.servidor.error.ValidacionIncorrecta;
import es.sescam.servidor.model.Paciente;
import es.sescam.servidor.repository.PacienteRepository;
import es.sescam.servidor.seguridad.Rol;
import es.sescam.servidor.seguridad.Usuario;

/**
 * Implementación del servicio de gestión de pacientes (CU-12).
 * <p>
 * Aplica el control de acceso por rol, valida los datos demográficos, evita
 * duplicados por DNI y número de historia clínica, audita cada acceso y
 * sincroniza altas, modificaciones y bajas con el sistema de admisión.
 * </p>
 */
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final Auditoria auditoria;
    private final ServicioAdmision servicioAdmision;

    /**
     * Crea el servicio con sus dependencias.
     *
     * @param pacienteRepository repositorio de pacientes
     * @param auditoria servicio de auditoría
     * @param servicioAdmision integración con el sistema de admisión
     */
    public PacienteServiceImpl(PacienteRepository pacienteRepository, Auditoria auditoria,
                               ServicioAdmision servicioAdmision) {
        this.pacienteRepository = Objects.requireNonNull(pacienteRepository, "pacienteRepository");
        this.auditoria = Objects.requireNonNull(auditoria, "auditoria");
        this.servicioAdmision = Objects.requireNonNull(servicioAdmision, "servicioAdmision");
    }

    @Override
    public Paciente crear(Usuario usuario, Paciente paciente) {
        exigirRolEscritura(usuario);
        validarPaciente(paciente);
        comprobarDuplicados(paciente, null);
        Paciente guardado = pacienteRepository.save(paciente);
        auditar(usuario, "paciente/" + guardado.getId(), "ALTA");
        servicioAdmision.sincronizarAlta(guardado);
        return guardado;
    }

    @Override
    public Paciente actualizar(Usuario usuario, Long id, Paciente datos) {
        exigirRolEscritura(usuario);
        Paciente existente = obtenerPacienteExistente(id);
        if (!existente.estaActivo()) {
            throw new ValidacionIncorrecta("No se puede modificar un paciente dado de baja");
        }
        validarPaciente(datos);
        comprobarDuplicados(datos, existente.getId());
        copiarDatos(datos, existente);
        Paciente guardado = pacienteRepository.save(existente);
        auditar(usuario, "paciente/" + guardado.getId(), "MODIFICACION");
        servicioAdmision.sincronizarModificacion(guardado);
        return guardado;
    }

    @Override
    public void darDeBaja(Usuario usuario, Long id) {
        exigirRolEscritura(usuario);
        Paciente existente = obtenerPacienteExistente(id);
        if (!existente.estaActivo()) {
            throw new ValidacionIncorrecta("El paciente ya está dado de baja");
        }
        existente.setFechaBaja(LocalDate.now());
        Paciente guardado = pacienteRepository.save(existente);
        auditar(usuario, "paciente/" + guardado.getId(), "BAJA");
        servicioAdmision.sincronizarBaja(guardado);
    }

    @Override
    public Optional<Paciente> obtenerPorId(Usuario usuario, Long id) {
        exigirRolLectura(usuario);
        if (id == null) {
            throw new ValidacionIncorrecta("El identificador del paciente es obligatorio");
        }
        auditar(usuario, "paciente/" + id, "LECTURA");
        return pacienteRepository.findById(id);
    }

    @Override
    public Optional<Paciente> buscarPorDni(Usuario usuario, String dni) {
        exigirRolLectura(usuario);
        if (esBlanco(dni)) {
            throw new ValidacionIncorrecta("El DNI del paciente es obligatorio");
        }
        String dniNormalizado = dni.trim();
        auditar(usuario, "paciente/dni/" + dniNormalizado, "LECTURA");
        return pacienteRepository.findByDni(dniNormalizado);
    }

    @Override
    public List<Paciente> listar(Usuario usuario) {
        exigirRolLectura(usuario);
        auditar(usuario, "paciente", "LECTURA");
        return pacienteRepository.findAll();
    }

    private void exigirRolEscritura(Usuario usuario) {
        if (usuario == null || !usuario.tieneRol(Rol.ADMINISTRATIVO)) {
            throw new AccesoDenegado(
                    "Solo el personal administrativo puede dar de alta, modificar o dar de baja pacientes");
        }
    }

    private void exigirRolLectura(Usuario usuario) {
        if (usuario == null
                || !(usuario.tieneRol(Rol.MEDICO)
                || usuario.tieneRol(Rol.ADMINISTRATIVO)
                || usuario.tieneRol(Rol.ADMINISTRADOR_SISTEMA))) {
            throw new AccesoDenegado("No tiene permisos para consultar datos de pacientes");
        }
    }

    private void validarPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new ValidacionIncorrecta("Los datos del paciente son obligatorios");
        }
        if (esBlanco(paciente.getNombre())) {
            throw new ValidacionIncorrecta("El nombre del paciente es obligatorio");
        }
        if (esBlanco(paciente.getApellidos())) {
            throw new ValidacionIncorrecta("Los apellidos del paciente son obligatorios");
        }
        if (esBlanco(paciente.getDni())) {
            throw new ValidacionIncorrecta("El DNI del paciente es obligatorio");
        }
        if (paciente.getFechaNacimiento() != null
                && paciente.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new ValidacionIncorrecta("La fecha de nacimiento no puede ser futura");
        }
        normalizar(paciente);
    }

    private void normalizar(Paciente paciente) {
        paciente.setNombre(paciente.getNombre().trim());
        paciente.setApellidos(paciente.getApellidos().trim());
        paciente.setDni(paciente.getDni().trim());
        if (esBlanco(paciente.getNumeroHistoriaClinica())) {
            paciente.setNumeroHistoriaClinica(null);
        } else {
            paciente.setNumeroHistoriaClinica(paciente.getNumeroHistoriaClinica().trim());
        }
    }

    private void comprobarDuplicados(Paciente paciente, Long idExcluido) {
        pacienteRepository.findByDni(paciente.getDni())
                .filter(existente -> !Objects.equals(existente.getId(), idExcluido))
                .ifPresent(existente -> {
                    throw new ValidacionIncorrecta("Ya existe un paciente con el DNI indicado");
                });
        String historiaClinica = paciente.getNumeroHistoriaClinica();
        if (historiaClinica != null) {
            pacienteRepository.findByNumeroHistoriaClinica(historiaClinica)
                    .filter(existente -> !Objects.equals(existente.getId(), idExcluido))
                    .ifPresent(existente -> {
                        throw new ValidacionIncorrecta(
                                "Ya existe un paciente con el número de historia clínica indicado");
                    });
        }
    }

    private Paciente obtenerPacienteExistente(Long id) {
        if (id == null) {
            throw new EntidadNoEncontrada("No se ha encontrado el paciente indicado");
        }
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontrada(
                        "No se ha encontrado el paciente con identificador " + id));
    }

    private void copiarDatos(Paciente datos, Paciente destino) {
        destino.setNombre(datos.getNombre());
        destino.setApellidos(datos.getApellidos());
        destino.setDni(datos.getDni());
        destino.setFechaNacimiento(datos.getFechaNacimiento());
        destino.setTelefono(datos.getTelefono());
        destino.setEmail(datos.getEmail());
        destino.setDireccion(datos.getDireccion());
        destino.setNumeroHistoriaClinica(datos.getNumeroHistoriaClinica());
    }

    private void auditar(Usuario usuario, String recurso, String accion) {
        auditoria.registrarAcceso(usuario.getId(), recurso, accion, Instant.now());
    }

    private boolean esBlanco(String valor) {
        return valor == null || valor.isBlank();
    }
}
