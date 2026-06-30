package com.videojuegos.coleccion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "juegos_cacheados")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JuegoCacheado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long rawgId;

    @Column(nullable = false)
    private String nombre;

    private String imagenUrl;

    private LocalDate fechaLanzamiento;

    private Double metacritic;

    @ManyToMany
    @JoinTable(
            name = "juego_plataforma",
            joinColumns = @JoinColumn(name = "juego_id"),
            inverseJoinColumns = @JoinColumn(name = "plataforma_id")
    )
    @Builder.Default
    private Set<Plataforma> plataformas = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "juego_genero",
            joinColumns = @JoinColumn(name = "juego_id"),
            inverseJoinColumns = @JoinColumn(name = "genero_id")
    )
    @Builder.Default
    private Set<Genero> generos = new HashSet<>();

    @Column(nullable = false)
    private Instant fechaCacheo;

    @PrePersist
    @PreUpdate
    void onSave() {
        fechaCacheo = Instant.now();
    }
}
