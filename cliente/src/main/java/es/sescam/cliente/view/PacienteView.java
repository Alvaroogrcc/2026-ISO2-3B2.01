package es.sescam.cliente.view;

import java.util.List;

import es.sescam.cliente.model.PacienteDTO;

/**
 * Contrato de vista para la gestión de pacientes (CU-12).
 * <p>
 * La vista solo muestra datos y recoge entradas; no aplica reglas de negocio ni
 * accede a repositorios o servicios del servidor.
 * </p>
 */
public interface PacienteView {

    /**
     * Muestra un paciente.
     *
     * @param paciente paciente a mostrar
     */
    void mostrarPaciente(PacienteDTO paciente);

    /**
     * Muestra una lista de pacientes.
     *
     * @param pacientes pacientes a mostrar
     */
    void mostrarPacientes(List<PacienteDTO> pacientes);

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
