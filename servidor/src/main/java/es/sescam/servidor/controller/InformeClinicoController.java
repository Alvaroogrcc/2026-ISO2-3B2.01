package es.sescam.servidor.controller;

import java.util.List;
import java.util.Optional;

import es.sescam.servidor.error.AccesoDenegado;
import es.sescam.servidor.error.EntidadNoEncontrada;
import es.sescam.servidor.error.ValidacionIncorrecta;
import es.sescam.servidor.model.ImagenMedica;
import es.sescam.servidor.model.InformeClinico;
import es.sescam.servidor.seguridad.Usuario;

/**
 * Controlador de entrada para la gestión de informes clínicos e imágenes médicas
 * (CU-09).
 * <p>
 * No contiene lógica de negocio: recibe la petición, delega en el servicio y
 * devuelve el resultado. El usuario que solicita la operación viaja en cada
 * llamada para que el servicio aplique el control de acceso por rol y la
 * auditoría.
 * </p>
 */
public interface InformeClinicoController {

    /**
     * Crea un informe clínico.
     *
     * @param usuario usuario que crea el informe
     * @param informe datos del informe
     * @return informe registrado
     * @throws AccesoDenegado si el usuario no tiene el rol requerido o no mantiene
     *         relación clínica con el paciente
     * @throws ValidacionIncorrecta si los datos no son válidos
     * @throws EntidadNoEncontrada si la solicitud no existe
     */
    InformeClinico crear(Usuario usuario, InformeClinico informe);

    /**
     * Modifica un informe clínico existente.
     *
     * @param usuario usuario que modifica el informe
     * @param id identificador del informe
     * @param datos nuevos datos del informe
     * @return informe actualizado
     * @throws AccesoDenegado si el usuario no tiene el rol requerido o no mantiene
     *         relación clínica con el paciente
     * @throws ValidacionIncorrecta si el informe está firmado o los datos no son
     *         válidos
     * @throws EntidadNoEncontrada si el informe no existe
     */
    InformeClinico actualizar(Usuario usuario, Long id, InformeClinico datos);

    /**
     * Firma un informe clínico.
     *
     * @param usuario usuario que firma el informe
     * @param id identificador del informe
     * @return informe firmado
     * @throws AccesoDenegado si el usuario no tiene el rol requerido o no mantiene
     *         relación clínica con el paciente
     * @throws ValidacionIncorrecta si el informe ya está firmado
     * @throws EntidadNoEncontrada si el informe no existe
     */
    InformeClinico firmar(Usuario usuario, Long id);

    /**
     * Obtiene un informe por su identificador.
     *
     * @param usuario usuario que solicita la consulta
     * @param id identificador del informe
     * @return informe encontrado o {@link Optional#empty()} si no existe
     * @throws AccesoDenegado si el usuario no tiene un rol autorizado o no mantiene
     *         relación clínica con el paciente
     * @throws ValidacionIncorrecta si el identificador es nulo
     */
    Optional<InformeClinico> obtenerPorId(Usuario usuario, Long id);

    /**
     * Lista los informes clínicos de un paciente.
     *
     * @param usuario usuario que solicita el listado
     * @param pacienteId identificador del paciente
     * @return informes del paciente
     * @throws AccesoDenegado si el usuario no tiene un rol autorizado o no mantiene
     *         relación clínica con el paciente
     * @throws ValidacionIncorrecta si el identificador del paciente es nulo
     */
    List<InformeClinico> listarPorPaciente(Usuario usuario, Long pacienteId);

    /**
     * Lista las imágenes médicas de una solicitud de prueba.
     *
     * @param usuario usuario que solicita las imágenes
     * @param solicitudId identificador de la solicitud de prueba
     * @return imágenes médicas asociadas al estudio
     * @throws AccesoDenegado si el usuario no tiene el rol requerido o no mantiene
     *         relación clínica con el paciente
     * @throws ValidacionIncorrecta si el identificador de la solicitud es nulo
     * @throws EntidadNoEncontrada si la solicitud no existe
     */
    List<ImagenMedica> listarImagenes(Usuario usuario, Long solicitudId);
}
