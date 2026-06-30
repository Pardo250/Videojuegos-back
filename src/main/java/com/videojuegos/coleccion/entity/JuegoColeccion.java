package com.videojuegos.coleccion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "juegos_coleccion", uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "juego_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JuegoColeccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "juego_id", nullable = false)
    private JuegoCacheado juego;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoJuego estado;

    @Builder.Default
    private Integer horasJugadas = 0;

    private Integer ratingPersonal;

    @Column(columnDefinition = "TEXT")
    private String resena;

    @Column(nullable = false, updatable = false)
    private Instant fechaAgregado;

    private LocalDate fechaCompletado;

    @PrePersist
    void prePersist() {
        fechaAgregado = Instant.now();
    }
}
