package com.videojuegos.coleccion.repository;

import com.videojuegos.coleccion.entity.EstadoJuego;
import com.videojuegos.coleccion.entity.JuegoColeccion;
import com.videojuegos.coleccion.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JuegoColeccionRepository extends JpaRepository<JuegoColeccion, Long> {
    List<JuegoColeccion> findByUsuario(Usuario usuario);

    List<JuegoColeccion> findByUsuarioAndEstado(Usuario usuario, EstadoJuego estado);

    Optional<JuegoColeccion> findByUsuarioAndJuego_Id(Usuario usuario, Long juegoId);

    boolean existsByUsuarioAndJuego_Id(Usuario usuario, Long juegoId);
}
