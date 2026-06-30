package com.videojuegos.coleccion.controller;

import com.videojuegos.coleccion.dto.juego.JuegoResumenResponse;
import com.videojuegos.coleccion.service.RawgService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/buscar")
@RequiredArgsConstructor
public class BuscarController {

    private final RawgService rawgService;

    @GetMapping
    public ResponseEntity<List<JuegoResumenResponse>> buscar(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int pagina) {
        return ResponseEntity.ok(rawgService.buscarJuegos(query, pagina));
    }
}
