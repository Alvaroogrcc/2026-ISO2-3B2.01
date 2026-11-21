package es.sescam.servidor.controller;

import java.util.List;
import java.util.Optional;

import es.sescam.servidor.error.AccesoDenegado;
import es.sescam.servidor.error.EntidadNoEncontrada;
import es.sescam.servidor.error.ValidacionIncorrecta;
import es.sescam.servidor.model.SolicitudPrueba;
import es.sescam.servidor.model.TipoPrueba;
import es.sescam.servidor.seguridad.Usuario;

/**
 * Controlador de entrada para la prescripción de pruebas diagnósticas (CU-06).
 * <p>
 * No contiene lógica de negocio: recibe la petición, delega en el servicio y
 * devuelve el resultado. El usuario que solicita la operación viaja en cada
 * llamada para que el servicio aplique el control de acceso por rol.
 * </p>
 */
public interface SolicitudPruebaController {

    /**
     * Prescribe una prueba diagnóstica.
     *
     * @param usuario usuario que prescribe la prueba
     * @param solicitud datos de la solicitud
     * @return solicitud registrada
     * @throws AccesoDenegado si el usuario no tiene el rol requerido
     * @throws ValidacionIncorrecta si los datos no son válidos
     * @throws EntidadNoEncontrada si el paciente, el médico o el tipo de prueba no
     *         existen
     */
    SolicitudPrueba prescribir(Usuario usuario, SolicitudPrueba solicitud);

    /**
     * Lista las solicitudes pendientes de citación.
     *
     * @param usuario usuario que solicita el listado
     * @return solicitudes pendientes
     * @throws AccesoDenegado si el usuario no tiene un rol autorizado
     */
    List<SolicitudPrueba> listarPendientes(Usuario usuario);

    /**
     * Obtiene una solicitud por su identificador.
     *
     * @param usuario usuario que solicita la consulta
     * @param id identificador de la solicitud
     * @return solicitud encontrada o {@link Optional#empty()} si no existe
     * @throws AccesoDenegado si el usuario no tiene un rol autorizado
     * @throws ValidacionIncorrecta si el identificador es nulo
     */
    Optional<SolicitudPrueba> obtenerPorId(Usuario usuario, Long id);

    /**
     * Lista los tipos de prueba disponibles en el catálogo.
     *
     * @param usuario usuario que solicita el catálogo
     * @return tipos de prueba disponibles
     * @throws AccesoDenegado si el usuario no tiene un rol autorizado
     */
    List<TipoPrueba> listarTiposPrueba(Usuario usuario);
}
