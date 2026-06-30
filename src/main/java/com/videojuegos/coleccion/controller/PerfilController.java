package com.videojuegos.coleccion.controller;

import com.videojuegos.coleccion.dto.perfil.ActualizarVisibilidadRequest;
import com.videojuegos.coleccion.dto.perfil.PerfilPublicoResponse;
import com.videojuegos.coleccion.service.PerfilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService perfilService;

    @GetMapping("/{username}")
    public ResponseEntity<PerfilPublicoResponse> obtener(@PathVariable String username) {
        return ResponseEntity.ok(perfilService.obtenerPerfilPublico(username));
    }

    @PatchMapping("/visibilidad")
    public ResponseEntity<Void> actualizarVisibilidad(Authentication auth,
                                                        @Valid @RequestBody ActualizarVisibilidadRequest request) {
        perfilService.actualizarVisibilidad(auth.getName(), request.publico());
        return ResponseEntity.noContent().build();
    }
}
