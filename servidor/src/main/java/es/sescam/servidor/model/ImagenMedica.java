package es.sescam.servidor.model;

import java.time.Instant;

/**
 * Metadatos de una imagen médica asociada a un estudio (CU-09).
 * <p>
 * El modelo solo describe la imagen: identificador del estudio, serie, formato,
 * URL de acceso, descripción y fecha de adquisición. La descarga y decodificación
 * de los binarios corresponde al visor y a la integración con el RIS/PACS.
 * </p>
 */
public class ImagenMedica {

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
    public ImagenMedica() {
    }

    /**
     * Crea una imagen médica con todos sus metadatos.
     *
     * @param id identificador interno de la imagen
     * @param estudioId identificador del estudio al que pertenece
     * @param serie serie DICOM de la imagen
     * @param formato formato de la imagen
     * @param url URL de acceso al recurso
     * @param descripcion descripción de la imagen
     * @param fechaAdquisicion fecha y hora de adquisición
     */
    public ImagenMedica(Long id, String estudioId, String serie, String formato, String url,
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
     * Devuelve el identificador interno de la imagen.
     *
     * @return identificador interno
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador interno de la imagen.
     *
     * @param id identificador interno
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Devuelve el identificador del estudio al que pertenece la imagen.
     *
     * @return identificador del estudio
     */
    public String getEstudioId() {
        return estudioId;
    }

    /**
     * Establece el identificador del estudio al que pertenece la imagen.
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
