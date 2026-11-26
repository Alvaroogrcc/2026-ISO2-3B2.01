package es.sescam.cliente.controller;

import java.util.List;

import es.sescam.cliente.model.ImagenMedicaDTO;
import es.sescam.cliente.model.InformeClinicoDTO;
import es.sescam.cliente.service.auth.Solicitante;

/**
 * Controlador de presentación para la gestión de informes clínicos e imágenes
 * médicas (CU-09).
 * <p>
 * Traduce las acciones de la vista en llamadas al cliente de la API y comunica
 * los resultados a la vista. No contiene reglas de negocio ni accede al modelo
 * del servidor.
 * </p>
 */
public interface InformeClinicoController {

    /**
     * Crea un informe clínico y muestra el resultado en la vista.
     *
     * @param solicitante usuario que realiza la petición
     * @param informe datos del informe
     */
    void crear(Solicitante solicitante, InformeClinicoDTO informe);

    /**
     * Modifica un informe clínico existente y muestra el resultado en la vista.
     *
     * @param solicitante usuario que realiza la petición
     * @param id identificador del informe
     * @param informe nuevos datos del informe
     */
    void guardar(Solicitante solicitante, Long id, InformeClinicoDTO informe);

    /**
     * Firma un informe clínico existente.
     *
     * @param solicitante usuario que realiza la petición
     * @param id identificador del informe
     */
    void firmar(Solicitante solicitante, Long id);

    /**
     * Consulta un informe clínico y lo muestra en la vista.
     *
     * @param solicitante usuario que realiza la petición
     * @param id identificador del informe
     */
    void consultar(Solicitante solicitante, Long id);

    /**
     * Lista los informes de un paciente y los muestra en la vista.
     *
     * @param solicitante usuario que realiza la petición
     * @param pacienteId identificador del paciente
     */
    void listarPorPaciente(Solicitante solicitante, Long pacienteId);

    /**
     * Carga las imágenes médicas de una solicitud y las muestra en la vista.
     * <p>
     * Devuelve las imágenes cargadas para que la vista pueda completar la carga no
     * bloqueante iniciada en un {@code SwingWorker}. Si la operación falla, la vista
     * recibe el error y se devuelve una lista vacía.
     * </p>
     *
     * @param solicitante usuario que realiza la petición
     * @param solicitudId identificador de la solicitud de prueba
     * @return imágenes cargadas, o una lista vacía si la operación falla
     */
    List<ImagenMedicaDTO> cargarImagenes(Solicitante solicitante, Long solicitudId);
}
