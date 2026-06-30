package com.videojuegos.coleccion.dto.estadisticas;

import com.videojuegos.coleccion.entity.EstadoJuego;

import java.util.Map;

public record EstadisticasResponse(
        int totalJuegos,
        Map<EstadoJuego, Long> juegosPorEstado,
        Map<String, Long> juegosPorPlataforma,
        Map<String, Long> juegosPorGenero,
        long horasTotalesJugadas,
        Map<Integer, Long> completadosPorAnio
) {
}
