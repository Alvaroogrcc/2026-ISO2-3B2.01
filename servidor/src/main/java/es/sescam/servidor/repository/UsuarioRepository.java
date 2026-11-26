package es.sescam.servidor.repository;

import java.util.List;
import java.util.Optional;

import es.sescam.servidor.seguridad.Usuario;

/**
 * Repositorio de usuarios del sistema (CU-18).
 */
public interface UsuarioRepository {

    List<Usuario> findAll();

    Optional<Usuario> findById(String id);

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    Usuario save(Usuario usuario);
}
