package com.elsoldorado.app.service;

import com.elsoldorado.app.model.Plato;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PlatoService {
    private final MenuService menuService;

    public PlatoService(MenuService menuService) {
        this.menuService = menuService;
    }

    public Plato crearPlato(Plato plato) {
        validarPlato(plato);
        return menuService.agregarPlato(plato);
    }

    public Plato actualizarPlato(Long id, Plato actualizado) {
        validarPlato(actualizado);
        return menuService.actualizarPlato(id, actualizado);
    }

    public Plato cambiarDisponibilidad(Long id, boolean disponible) {
        return menuService.cambiarDisponibilidad(id, disponible);
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
