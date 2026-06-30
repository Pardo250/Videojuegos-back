package com.videojuegos.coleccion.service;

import com.videojuegos.coleccion.dto.coleccion.ActualizarColeccionRequest;
import com.videojuegos.coleccion.dto.coleccion.AgregarColeccionRequest;
import com.videojuegos.coleccion.dto.coleccion.JuegoColeccionResponse;
import com.videojuegos.coleccion.entity.EstadoJuego;
import com.videojuegos.coleccion.entity.JuegoCacheado;
import com.videojuegos.coleccion.entity.JuegoColeccion;
import com.videojuegos.coleccion.entity.Usuario;
import com.videojuegos.coleccion.exception.RecursoDuplicadoException;
import com.videojuegos.coleccion.exception.RecursoNoEncontradoException;
import com.videojuegos.coleccion.repository.JuegoColeccionRepository;
import com.videojuegos.coleccion.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ColeccionService {

    private final JuegoColeccionRepository juegoColeccionRepository;
    private final UsuarioRepository usuarioRepository;
    private final RawgService rawgService;

    public JuegoColeccionResponse agregar(String username, AgregarColeccionRequest request) {
        Usuario usuario = obtenerUsuario(username);
        JuegoCacheado juego = rawgService.obtenerOCachearJuego(request.rawgId());

        if (juegoColeccionRepository.existsByUsuarioAndJuego_Id(usuario, juego.getId())) {
            throw new RecursoDuplicadoException("Este juego ya esta en tu coleccion");
        }

        JuegoColeccion coleccion = JuegoColeccion.builder()
                .usuario(usuario)
                .juego(juego)
                .estado(request.estado())
                .horasJugadas(request.horasJugadas() == null ? 0 : request.horasJugadas())
                .ratingPersonal(request.ratingPersonal())
                .resena(request.resena())
                .fechaCompletado(request.estado() == EstadoJuego.COMPLETADO ? LocalDate.now() : null)
                .build();

        return JuegoColeccionResponse.desde(juegoColeccionRepository.save(coleccion));
    }

    @Transactional(readOnly = true)
    public List<JuegoColeccionResponse> listar(String username, EstadoJuego estado) {
        Usuario usuario = obtenerUsuario(username);

        List<JuegoColeccion> juegos = estado == null
                ? juegoColeccionRepository.findByUsuario(usuario)
                : juegoColeccionRepository.findByUsuarioAndEstado(usuario, estado);

        return juegos.stream().map(JuegoColeccionResponse::desde).toList();
    }

    public JuegoColeccionResponse actualizar(String username, Long coleccionId, ActualizarColeccionRequest request) {
        JuegoColeccion coleccion = obtenerDeUsuario(username, coleccionId);

        coleccion.setEstado(request.estado());
        if (request.horasJugadas() != null) {
            coleccion.setHorasJugadas(request.horasJugadas());
        }
        coleccion.setRatingPersonal(request.ratingPersonal());
        coleccion.setResena(request.resena());

        if (request.estado() == EstadoJuego.COMPLETADO) {
            coleccion.setFechaCompletado(request.fechaCompletado() != null ? request.fechaCompletado() : LocalDate.now());
        } else {
            coleccion.setFechaCompletado(null);
        }

        return JuegoColeccionResponse.desde(juegoColeccionRepository.save(coleccion));
    }

    public void eliminar(String username, Long coleccionId) {
        JuegoColeccion coleccion = obtenerDeUsuario(username, coleccionId);
        juegoColeccionRepository.delete(coleccion);
    }

    private JuegoColeccion obtenerDeUsuario(String username, Long coleccionId) {
        Usuario usuario = obtenerUsuario(username);
        JuegoColeccion coleccion = juegoColeccionRepository.findById(coleccionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Registro de coleccion no encontrado"));

        if (!coleccion.getUsuario().getId().equals(usuario.getId())) {
            throw new RecursoNoEncontradoException("Registro de coleccion no encontrado");
        }

        return coleccion;
    }

    private Usuario obtenerUsuario(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
    }
}
