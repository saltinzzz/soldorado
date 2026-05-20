package com.elsoldorado.app.service;

import com.elsoldorado.app.model.Categoria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final List<Categoria> categorias = new ArrayList<>();
    private Long secuencia = 5L;

    public CategoriaService() {
        categorias.add(new Categoria(1L, "Entradas"));
        categorias.add(new Categoria(2L, "Platos de Fondo"));
        categorias.add(new Categoria(3L, "Bebidas"));
        categorias.add(new Categoria(4L, "Postres"));
    }

    public List<Categoria> obtenerTodasLasCategorias() {
        return categorias;
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return categorias.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    public Categoria crearCategoria(Categoria categoria) {
        if (categoria == null || categoria.getNombre() == null || categoria.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }
        if (nombreExiste(categoria.getNombre())) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        }
        categoria.setId(secuencia++);
        categorias.add(categoria);
        return categoria;
    }

    public Optional<Categoria> actualizarCategoria(Long id, Categoria categoriaActualizada) {
        return buscarPorId(id).map(categoria -> {
            if (categoriaActualizada.getNombre() != null && !categoriaActualizada.getNombre().isBlank()) {
                if (!categoria.getNombre().equalsIgnoreCase(categoriaActualizada.getNombre()) && nombreExiste(categoriaActualizada.getNombre())) {
                    throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
                }
                categoria.setNombre(categoriaActualizada.getNombre());
            }
            return categoria;
        });
    }

    private boolean nombreExiste(String nombre) {
        return categorias.stream()
                .anyMatch(c -> c.getNombre().equalsIgnoreCase(nombre));
    }

    public boolean eliminarCategoria(Long id) {
        return buscarPorId(id)
                .map(categorias::remove)
                .orElse(false);
    }
}