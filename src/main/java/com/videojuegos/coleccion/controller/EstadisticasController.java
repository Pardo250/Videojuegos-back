package com.videojuegos.coleccion.controller;

import com.videojuegos.coleccion.dto.estadisticas.EstadisticasResponse;
import com.videojuegos.coleccion.service.EstadisticasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estadisticas")
@RequiredArgsConstructor
public class EstadisticasController {

    private final EstadisticasService estadisticasService;

    @GetMapping
    public ResponseEntity<EstadisticasResponse> obtener(Authentication auth) {
        return ResponseEntity.ok(estadisticasService.generar(auth.getName()));
    }
}
