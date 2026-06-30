package com.videojuegos.coleccion.service;

import com.videojuegos.coleccion.dto.auth.AuthResponse;
import com.videojuegos.coleccion.dto.auth.LoginRequest;
import com.videojuegos.coleccion.dto.auth.RegisterRequest;
import com.videojuegos.coleccion.entity.Usuario;
import com.videojuegos.coleccion.exception.RecursoDuplicadoException;
import com.videojuegos.coleccion.repository.UsuarioRepository;
import com.videojuegos.coleccion.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse registrar(RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.username())) {
            throw new RecursoDuplicadoException("El nombre de usuario ya esta en uso");
        }
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RecursoDuplicadoException("El email ya esta registrado");
        }

        Usuario usuario = Usuario.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        usuarioRepository.save(usuario);

        String token = jwtService.generarToken(toUserDetails(usuario));
        return new AuthResponse(token, usuario.getUsername(), usuario.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        Usuario usuario = usuarioRepository.findByUsername(request.username())
                .orElseThrow();

        String token = jwtService.generarToken(toUserDetails(usuario));
        return new AuthResponse(token, usuario.getUsername(), usuario.getEmail());
    }

    private UserDetails toUserDetails(Usuario usuario) {
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities("ROLE_USER")
                .build();
    }
}
