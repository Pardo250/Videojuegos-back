package com.videojuegos.coleccion.dto.juego;

import com.videojuegos.coleccion.entity.Genero;
import com.videojuegos.coleccion.entity.JuegoCacheado;
import com.videojuegos.coleccion.entity.Plataforma;

import java.time.LocalDate;
import java.util.List;

public record JuegoResumenResponse(
        Long rawgId,
        String nombre,
        String imagenUrl,
        LocalDate fechaLanzamiento,
        Double metacritic,
        List<String> plataformas,
        List<String> generos
) {
    public static JuegoResumenResponse desde(JuegoCacheado juego) {
        return new JuegoResumenResponse(
                juego.getRawgId(),
                juego.getNombre(),
                juego.getImagenUrl(),
                juego.getFechaLanzamiento(),
                juego.getMetacritic(),
                juego.getPlataformas().stream().map(Plataforma::getNombre).toList(),
                juego.getGeneros().stream().map(Genero::getNombre).toList()
        );
    }
}
