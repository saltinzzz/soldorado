package com.elsoldorado.app.dto.response;

import com.elsoldorado.app.model.RolUsuario;

public class UsuarioResponseDTO {
    private Long id;
    private String username;
    private RolUsuario rol;
    private boolean enabled;

    public UsuarioResponseDTO(Long id, String username, RolUsuario rol, boolean enabled) {
        this.id = id;
        this.username = username;
        this.rol = rol;
        this.enabled = enabled;
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

    public boolean isEnabled() {
        return enabled;
    }
}
