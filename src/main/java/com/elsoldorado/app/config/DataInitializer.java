package com.elsoldorado.app.config;

import com.elsoldorado.app.model.RolUsuario;
import com.elsoldorado.app.model.Usuario;
import com.elsoldorado.app.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner crearUsuariosBase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            crearSiNoExiste(usuarioRepository, passwordEncoder, "admin", "admin123", RolUsuario.ADMIN);
            crearSiNoExiste(usuarioRepository, passwordEncoder, "empleado", "empleado123", RolUsuario.EMPLEADO);
            crearSiNoExiste(usuarioRepository, passwordEncoder, "cliente", "cliente123", RolUsuario.CLIENTE);
        };
    }

    private void crearSiNoExiste(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                                 String username, String password, RolUsuario rol) {
        if (!usuarioRepository.existsByUsernameIgnoreCase(username)) {
            Usuario usuario = new Usuario();
            usuario.setUsername(username);
            usuario.setPassword(passwordEncoder.encode(password));
            usuario.setRol(rol);
            usuario.setEnabled(true);
            usuarioRepository.save(usuario);
        }
    }
}
