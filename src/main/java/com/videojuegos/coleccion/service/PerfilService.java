package com.videojuegos.coleccion.service;

import com.videojuegos.coleccion.dto.coleccion.JuegoColeccionResponse;
import com.videojuegos.coleccion.dto.perfil.PerfilPublicoResponse;
import com.videojuegos.coleccion.entity.Usuario;
import com.videojuegos.coleccion.exception.RecursoNoEncontradoException;
import com.videojuegos.coleccion.repository.JuegoColeccionRepository;
import com.videojuegos.coleccion.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerfilService {

    private final UsuarioRepository usuarioRepository;
    private final JuegoColeccionRepository juegoColeccionRepository;

    public PerfilPublicoResponse obtenerPerfilPublico(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Perfil no encontrado"));

        if (!usuario.isPerfilPublico()) {
            throw new RecursoNoEncontradoException("Perfil no encontrado");
        }

        var coleccion = juegoColeccionRepository.findByUsuario(usuario).stream()
                .map(JuegoColeccionResponse::desde)
                .toList();

        return new PerfilPublicoResponse(usuario.getUsername(), usuario.getNombreVisible(), coleccion);
    }

    @Transactional
    public void actualizarVisibilidad(String username, boolean publico) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        usuario.setPerfilPublico(publico);
        usuarioRepository.save(usuario);
    }
}
