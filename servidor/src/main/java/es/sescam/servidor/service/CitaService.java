package es.sescam.servidor.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import es.sescam.servidor.error.AccesoDenegado;
import es.sescam.servidor.error.EntidadNoEncontrada;
import es.sescam.servidor.error.ValidacionIncorrecta;
import es.sescam.servidor.model.Cita;
import es.sescam.servidor.seguridad.Usuario;

/**
 * Contrato de negocio de citas (CU-01 y CU-13). Las operaciones de gestión requieren
 * un usuario con rol administrativo y dejan constancia en la auditoría.
 */
public interface CitaService {

    Cita crear(Cita cita);

    Optional<Cita> obtenerPorId(Long id);

    List<Cita> listar();

    List<Cita> listarPorPaciente(Long pacienteId);

    /**
     * Asigna fecha y hora a una cita pendiente y la deja confirmada.
     *
     * @param citaId identificador de la cita pendiente
     * @param fecha nueva fecha de la cita
     * @param hora nueva hora de la cita
     * @param usuario usuario administrativo que realiza la operación
     * @return la cita confirmada
     * @throws ValidacionIncorrecta si faltan datos obligatorios
     * @throws EntidadNoEncontrada si la cita no existe
     * @throws ValidacionIncorrecta si la cita no está pendiente o solapa con otra cita confirmada
     * @throws AccesoDenegado si el usuario no tiene rol administrativo
     */
    Cita asignarCita(Long citaId, LocalDate fecha, LocalTime hora, Usuario usuario);

    /**
     * Reprograma una cita confirmada, rechazando solapes con otras citas confirmadas.
     *
     * @param citaId identificador de la cita
     * @param fecha nueva fecha
     * @param hora nueva hora
     * @param usuario usuario administrativo que realiza la operación
     * @return la cita reprogramada
     * @throws ValidacionIncorrecta si faltan datos obligatorios
     * @throws EntidadNoEncontrada si la cita no existe
     * @throws ValidacionIncorrecta si la cita no está confirmada o solapa con otra cita confirmada
     * @throws AccesoDenegado si el usuario no tiene rol administrativo
     */
    Cita reprogramarCita(Long citaId, LocalDate fecha, LocalTime hora, Usuario usuario);

    /**
     * Cancela una cita y libera su hueco en la agenda del doctor.
     *
     * @param citaId identificador de la cita
     * @param usuario usuario administrativo que realiza la operación
     * @return la cita cancelada
     * @throws EntidadNoEncontrada si la cita no existe
     * @throws ValidacionIncorrecta si la cita no está pendiente ni confirmada
     * @throws AccesoDenegado si el usuario no tiene rol administrativo
     */
    Cita cancelarCita(Long citaId, Usuario usuario);
}
