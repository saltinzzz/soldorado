package com.elsoldorado.app.dto.request;

import com.elsoldorado.app.model.RolUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistroUsuarioRequestDTO {
    @NotBlank(message = "El usuario es obligatorio")
    @Size(min = 3, max = 80, message = "El usuario debe tener entre 3 y 80 caracteres")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 80, message = "La contraseña debe tener entre 6 y 80 caracteres")
    private String password;

    private RolUsuario rol;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }
}
