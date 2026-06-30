package com.videojuegos.coleccion.dto.rawg;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RawgJuegoDto(
        Long id,
        String name,
        String released,
        @JsonProperty("background_image") String backgroundImage,
        Double metacritic,
        List<RawgGeneroDto> genres,
        List<RawgPlataformaWrapperDto> platforms
) {
}
