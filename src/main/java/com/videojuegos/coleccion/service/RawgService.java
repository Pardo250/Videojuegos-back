package com.videojuegos.coleccion.service;

import com.videojuegos.coleccion.dto.juego.JuegoResumenResponse;
import com.videojuegos.coleccion.dto.rawg.RawgBusquedaDto;
import com.videojuegos.coleccion.dto.rawg.RawgGeneroDto;
import com.videojuegos.coleccion.dto.rawg.RawgJuegoDto;
import com.videojuegos.coleccion.dto.rawg.RawgPlataformaDto;
import com.videojuegos.coleccion.dto.rawg.RawgPlataformaWrapperDto;
import com.videojuegos.coleccion.entity.Genero;
import com.videojuegos.coleccion.entity.JuegoCacheado;
import com.videojuegos.coleccion.entity.Plataforma;
import com.videojuegos.coleccion.exception.RecursoNoEncontradoException;
import com.videojuegos.coleccion.repository.GeneroRepository;
import com.videojuegos.coleccion.repository.JuegoCacheadoRepository;
import com.videojuegos.coleccion.repository.PlataformaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RawgService {

    private final RestClient rawgRestClient;
    private final JuegoCacheadoRepository juegoCacheadoRepository;
    private final PlataformaRepository plataformaRepository;
    private final GeneroRepository generoRepository;

    @Value("${rawg.api.key}")
    private String apiKey;

    public List<JuegoResumenResponse> buscarJuegos(String query, int pagina) {
        validarApiKey();

        RawgBusquedaDto respuesta = rawgRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/games")
                        .queryParam("key", apiKey)
                        .queryParam("search", query)
                        .queryParam("page", pagina)
                        .queryParam("page_size", 20)
                        .build())
                .retrieve()
                .body(RawgBusquedaDto.class);

        if (respuesta == null || respuesta.results() == null) {
            return List.of();
        }

        return respuesta.results().stream()
                .map(this::aResumen)
                .toList();
    }

    public JuegoCacheado obtenerOCachearJuego(Long rawgId) {
        return juegoCacheadoRepository.findByRawgId(rawgId)
                .orElseGet(() -> cachearDesdeRawg(rawgId));
    }

    private JuegoCacheado cachearDesdeRawg(Long rawgId) {
        validarApiKey();

        RawgJuegoDto dto = rawgRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/games/{id}")
                        .queryParam("key", apiKey)
                        .build(rawgId))
                .retrieve()
                .body(RawgJuegoDto.class);

        if (dto == null) {
            throw new RecursoNoEncontradoException("Juego no encontrado en RAWG: " + rawgId);
        }

        JuegoCacheado juego = JuegoCacheado.builder()
                .rawgId(dto.id())
                .nombre(dto.name())
                .imagenUrl(dto.backgroundImage())
                .fechaLanzamiento(parsearFecha(dto.released()))
                .metacritic(dto.metacritic())
                .plataformas(resolverPlataformas(dto))
                .generos(resolverGeneros(dto))
                .build();

        return juegoCacheadoRepository.save(juego);
    }

    private Set<Plataforma> resolverPlataformas(RawgJuegoDto dto) {
        if (dto.platforms() == null) {
            return new HashSet<>();
        }
        return dto.platforms().stream()
                .map(RawgPlataformaWrapperDto::platform)
                .filter(Objects::nonNull)
                .map(p -> plataformaRepository.findByRawgId(p.id())
                        .orElseGet(() -> plataformaRepository.save(
                                Plataforma.builder().rawgId(p.id()).nombre(p.name()).build())))
                .collect(Collectors.toSet());
    }

    private Set<Genero> resolverGeneros(RawgJuegoDto dto) {
        if (dto.genres() == null) {
            return new HashSet<>();
        }
        return dto.genres().stream()
                .map(g -> generoRepository.findByRawgId(g.id())
                        .orElseGet(() -> generoRepository.save(
                                Genero.builder().rawgId(g.id()).nombre(g.name()).build())))
                .collect(Collectors.toSet());
    }

    private JuegoResumenResponse aResumen(RawgJuegoDto dto) {
        List<String> plataformas = dto.platforms() == null ? List.of() : dto.platforms().stream()
                .map(RawgPlataformaWrapperDto::platform)
                .filter(Objects::nonNull)
                .map(RawgPlataformaDto::name)
                .toList();

        List<String> generos = dto.genres() == null ? List.of() : dto.genres().stream()
                .map(RawgGeneroDto::name)
                .toList();

        return new JuegoResumenResponse(dto.id(), dto.name(), dto.backgroundImage(),
                parsearFecha(dto.released()), dto.metacritic(), plataformas, generos);
    }

    private LocalDate parsearFecha(String fecha) {
        if (fecha == null || fecha.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(fecha);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private void validarApiKey() {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("RAWG_API_KEY no esta configurada. Define la variable de entorno antes de buscar juegos.");
        }
    }
}
