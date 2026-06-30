package com.videojuegos.coleccion.dto.coleccion;

import com.videojuegos.coleccion.dto.juego.JuegoResumenResponse;
import com.videojuegos.coleccion.entity.EstadoJuego;
import com.videojuegos.coleccion.entity.JuegoColeccion;

import java.time.Instant;
import java.time.LocalDate;

public record JuegoColeccionResponse(
        Long id,
        JuegoResumenResponse juego,
        EstadoJuego estado,
        Integer horasJugadas,
        Integer ratingPersonal,
        String resena,
        Instant fechaAgregado,
        LocalDate fechaCompletado
) {
    public static JuegoColeccionResponse desde(JuegoColeccion coleccion) {
        return new JuegoColeccionResponse(
                coleccion.getId(),
                JuegoResumenResponse.desde(coleccion.getJuego()),
                coleccion.getEstado(),
                coleccion.getHorasJugadas(),
                coleccion.getRatingPersonal(),
                coleccion.getResena(),
                coleccion.getFechaAgregado(),
                coleccion.getFechaCompletado()
        );
    }
}
