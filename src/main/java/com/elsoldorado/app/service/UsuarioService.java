package com.elsoldorado.app.service;

import com.elsoldorado.app.dto.request.RegistroUsuarioRequestDTO;
import com.elsoldorado.app.model.RolUsuario;
import com.elsoldorado.app.model.Usuario;
import com.elsoldorado.app.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@SuppressWarnings("null")
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Transactional
    public Usuario crearUsuario(RegistroUsuarioRequestDTO dto, RolUsuario rolPorDefecto) {
        validarRegistro(dto);
        RolUsuario rol = dto.getRol() == null ? rolPorDefecto : dto.getRol();
        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername().trim().toLowerCase());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(rol);
        usuario.setEnabled(true);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario cambiarEstado(Long id, boolean enabled) {
        Usuario usuario = buscarPorId(id);
        usuario.setEnabled(enabled);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }

    private void validarRegistro(RegistroUsuarioRequestDTO dto) {
        if (dto == null) throw new IllegalArgumentException("Los datos de registro son obligatorios");
        if (dto.getUsername() == null || dto.getUsername().isBlank()) throw new IllegalArgumentException("El usuario es obligatorio");
        if (dto.getPassword() == null || dto.getPassword().isBlank()) throw new IllegalArgumentException("La contraseña es obligatoria");
        if (dto.getPassword().length() < 6) throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        if (usuarioRepository.existsByUsernameIgnoreCase(dto.getUsername())) throw new IllegalArgumentException("Ya existe un usuario con ese nombre");
    }
}
