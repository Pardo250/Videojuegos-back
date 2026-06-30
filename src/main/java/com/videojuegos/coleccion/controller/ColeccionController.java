package com.videojuegos.coleccion.controller;

import com.videojuegos.coleccion.dto.coleccion.ActualizarColeccionRequest;
import com.videojuegos.coleccion.dto.coleccion.AgregarColeccionRequest;
import com.videojuegos.coleccion.dto.coleccion.JuegoColeccionResponse;
import com.videojuegos.coleccion.entity.EstadoJuego;
import com.videojuegos.coleccion.service.ColeccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/coleccion")
@RequiredArgsConstructor
public class ColeccionController {

    private final ColeccionService coleccionService;

    @PostMapping
    public ResponseEntity<JuegoColeccionResponse> agregar(Authentication auth,
                                                            @Valid @RequestBody AgregarColeccionRequest request) {
        return ResponseEntity.ok(coleccionService.agregar(auth.getName(), request));
    }

    @GetMapping
    public ResponseEntity<List<JuegoColeccionResponse>> listar(Authentication auth,
                                                                @RequestParam(required = false) EstadoJuego estado) {
        return ResponseEntity.ok(coleccionService.listar(auth.getName(), estado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JuegoColeccionResponse> actualizar(Authentication auth,
                                                              @PathVariable Long id,
                                                              @Valid @RequestBody ActualizarColeccionRequest request) {
        return ResponseEntity.ok(coleccionService.actualizar(auth.getName(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(Authentication auth, @PathVariable Long id) {
        coleccionService.eliminar(auth.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
