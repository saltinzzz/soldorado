package com.elsoldorado.app.service;

import com.elsoldorado.app.model.Categoria;
import com.elsoldorado.app.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@SuppressWarnings("null")
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> obtenerTodasLasCategorias() { return categoriaRepository.findAll(); }
    public Optional<Categoria> buscarPorId(Long id) { return categoriaRepository.findById(id); }
    public List<Categoria> buscarPorNombre(String texto) { return categoriaRepository.buscarPorNombre(texto); }

    public Categoria crearCategoria(Categoria categoria) {
        if (categoria == null || categoria.getNombre() == null || categoria.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }
        if (categoriaRepository.existsByNombreIgnoreCase(categoria.getNombre())) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        }
        return categoriaRepository.save(categoria);
    }

    public Optional<Categoria> actualizarCategoria(Long id, Categoria categoriaActualizada) {
        return categoriaRepository.findById(id).map(categoria -> {
            if (categoriaActualizada.getNombre() != null && !categoriaActualizada.getNombre().isBlank()) {
                if (!categoria.getNombre().equalsIgnoreCase(categoriaActualizada.getNombre()) && categoriaRepository.existsByNombreIgnoreCase(categoriaActualizada.getNombre())) {
                    throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
                }
                categoria.setNombre(categoriaActualizada.getNombre());
            }
            return categoriaRepository.save(categoria);
        });
    }

    public boolean eliminarCategoria(Long id) {
        return categoriaRepository.findById(id).map(c -> { categoriaRepository.delete(c); return true; }).orElse(false);
    }
}
