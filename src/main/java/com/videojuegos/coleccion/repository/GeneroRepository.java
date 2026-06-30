package com.videojuegos.coleccion.repository;

import com.videojuegos.coleccion.entity.Genero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GeneroRepository extends JpaRepository<Genero, Long> {
    Optional<Genero> findByRawgId(Long rawgId);
}
