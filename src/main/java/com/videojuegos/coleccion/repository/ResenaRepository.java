package com.videojuegos.coleccion.repository;

import com.videojuegos.coleccion.entity.Resena;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByJuego_RawgIdOrderByFechaCreacionDesc(Long rawgId);
}
