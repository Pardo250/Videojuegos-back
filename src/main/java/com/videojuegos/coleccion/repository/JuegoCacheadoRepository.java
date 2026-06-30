package com.videojuegos.coleccion.repository;

import com.videojuegos.coleccion.entity.JuegoCacheado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JuegoCacheadoRepository extends JpaRepository<JuegoCacheado, Long> {
    Optional<JuegoCacheado> findByRawgId(Long rawgId);
}
