package com.elsoldorado.app.service;

import com.elsoldorado.app.dto.request.AuthRequestDTO;
import com.elsoldorado.app.dto.request.RegistroUsuarioRequestDTO;
import com.elsoldorado.app.dto.response.AuthResponseDTO;
import com.elsoldorado.app.model.RolUsuario;
import com.elsoldorado.app.model.Usuario;
import com.elsoldorado.app.repository.UsuarioRepository;
import com.elsoldorado.app.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager,
                       UsuarioRepository usuarioRepository,
                       UsuarioService usuarioService,
                       JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    public AuthResponseDTO login(AuthRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        String token = jwtService.generarToken(usuario);
        return new AuthResponseDTO(token, usuario.getId(), usuario.getUsername(), usuario.getRol());
    }

    public AuthResponseDTO registrarCliente(RegistroUsuarioRequestDTO request) {
        request.setRol(RolUsuario.CLIENTE);
        Usuario usuario = usuarioService.crearUsuario(request, RolUsuario.CLIENTE);
        String token = jwtService.generarToken(usuario);
        return new AuthResponseDTO(token, usuario.getId(), usuario.getUsername(), usuario.getRol());
    }
}
