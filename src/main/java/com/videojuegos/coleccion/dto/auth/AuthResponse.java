package com.videojuegos.coleccion.dto.auth;

public record AuthResponse(
        String token,
        String username,
        String email
) {
}
