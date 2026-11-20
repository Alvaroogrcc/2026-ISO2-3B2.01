package es.sescam.cliente.view;

import java.util.List;

import es.sescam.cliente.model.ImagenMedicaDTO;
import es.sescam.cliente.model.InformeClinicoDTO;

/**
 * Implementación sin interfaz gráfica de la vista de informes clínicos.
 * <p>
 * Sirve como implementación de referencia para los clientes que todavía no
 * disponen de interfaz gráfica. La vista de escritorio se implementa en
 * {@link InformeClinicoViewSwing}.
 * </p>
 */
public class InformeClinicoViewImpl implements InformeClinicoView {

    /**
     * Crea una vista de informes clínicos sin comportamiento gráfico.
     */
    public InformeClinicoViewImpl() {
    }

    @Override
    public void mostrarInformes(List<InformeClinicoDTO> informes) {
        // Sin representación gráfica en esta implementación.
    }

    @Override
    public void mostrarInforme(InformeClinicoDTO informe) {
        // Sin representación gráfica en esta implementación.
    }

    @Override
    public void mostrarImagenes(List<ImagenMedicaDTO> imagenes) {
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
