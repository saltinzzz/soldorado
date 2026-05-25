package com.elsoldorado.app.service;

import com.elsoldorado.app.model.Categoria;
import com.elsoldorado.app.model.Plato;
import com.elsoldorado.app.repository.CategoriaRepository;
import com.elsoldorado.app.repository.PlatoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    private final PlatoRepository platoRepository;
    private final CategoriaRepository categoriaRepository;

    public MenuService(PlatoRepository platoRepository, CategoriaRepository categoriaRepository) {
        this.platoRepository = platoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Plato> obtenerMenuCompleto() { return platoRepository.findByDisponibleTrue(); }
    public List<Plato> obtenerVistaPredefinida() { return platoRepository.findByDisponibleTrueAndVisibleEnInicioTrue(); }
    public List<Plato> obtenerPlatosDestacados() { return platoRepository.findByDisponibleTrueAndDestacadoTrue(); }
    public List<Plato> obtenerPorCategoria(String categoria) { return platoRepository.findByDisponibleTrueAndCategoriaNombreIgnoreCase(categoria); }
    public List<Plato> buscarPorTexto(String texto) { return platoRepository.buscarPorNombreOCategoria(texto); }
    public List<Plato> buscarPorRangoPrecio(BigDecimal min, BigDecimal max) { return platoRepository.buscarPorRangoPrecio(min, max); }
    public List<Plato> buscarDisponiblesPorCategoriaYPrecioMaximo(Long categoriaId, BigDecimal precioMaximo) {
        return platoRepository.buscarDisponiblesPorCategoriaYPrecioMaximo(categoriaId, precioMaximo);
    }
    public Optional<Plato> buscarPorId(Long id) { return platoRepository.findById(id); }

    @Transactional
    public Plato actualizarPlato(Long id, Plato actualizado) {
        validarPlato(actualizado);
        Plato existente = platoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado"));
        existente.setNombre(actualizado.getNombre());
        existente.setDescripcion(actualizado.getDescripcion());
        existente.setPrecio(actualizado.getPrecio());
        existente.setCategoria(obtenerCategoriaValidada(actualizado));
        existente.setDisponible(actualizado.isDisponible());
        existente.setDestacado(actualizado.isDestacado());
        existente.setVisibleEnInicio(actualizado.isVisibleEnInicio());
        return platoRepository.save(existente);
    }

    @Transactional
    public void eliminarPlato(Long id) {
        Plato plato = platoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado"));
        platoRepository.delete(plato);
    }

    @Transactional
    public Plato cambiarDisponibilidad(Long id, boolean disponible) {
        Plato plato = platoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado"));
        plato.setDisponible(disponible);
        return platoRepository.save(plato);
    }

    @Transactional
    public Plato agregarPlato(Plato plato) {
        validarPlato(plato);
        plato.setCategoria(obtenerCategoriaValidada(plato));
        return platoRepository.save(plato);
    }

    private Categoria obtenerCategoriaValidada(Plato plato) {
        return categoriaRepository.findById(plato.getCategoria().getId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    private void validarPlato(Plato plato) {
        if (plato == null) throw new IllegalArgumentException("El plato es obligatorio");
        if (plato.getNombre() == null || plato.getNombre().isBlank())
            throw new IllegalArgumentException("El nombre del plato es obligatorio");
        if (plato.getPrecio() == null || plato.getPrecio().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("El precio debe ser mayor que cero");
        if (plato.getCategoria() == null || plato.getCategoria().getId() == null)
            throw new IllegalArgumentException("La categoría es obligatoria");
    }
}
