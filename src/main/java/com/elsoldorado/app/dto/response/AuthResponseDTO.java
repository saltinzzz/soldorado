package com.elsoldorado.app.dto.response;

import com.elsoldorado.app.model.RolUsuario;

public class AuthResponseDTO {
    private String token;
    private String tipo = "Bearer";
    private Long id;
    private String username;
    private RolUsuario rol;

    public AuthResponseDTO(String token, Long id, String username, RolUsuario rol) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public String getTipo() {
        return tipo;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public RolUsuario getRol() {
        return rol;
    }
}
