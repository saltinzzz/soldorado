package com.elsoldorado.app.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.elsoldorado.app.model.Plato;
import com.elsoldorado.app.service.MenuService;

@RestController
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) { this.menuService = menuService; }

    @GetMapping
    public List<Plato> obtenerMenuCompleto() { return menuService.obtenerMenuCompleto(); }

    @GetMapping("/{id}")
    public Plato obtenerPlatoPorId(@PathVariable Long id) {
        return menuService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plato no encontrado"));
    }

    @GetMapping("/inicio")
    public List<Plato> obtenerVistaPredefinida() { return menuService.obtenerVistaPredefinida(); }

    @GetMapping("/destacados")
    public List<Plato> obtenerPlatosDestacados() { return menuService.obtenerPlatosDestacados(); }

    @GetMapping("/categoria/{nombre}")
    public List<Plato> obtenerPorCategoria(@PathVariable String nombre) { return menuService.obtenerPorCategoria(nombre); }

    @GetMapping("/buscar")
    public List<Plato> buscar(@RequestParam String texto) { return menuService.buscarPorTexto(texto); }

    @GetMapping("/precio")
    public List<Plato> buscarPorPrecio(@RequestParam BigDecimal min, @RequestParam BigDecimal max) { return menuService.buscarPorRangoPrecio(min, max); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Plato agregarPlato(@RequestBody Plato plato) { return menuService.agregarPlato(plato); }

    @PutMapping("/{id}")
    public Plato actualizarPlato(@PathVariable Long id, @RequestBody Plato plato) { return menuService.actualizarPlato(id, plato); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarPlato(@PathVariable Long id) { menuService.eliminarPlato(id); }

    @PatchMapping("/{id}/disponibilidad")
    public Plato cambiarDisponibilidad(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        boolean disponible = body.get("disponible");
        return menuService.cambiarDisponibilidad(id, disponible);
    }
}
