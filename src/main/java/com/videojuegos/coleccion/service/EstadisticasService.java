package com.videojuegos.coleccion.service;

import com.videojuegos.coleccion.dto.estadisticas.EstadisticasResponse;
import com.videojuegos.coleccion.entity.EstadoJuego;
import com.videojuegos.coleccion.entity.Genero;
import com.videojuegos.coleccion.entity.JuegoColeccion;
import com.videojuegos.coleccion.entity.Plataforma;
import com.videojuegos.coleccion.entity.Usuario;
import com.videojuegos.coleccion.exception.RecursoNoEncontradoException;
import com.videojuegos.coleccion.repository.JuegoColeccionRepository;
import com.videojuegos.coleccion.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EstadisticasService {

    private final JuegoColeccionRepository juegoColeccionRepository;
    private final UsuarioRepository usuarioRepository;

    public EstadisticasResponse generar(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        List<JuegoColeccion> juegos = juegoColeccionRepository.findByUsuario(usuario);

        Map<EstadoJuego, Long> porEstado = juegos.stream()
                .collect(Collectors.groupingBy(JuegoColeccion::getEstado, Collectors.counting()));

        Map<String, Long> porPlataforma = juegos.stream()
                .flatMap(jc -> jc.getJuego().getPlataformas().stream())
                .collect(Collectors.groupingBy(Plataforma::getNombre, Collectors.counting()));

        Map<String, Long> porGenero = juegos.stream()
                .flatMap(jc -> jc.getJuego().getGeneros().stream())
                .collect(Collectors.groupingBy(Genero::getNombre, Collectors.counting()));

        long horasTotales = juegos.stream()
                .mapToLong(jc -> jc.getHorasJugadas() == null ? 0 : jc.getHorasJugadas())
                .sum();

        Map<Integer, Long> completadosPorAnio = juegos.stream()
                .filter(jc -> jc.getEstado() == EstadoJuego.COMPLETADO && jc.getFechaCompletado() != null)
                .collect(Collectors.groupingBy(jc -> jc.getFechaCompletado().getYear(), Collectors.counting()));

        return new EstadisticasResponse(juegos.size(), porEstado, porPlataforma, porGenero, horasTotales, completadosPorAnio);
    }
}
