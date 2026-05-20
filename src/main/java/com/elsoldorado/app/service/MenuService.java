package com.elsoldorado.app.service;

import com.elsoldorado.app.model.Categoria;
import com.elsoldorado.app.model.Plato;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final List<Plato> platos = new ArrayList<>();

    public MenuService(CategoriaService categoriaService) {
        Categoria entradas = categoriaService.buscarPorId(1L).orElseThrow();
        Categoria fondos = categoriaService.buscarPorId(2L).orElseThrow();
        Categoria bebidas = categoriaService.buscarPorId(3L).orElseThrow();
        Categoria postres = categoriaService.buscarPorId(4L).orElseThrow();

        platos.add(new Plato(1L, "Ceviche", "Pescado fresco con limón y ají", 32.00, entradas, true, true, true));
        platos.add(new Plato(2L, "Papa a la Huancaína", "Papa con crema de ají amarillo", 18.00, entradas, true, true, true));
        platos.add(new Plato(3L, "Lomo Saltado", "Carne salteada con papas fritas y arroz", 28.00, fondos, true, true, true));
        platos.add(new Plato(4L, "Ají de Gallina", "Pollo deshilachado en crema", 24.00, fondos, true, false, true));
        platos.add(new Plato(5L, "Chicha Morada", "Bebida tradicional peruana", 8.00, bebidas, true, true, true));
        platos.add(new Plato(6L, "Suspiro a la Limeña", "Postre tradicional peruano", 14.00, postres, true, false, false));
    }

    public List<Plato> obtenerMenuCompleto() {
        return platos.stream()
                .filter(Plato::isDisponible)
                .collect(Collectors.toList());
    }

    public List<Plato> obtenerVistaPredefinida() {
        return platos.stream()
                .filter(Plato::isDisponible)
                .filter(Plato::isVisibleEnInicio)
                .collect(Collectors.toList());
    }

    public List<Plato> obtenerPlatosDestacados() {
        return platos.stream()
                .filter(Plato::isDisponible)
                .filter(Plato::isDestacado)
                .collect(Collectors.toList());
    }

    public List<Plato> obtenerPorCategoria(String categoria) {
        return platos.stream()
                .filter(Plato::isDisponible)
                .filter(p -> p.getCategoria().getNombre().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    public Optional<Plato> buscarPorId(Long id) {
        return platos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public Plato actualizarPlato(Long id, Plato actualizado) {
        Plato existente = buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado"));
        existente.setNombre(actualizado.getNombre());
        existente.setDescripcion(actualizado.getDescripcion());
        existente.setPrecio(actualizado.getPrecio());
        existente.setCategoria(actualizado.getCategoria());
        existente.setDisponible(actualizado.isDisponible());
        existente.setDestacado(actualizado.isDestacado());
        existente.setVisibleEnInicio(actualizado.isVisibleEnInicio());
        return existente;
    }

    public void eliminarPlato(Long id) {
        Plato plato = buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado"));
        platos.remove(plato);
    }

    public Plato cambiarDisponibilidad(Long id, boolean disponible) {
        Plato plato = buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado"));
        plato.setDisponible(disponible);
        return plato;
    }

    public Plato agregarPlato(Plato plato) {
        if (plato.getNombre() == null || plato.getNombre().isBlank())
            throw new RuntimeException("El nombre del plato es obligatorio");
        if (plato.getPrecio() <= 0)
            throw new RuntimeException("El precio debe ser mayor que cero");
        if (plato.getCategoria() == null)
            throw new RuntimeException("La categoría es obligatoria");
        long maxId = platos.stream().mapToLong(Plato::getId).max().orElse(0L);
        plato.setId(maxId + 1);
        platos.add(plato);
        return plato;
    }
}

