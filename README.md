# Videojuegos-back

Backend de una app de colección personal de videojuegos. Permite registrar tu biblioteca, buscar juegos vía la API de [RAWG](https://rawg.io/apidocs), llevar estado/horas/rating/reseña de cada juego, ver estadísticas y compartir un perfil público.

> Este repo es **solo el backend**. El frontend (Angular + TailwindCSS, estilo gamer) vive en otro proyecto.

## Stack

- **Java 21** + **Spring Boot 3.5**
- **Spring Security** + **JWT** (jjwt 0.12.6) para autenticación stateless
- **Spring Data JPA** + **PostgreSQL**
- **Lombok**
- **RestClient** (Spring 6) para consumir la API de RAWG
- **spring-dotenv** para cargar variables desde `.env` automáticamente
- **Docker Compose** para levantar PostgreSQL en local

## Arquitectura

```
com.videojuegos.coleccion
├── config         # SecurityConfig, RawgConfig
├── controller      # AuthController, BuscarController, ColeccionController, EstadisticasController, PerfilController
├── dto             # records de entrada/salida, organizados por feature (auth, juego, coleccion, estadisticas, perfil, rawg)
├── entity          # Usuario, JuegoCacheado, Plataforma, Genero, JuegoColeccion, EstadoJuego
├── exception       # excepciones de dominio + GlobalExceptionHandler
├── repository      # interfaces JpaRepository
├── security        # JwtService, JwtAuthFilter, CustomUserDetailsService
└── service         # AuthService, RawgService, ColeccionService, EstadisticasService, PerfilService
```

### Modelo de datos

- **Usuario** → tiene muchos → **JuegoColeccion**
- **JuegoColeccion** → referencia → **JuegoCacheado** (datos de RAWG cacheados localmente para no repetir llamadas a la API externa)
- **JuegoCacheado** → many-to-many → **Plataforma**, **Genero**

`JuegoColeccion` guarda: estado (`JUGANDO` / `COMPLETADO` / `ABANDONADO` / `PENDIENTE` / `WISHLIST`), horas jugadas, rating personal (1-10), reseña y fecha de completado (para estadísticas por año).

## Cómo correrlo

### 1. Requisitos
- Java 21
- Docker Desktop (para PostgreSQL)
- Una API key gratis de RAWG: https://rawg.io/apidocs

### 2. Configurar variables de entorno

Copia `.env.example` a `.env` y completa tu `RAWG_API_KEY` (el resto de valores ya tienen defaults razonables para desarrollo local):

```bash
cp .env.example .env
```

### 3. Levantar PostgreSQL

```bash
docker compose up -d
```

Levanta Postgres en el puerto **5433** (no 5432, para no chocar con instalaciones nativas de Postgres en Windows).

### 4. Arrancar el backend

```bash
./mvnw spring-boot:run
```

En Windows con PowerShell: `.\mvnw.cmd spring-boot:run`

El backend queda escuchando en `http://localhost:8080`. Las tablas se crean solas (`spring.jpa.hibernate.ddl-auto=update`).

## Endpoints

Todos los endpoints (salvo los marcados como públicos) requieren el header `Authorization: Bearer <token>` obtenido en login/registro.

### Auth (públicos)
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/auth/registro` | Crea una cuenta, devuelve JWT |
| POST | `/api/auth/login` | Login, devuelve JWT |

### Búsqueda (protegido)
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/buscar?query=&pagina=` | Busca juegos en RAWG en vivo |

### Colección (protegido)
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/coleccion` | Agrega un juego a tu colección (lo cachea desde RAWG si no existe localmente) |
| GET | `/api/coleccion?estado=` | Lista tu colección, opcionalmente filtrada por estado |
| PUT | `/api/coleccion/{id}` | Edita estado, horas, rating, reseña |
| DELETE | `/api/coleccion/{id}` | Elimina un registro de tu colección |

### Estadísticas (protegido)
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/estadisticas` | Totales por estado, plataforma y género, horas totales, completados por año |

### Perfil
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/perfil/{username}` | Perfil público (público solo si el usuario activó visibilidad; si no, 404) |
| PATCH | `/api/perfil/visibilidad` | Activa/desactiva tu perfil público (protegido) |

## Seguridad

- Passwords con BCrypt
- JWT stateless (sin sesiones de servidor), firmado con HS512
- Cada registro de colección/perfil verifica que pertenezca al usuario autenticado (sin esto, un usuario podría editar/ver datos de otro vía ID)
- CORS habilitado para `http://localhost:4200` (Angular dev server)
- La API key de RAWG vive solo en el backend — el frontend nunca la ve

## Estado actual

Construido y verificado end-to-end (registro, login, manejo de errores, búsqueda real en RAWG, cacheo automático, alta/edición/baja de colección, estadísticas, perfil público con control de visibilidad).

Pendiente para iterar: tests automatizados, paginación de colección/búsqueda, endpoint de detalle de juego individual, edición de perfil (nombre visible, etc.).
