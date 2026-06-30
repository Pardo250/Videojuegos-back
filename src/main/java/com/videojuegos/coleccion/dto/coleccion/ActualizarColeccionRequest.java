package com.videojuegos.coleccion.dto.coleccion;

import com.videojuegos.coleccion.entity.EstadoJuego;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ActualizarColeccionRequest(
        @NotNull EstadoJuego estado,
        @Min(0) Integer horasJugadas,
        @Min(1) @Max(10) Integer ratingPersonal,
        String resena,
        LocalDate fechaCompletado
) {
}
