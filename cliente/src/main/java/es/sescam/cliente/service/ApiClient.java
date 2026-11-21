package es.sescam.cliente.service;

/**
 * Fachada legacy. Preferir contratos específicos por entidad:
 * {@link CitaApiClient}, {@link PacienteApiClient}, {@link DoctorApiClient}.
 */
@Deprecated
public interface ApiClient extends CitaApiClient, PacienteApiClient, DoctorApiClient {
}
