package com.videojuegos.coleccion.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(cuerpoError(HttpStatus.BAD_REQUEST, "Error de validacion", errores));
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> manejarDuplicado(RecursoDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(cuerpoError(HttpStatus.CONFLICT, ex.getMessage(), null));
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cuerpoError(HttpStatus.NOT_FOUND, ex.getMessage(), null));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> manejarCredencialesInvalidas(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(cuerpoError(HttpStatus.UNAUTHORIZED, "Credenciales invalidas", null));
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Map<String, Object>> manejarErrorRawg(RestClientException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(cuerpoError(HttpStatus.BAD_GATEWAY, "Error al comunicarse con RAWG", null));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> manejarEstadoInvalido(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(cuerpoError(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), null));
    }

    private Map<String, Object> cuerpoError(HttpStatus status, String mensaje, Object detalles) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", status.value());
        body.put("mensaje", mensaje);
        if (detalles != null) {
            body.put("detalles", detalles);
        }
        return body;
    }
}
