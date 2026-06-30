package com.videojuegos.coleccion.dto.perfil;

import jakarta.validation.constraints.NotNull;

public record ActualizarVisibilidadRequest(
        @NotNull Boolean publico
) {
}
