package com.videojuegos.coleccion.dto.juego;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CrearResenaRequest(
        @NotBlank String resena,
        @Min(1) @Max(10) Integer ratingPersonal
) {
}
