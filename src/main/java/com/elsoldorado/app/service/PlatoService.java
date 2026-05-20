package com.elsoldorado.app.service;
import com.elsoldorado.app.model.Plato;
import org.springframework.stereotype.Service;

@Service
public class PlatoService {
    private final MenuService menuService;

    public PlatoService(MenuService menuService) {
        this.menuService = menuService;
    }

    public Plato crearPlato(Plato plato) {
        if (plato.getNombre() == null || plato.getNombre().isBlank())
            throw new RuntimeException("El nombre del plato es obligatorio");
        if (plato.getPrecio() <= 0)
            throw new RuntimeException("El precio debe ser mayor que cero");
        if (plato.getCategoria() == null)
            throw new RuntimeException("La categoría es obligatoria");
        return menuService.agregarPlato(plato); // método a agregar en MenuService
    }

    public Plato actualizarPlato(Long id, Plato actualizado) {
        Plato existente = menuService.buscarPorId(id)
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

    public void cambiarDisponibilidad(Long id, boolean disponible) {
        Plato plato = menuService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado"));
        plato.setDisponible(disponible);
    }
}