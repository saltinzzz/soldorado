package com.elsoldorado.app.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.elsoldorado.app.model.Plato;
import com.elsoldorado.app.service.MenuService;
import java.util.Map;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public List<Plato> obtenerMenuCompleto() {
        return menuService.obtenerMenuCompleto();
    }

    @GetMapping("/{id}")
    public Plato obtenerPlatoPorId(@PathVariable Long id) {
        return menuService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plato no encontrado"));
    }

    @GetMapping("/inicio")
    public List<Plato> obtenerVistaPredefinida() {
        return menuService.obtenerVistaPredefinida();
    }

    @GetMapping("/destacados")
    public List<Plato> obtenerPlatosDestacados() {
        return menuService.obtenerPlatosDestacados();
    }

    @GetMapping("/categoria/{nombre}")
    public List<Plato> obtenerPorCategoria(@PathVariable String nombre) {
        return menuService.obtenerPorCategoria(nombre);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Plato agregarPlato(@RequestBody Plato plato) {
        return menuService.agregarPlato(plato);
    }

    @PutMapping("/{id}")
    public Plato actualizarPlato(@PathVariable Long id, @RequestBody Plato plato) {
        return menuService.actualizarPlato(id, plato);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarPlato(@PathVariable Long id) {
        menuService.eliminarPlato(id);
    }

    @PatchMapping("/{id}/disponibilidad")
    public Plato cambiarDisponibilidad(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        boolean disponible = body.get("disponible");
        return menuService.cambiarDisponibilidad(id, disponible);   
    }

}
