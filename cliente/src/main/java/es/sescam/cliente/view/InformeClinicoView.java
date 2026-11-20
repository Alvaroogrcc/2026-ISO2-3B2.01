package es.sescam.cliente.view;

import java.util.List;

import es.sescam.cliente.model.ImagenMedicaDTO;
import es.sescam.cliente.model.InformeClinicoDTO;

/**
 * Contrato de vista para la gestión de informes clínicos e imágenes médicas
 * (CU-09).
 * <p>
 * La vista solo muestra datos y recoge entradas; no aplica reglas de negocio ni
 * accede a repositorios o servicios del servidor.
 * </p>
 */
public interface InformeClinicoView {

    /**
     * Muestra el listado de informes clínicos de un paciente.
     *
     * @param informes informes a mostrar
     */
    void mostrarInformes(List<InformeClinicoDTO> informes);

    /**
     * Muestra un informe clínico concreto.
     *
     * @param informe informe a mostrar
     */
    void mostrarInforme(InformeClinicoDTO informe);

    /**
     * Muestra las imágenes médicas de un estudio.
     *
     * @param imagenes imágenes a mostrar
     */
    void mostrarImagenes(List<ImagenMedicaDTO> imagenes);

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
