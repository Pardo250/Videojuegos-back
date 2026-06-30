package com.videojuegos.coleccion.dto.perfil;

import com.videojuegos.coleccion.dto.coleccion.JuegoColeccionResponse;

import java.util.List;

public record PerfilPublicoResponse(
        String username,
        String nombreVisible,
        List<JuegoColeccionResponse> coleccion
) {
}
