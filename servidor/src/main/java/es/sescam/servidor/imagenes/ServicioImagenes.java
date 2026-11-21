package es.sescam.servidor.imagenes;

import java.util.List;

import es.sescam.servidor.model.ImagenMedica;

/**
 * Contrato de acceso a las imágenes médicas de un estudio (CU-09).
 * <p>
 * Abstrae la integración con el RIS/PACS. El servicio de informes solo solicita
 * los metadatos de las imágenes; la descarga y decodificación DICOM corresponde
 * al visor del cliente.
 * </p>
 * <p>
 * En esta iteración no se descargan binarios ni se añaden dependencias DICOM: las
 * implementaciones disponibles devuelven metadatos simulados.
 * </p>
 */
public interface ServicioImagenes {

    /**
     * Lista las imágenes médicas asociadas a un estudio.
     *
     * @param estudioId identificador del estudio; no puede estar vacío
     * @return imágenes del estudio, o una lista vacía si no hay ninguna
     */
    List<ImagenMedica> listarImagenes(String estudioId);
}
