package com.videojuegos.coleccion.dto.juego;

import com.videojuegos.coleccion.entity.Resena;

import java.time.Instant;

public record ResenaResponse(
        String username,
        String nombreVisible,
        Integer ratingPersonal,
        String resena,
        Instant fechaCreacion
) {
    public static ResenaResponse desde(Resena resena) {
        return new ResenaResponse(
                resena.getUsuario().getUsername(),
                resena.getUsuario().getNombreVisible(),
                resena.getRatingPersonal(),
                resena.getTexto(),
                resena.getFechaCreacion()
        );
    }
}
