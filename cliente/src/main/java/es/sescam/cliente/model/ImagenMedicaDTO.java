package es.sescam.cliente.model;

import java.time.Instant;

/**
 * Copia de los metadatos de una imagen médica para viajar por la API del cliente
 * (CU-09).
 * <p>
 * Solo contiene los metadatos necesarios para listar y previsualizar el estudio:
 * la descarga y decodificación DICOM corresponde al visor integrado con el
 * RIS/PACS.
 * </p>
 */
public class ImagenMedicaDTO {

    private Long id;
    private String estudioId;
    private String serie;
    private String formato;
    private String url;
    private String descripcion;
    private Instant fechaAdquisicion;

    /**
     * Crea una imagen médica vacía.
     */
    public ImagenMedicaDTO() {
    }

    /**
     * Crea una imagen médica con todos sus metadatos.
     *
     * @param id identificador de la imagen
     * @param estudioId identificador del estudio
     * @param serie serie DICOM de la imagen
     * @param formato formato de la imagen
     * @param url URL de acceso al recurso
     * @param descripcion descripción de la imagen
     * @param fechaAdquisicion fecha y hora de adquisición
     */
    public ImagenMedicaDTO(Long id, String estudioId, String serie, String formato, String url,
                           String descripcion, Instant fechaAdquisicion) {
        this.id = id;
        this.estudioId = estudioId;
        this.serie = serie;
        this.formato = formato;
        this.url = url;
        this.descripcion = descripcion;
        this.fechaAdquisicion = fechaAdquisicion;
    }

    /**
     * Devuelve el identificador de la imagen.
     *
     * @return identificador
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador de la imagen.
     *
     * @param id identificador
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve el identificador del estudio.
     *
     * @return identificador del estudio
     */
    public String getEstudioId() {
        return estudioId;
    }

    /**
     * Establece el identificador del estudio.
     *
     * @param estudioId identificador del estudio
     */
    public void setEstudioId(String estudioId) {
        this.estudioId = estudioId;
    }

    /**
     * Devuelve la serie DICOM de la imagen.
     *
     * @return serie DICOM
     */
    public String getSerie() {
        return serie;
    }

    /**
     * Establece la serie DICOM de la imagen.
     *
     * @param serie serie DICOM
     */
    public void setSerie(String serie) {
        this.serie = serie;
    }

    /**
     * Devuelve el formato de la imagen.
     *
     * @return formato de la imagen
     */
    public String getFormato() {
        return formato;
    }

    /**
     * Establece el formato de la imagen.
     *
     * @param formato formato de la imagen
     */
    public void setFormato(String formato) {
        this.formato = formato;
    }

    /**
     * Devuelve la URL de acceso al recurso.
     *
     * @return URL del recurso
     */
    public String getUrl() {
        return url;
    }

    /**
     * Establece la URL de acceso al recurso.
     *
     * @param url URL del recurso
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Devuelve la descripción de la imagen.
     *
     * @return descripción de la imagen
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción de la imagen.
     *
     * @param descripcion descripción de la imagen
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve la fecha y hora de adquisición de la imagen.
     *
     * @return fecha de adquisición
     */
    public Instant getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    /**
     * Establece la fecha y hora de adquisición de la imagen.
     *
     * @param fechaAdquisicion fecha de adquisición
     */
    public void setFechaAdquisicion(Instant fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }
}
