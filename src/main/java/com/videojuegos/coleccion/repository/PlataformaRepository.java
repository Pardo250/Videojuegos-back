package com.videojuegos.coleccion.repository;

import com.videojuegos.coleccion.entity.Plataforma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlataformaRepository extends JpaRepository<Plataforma, Long> {
    Optional<Plataforma> findByRawgId(Long rawgId);
}
