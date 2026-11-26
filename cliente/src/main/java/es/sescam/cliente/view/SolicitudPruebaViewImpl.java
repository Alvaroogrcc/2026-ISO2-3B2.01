package es.sescam.cliente.view;

import java.util.List;

import es.sescam.cliente.model.SolicitudPruebaDTO;
import es.sescam.cliente.model.TipoPruebaDTO;

/**
 * Implementación sin interfaz gráfica de la vista de solicitudes de prueba.
 * <p>
 * Sirve como implementación de referencia para los clientes que todavía no
 * disponen de interfaz gráfica. La vista de escritorio se implementa en
 * {@link SolicitudPruebaViewSwing}.
 * </p>
 */
public class SolicitudPruebaViewImpl implements SolicitudPruebaView {

    /**
     * Crea una vista de solicitudes de prueba sin comportamiento gráfico.
     */
    public SolicitudPruebaViewImpl() {
    }

    @Override
    public void mostrarTiposPrueba(List<TipoPruebaDTO> tiposPrueba) {
        // Sin representación gráfica en esta implementación.
    }

    @Override
    public void mostrarSolicitudesPendientes(List<SolicitudPruebaDTO> solicitudes) {
        // Sin representación gráfica en esta implementación.
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        // Sin representación gráfica en esta implementación.
    }

    @Override
    public void mostrarConfirmacion(String mensaje) {
        // Sin representación gráfica en esta implementación.
    }

    @Override
    public void mostrarError(String mensaje) {
        // Sin representación gráfica en esta implementación.
    }
}
