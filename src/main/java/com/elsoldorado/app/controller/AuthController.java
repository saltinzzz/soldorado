package com.elsoldorado.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/auth/public")
    public String publico() {
        return "Endpoint público";
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