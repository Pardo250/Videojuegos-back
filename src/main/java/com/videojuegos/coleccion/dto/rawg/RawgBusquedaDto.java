package com.videojuegos.coleccion.dto.rawg;

import java.util.List;

public record RawgBusquedaDto(
        Integer count,
        List<RawgJuegoDto> results
) {
}
