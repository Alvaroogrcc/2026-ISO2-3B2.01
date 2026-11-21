package es.sescam.servidor.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import es.sescam.servidor.seguridad.Usuario;

/**
 * Implementación en memoria del repositorio de usuarios (CU-18).
 */
public class UsuarioRepositoryMemoria implements UsuarioRepository {

    private final ConcurrentMap<String, Usuario> usuarios = new ConcurrentHashMap<>();

    @Override
    public List<Usuario> findAll() {
        return new ArrayList<>(usuarios.values());
    }

    @Override
    public Optional<Usuario> findById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(usuarios.get(id));
    }

    @Override
    public Optional<Usuario> findByNombreUsuario(String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            return Optional.empty();
        }
        for (Usuario usuario : usuarios.values()) {
            if (nombreUsuario.equals(usuario.getNombreUsuario())) {
                return Optional.of(usuario);
            }
        }
        return Optional.empty();
    }

    @Override
    public Usuario save(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        if (usuario.getId() == null || usuario.getId().isBlank()) {
            throw new IllegalArgumentException("El id del usuario es obligatorio");
        }
        usuarios.put(usuario.getId(), usuario);
        return usuario;
    }
}
