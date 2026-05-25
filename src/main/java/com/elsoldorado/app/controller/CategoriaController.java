package com.elsoldorado.app.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.elsoldorado.app.model.Categoria;
import com.elsoldorado.app.service.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) { this.categoriaService = categoriaService; }

    @GetMapping
    public List<Categoria> obtenerTodasLasCategorias() { return categoriaService.obtenerTodasLasCategorias(); }

    @GetMapping("/{id}")
    public Categoria obtenerCategoriaPorId(@PathVariable Long id) {
        return categoriaService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));
    }

    @GetMapping("/buscar")
    public List<Categoria> buscarPorNombre(@RequestParam String texto) { return categoriaService.buscarPorNombre(texto); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Categoria crearCategoria(@RequestBody Categoria categoria) { return categoriaService.crearCategoria(categoria); }

    @PutMapping("/{id}")
    public Categoria actualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        return categoriaService.actualizarCategoria(id, categoria)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarCategoria(@PathVariable Long id) {
        if (!categoriaService.eliminarCategoria(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada");
    }
}
