package es.sescam.cliente.view;

import java.util.List;

import es.sescam.cliente.model.PacienteDTO;

/**
 * Implementación vacía de la vista de pacientes.
 * <p>
 * Sirve como implementación de referencia para los clientes que todavía no
 * disponen de interfaz gráfica. La vista de escritorio se implementa en
 * {@link PacienteViewSwing}.
 * </p>
 */
public class PacienteViewImpl implements PacienteView {

    /**
     * Crea una vista de pacientes sin comportamiento.
     */
    public PacienteViewImpl() {
    }

    @Override
    public void mostrarPaciente(PacienteDTO paciente) {
        // Implementación de vista pendiente (escritorio/móvil/web)
    }

    @Override
    public void mostrarPacientes(List<PacienteDTO> pacientes) {
        // Implementación de vista pendiente
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        // Implementación de vista pendiente
    }

    @Override
    public void mostrarConfirmacion(String mensaje) {
        // Implementación de vista pendiente
    }

    @Override
    public void mostrarError(String mensaje) {
        // Implementación de vista pendiente
    }
}
