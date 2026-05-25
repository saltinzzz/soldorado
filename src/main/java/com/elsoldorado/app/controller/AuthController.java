package com.elsoldorado.app.controller;

import com.elsoldorado.app.dto.request.LoginRequestDTO;
import com.elsoldorado.app.dto.response.LoginResponseDTO;
import com.elsoldorado.app.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // Endpoint principal - genera el JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generarToken(userDetails);

            return ResponseEntity.ok(new LoginResponseDTO(token, userDetails.getUsername()));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales incorrectas");
        }
    }

    // Endpoints de prueba de tu compañero - los mantenemos
    @GetMapping("/public")
    public String publico() {
        return "Endpoint público - sin autenticación";
    }

    @GetMapping("/user/home")
    public String user() {
        return "Bienvenido USER";
    }

    @GetMapping("/admin/home")
    public String admin() {
        return "Bienvenido ADMIN";
    }

    @GetMapping("/empleados/home")
    public String empleados() {
        return "Zona empleados";
    }
}