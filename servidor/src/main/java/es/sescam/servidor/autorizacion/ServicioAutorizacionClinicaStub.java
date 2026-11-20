package es.sescam.servidor.autorizacion;

import java.util.Objects;

import es.sescam.servidor.repository.SolicitudPruebaRepository;
import es.sescam.servidor.seguridad.Rol;
import es.sescam.servidor.seguridad.Usuario;

/**
 * Implementación provisional del servicio de autorización clínica (CU-09).
 * <p>
 * Mientras no se dispone de la integración real con la historia clínica
 * electrónica, se considera que existe relación clínica cuando el usuario tiene
 * rol {@code MEDICO} y el paciente tiene al menos una solicitud de prueba
 * registrada. Esta comprobación es un stub y deberá sustituirse por la
 * verificación de la HCE.
 * </p>
 */
public class ServicioAutorizacionClinicaStub implements ServicioAutorizacionClinica {

    private final SolicitudPruebaRepository solicitudPruebaRepository;

    /**
     * Crea el stub de autorización clínica con el repositorio de solicitudes.
     *
     * @param solicitudPruebaRepository repositorio de solicitudes de prueba
     */
    public ServicioAutorizacionClinicaStub(SolicitudPruebaRepository solicitudPruebaRepository) {
        this.solicitudPruebaRepository = Objects.requireNonNull(solicitudPruebaRepository,
                "solicitudPruebaRepository");
    }

    @Override
    public boolean tieneRelacionClinica(Usuario usuario, Long pacienteId) {
        if (usuario == null || !usuario.tieneRol(Rol.MEDICO) || pacienteId == null) {
            return false;
        }
        return !solicitudPruebaRepository.findByPacienteId(pacienteId).isEmpty();
    }
}
