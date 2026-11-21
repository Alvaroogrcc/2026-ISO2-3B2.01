package es.sescam.servidor.imagenes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.sescam.servidor.model.ImagenMedica;

/**
 * Implementación stub del servicio de imágenes para la Iteración 1 (CU-09).
 * <p>
 * No realiza llamadas de red ni descarga binarios DICOM. Devuelve tres imágenes
 * simuladas (una por serie) con sus metadatos y una URL {@code dicom://} para
 * cualquier identificador de estudio no vacío. La integración real con el
 * RIS/PACS sustituirá este stub.
 * </p>
 */
public class ServicioImagenesStub implements ServicioImagenes {

    /**
     * Crea el stub del servicio de imágenes.
     */
    public ServicioImagenesStub() {
    }

    @Override
    public List<ImagenMedica> listarImagenes(String estudioId) {
        if (estudioId == null || estudioId.isBlank()) {
            return Collections.emptyList();
        }
        String estudio = estudioId.trim();
        Instant adquisicion = Instant.now();
        List<ImagenMedica> imagenes = new ArrayList<>();
        imagenes.add(new ImagenMedica(1L, estudio, "1", "DICOM",
                "dicom://pacs/" + estudio + "/serie-1/imagen-1.dcm",
                "Corte axial", adquisicion));
        imagenes.add(new ImagenMedica(2L, estudio, "2", "DICOM",
                "dicom://pacs/" + estudio + "/serie-2/imagen-1.dcm",
                "Corte coronal", adquisicion));
        imagenes.add(new ImagenMedica(3L, estudio, "3", "DICOM",
                "dicom://pacs/" + estudio + "/serie-3/imagen-1.dcm",
                "Corte sagital", adquisicion));
        return imagenes;
    }
}
