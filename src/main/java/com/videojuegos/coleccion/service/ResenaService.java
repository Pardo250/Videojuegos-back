package com.videojuegos.coleccion.service;

import com.videojuegos.coleccion.dto.juego.CrearResenaRequest;
import com.videojuegos.coleccion.dto.juego.ResenaResponse;
import com.videojuegos.coleccion.entity.JuegoCacheado;
import com.videojuegos.coleccion.entity.Resena;
import com.videojuegos.coleccion.entity.Usuario;
import com.videojuegos.coleccion.exception.RecursoNoEncontradoException;
import com.videojuegos.coleccion.repository.ResenaRepository;
import com.videojuegos.coleccion.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ResenaService {

    private final ResenaRepository resenaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RawgService rawgService;

    public ResenaResponse crear(String username, Long rawgId, CrearResenaRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        JuegoCacheado juego = rawgService.obtenerOCachearJuego(rawgId);

        Resena resena = Resena.builder()
                .usuario(usuario)
                .juego(juego)
                .ratingPersonal(request.ratingPersonal())
                .texto(request.resena())
                .build();

        return ResenaResponse.desde(resenaRepository.save(resena));
    }

    @Transactional(readOnly = true)
    public List<ResenaResponse> listar(Long rawgId) {
        return resenaRepository.findByJuego_RawgIdOrderByFechaCreacionDesc(rawgId).stream()
                .map(ResenaResponse::desde)
                .toList();
    }
}
