package es.sescam.cliente.view;

import java.util.List;

import es.sescam.cliente.model.SolicitudPruebaDTO;
import es.sescam.cliente.model.TipoPruebaDTO;

/**
 * Contrato de vista para la prescripción de pruebas diagnósticas (CU-06).
 * <p>
 * La vista solo muestra datos y recoge entradas; no aplica reglas de negocio ni
 * accede a repositorios o servicios del servidor.
 * </p>
 */
public interface SolicitudPruebaView {

    /**
     * Muestra los tipos de prueba disponibles en el catálogo.
     *
     * @param tiposPrueba tipos de prueba a mostrar
     */
    void mostrarTiposPrueba(List<TipoPruebaDTO> tiposPrueba);

    /**
     * Muestra las solicitudes pendientes de citación.
     *
     * @param solicitudes solicitudes a mostrar
     */
    void mostrarSolicitudesPendientes(List<SolicitudPruebaDTO> solicitudes);

    /**
     * Muestra un mensaje informativo.
     *
     * @param mensaje texto a mostrar
     */
    void mostrarMensaje(String mensaje);

    /**
     * Muestra una confirmación de operación correcta.
     *
     * @param mensaje texto de confirmación
     */
    void mostrarConfirmacion(String mensaje);

    /**
     * Muestra un error de operación.
     *
     * @param mensaje texto del error
     */
    void mostrarError(String mensaje);
}
