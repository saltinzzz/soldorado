package com.elsoldorado.app.controller;

import com.elsoldorado.app.dto.request.AuthRequestDTO;
import com.elsoldorado.app.dto.request.RegistroUsuarioRequestDTO;
import com.elsoldorado.app.dto.response.AuthResponseDTO;
import com.elsoldorado.app.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody AuthRequestDTO request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDTO registrarCliente(@Valid @RequestBody RegistroUsuarioRequestDTO request) {
        return authService.registrarCliente(request);
    }
}
