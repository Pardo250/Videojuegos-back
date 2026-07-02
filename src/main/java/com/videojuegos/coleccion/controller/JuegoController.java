package com.videojuegos.coleccion.controller;

import com.videojuegos.coleccion.dto.juego.CrearResenaRequest;
import com.videojuegos.coleccion.dto.juego.JuegoResumenResponse;
import com.videojuegos.coleccion.dto.juego.ResenaResponse;
import com.videojuegos.coleccion.service.RawgService;
import com.videojuegos.coleccion.service.ResenaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/juegos")
@RequiredArgsConstructor
public class JuegoController {

    private final RawgService rawgService;
    private final ResenaService resenaService;

    @GetMapping("/{rawgId}")
    public ResponseEntity<JuegoResumenResponse> obtener(@PathVariable Long rawgId) {
        return ResponseEntity.ok(JuegoResumenResponse.desde(rawgService.obtenerOCachearJuego(rawgId)));
    }

    @GetMapping("/{rawgId}/resenas")
    public ResponseEntity<List<ResenaResponse>> resenas(@PathVariable Long rawgId) {
        return ResponseEntity.ok(resenaService.listar(rawgId));
    }

    @PostMapping("/{rawgId}/resenas")
    public ResponseEntity<ResenaResponse> crearResena(Authentication auth,
                                                        @PathVariable Long rawgId,
                                                        @Valid @RequestBody CrearResenaRequest request) {
        return ResponseEntity.ok(resenaService.crear(auth.getName(), rawgId, request));
    }
}
