package com.elsoldorado.app.service;

import com.elsoldorado.app.model.Plato;
import com.elsoldorado.app.repository.PlatoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    private final PlatoRepository platoRepository;

    public MenuService(PlatoRepository platoRepository) {
        this.platoRepository = platoRepository;
    }

    public List<Plato> obtenerMenuCompleto() { return platoRepository.findByDisponibleTrue(); }
    public List<Plato> obtenerVistaPredefinida() { return platoRepository.findByDisponibleTrueAndVisibleEnInicioTrue(); }
    public List<Plato> obtenerPlatosDestacados() { return platoRepository.findByDisponibleTrueAndDestacadoTrue(); }
    public List<Plato> obtenerPorCategoria(String categoria) { return platoRepository.findByDisponibleTrueAndCategoriaNombreIgnoreCase(categoria); }
    public List<Plato> buscarPorTexto(String texto) { return platoRepository.buscarPorNombreOCategoria(texto); }
    public List<Plato> buscarPorRangoPrecio(BigDecimal min, BigDecimal max) { return platoRepository.buscarPorRangoPrecio(min, max); }
    public Optional<Plato> buscarPorId(Long id) { return platoRepository.findById(id); }

    public Plato actualizarPlato(Long id, Plato actualizado) {
        Plato existente = platoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado"));
        existente.setNombre(actualizado.getNombre());
        existente.setDescripcion(actualizado.getDescripcion());
        existente.setPrecio(actualizado.getPrecio());
        existente.setCategoria(actualizado.getCategoria());
        existente.setDisponible(actualizado.isDisponible());
        existente.setDestacado(actualizado.isDestacado());
        existente.setVisibleEnInicio(actualizado.isVisibleEnInicio());
        return platoRepository.save(existente);
    }

    public void eliminarPlato(Long id) {
        Plato plato = platoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado"));
        platoRepository.delete(plato);
    }

    public Plato cambiarDisponibilidad(Long id, boolean disponible) {
        Plato plato = platoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado"));
        plato.setDisponible(disponible);
        return platoRepository.save(plato);
    }

    public Plato agregarPlato(Plato plato) {
        if (plato.getNombre() == null || plato.getNombre().isBlank())
            throw new RuntimeException("El nombre del plato es obligatorio");
        if (plato.getPrecio() == null || plato.getPrecio().compareTo(BigDecimal.ZERO) <= 0)
            throw new RuntimeException("El precio debe ser mayor que cero");
        if (plato.getCategoria() == null)
            throw new RuntimeException("La categoría es obligatoria");
        return platoRepository.save(plato);
    }
}
