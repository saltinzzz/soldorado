package com.elsoldorado.app.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.elsoldorado.app.dto.mapper.RestauranteMapper;
import com.elsoldorado.app.dto.response.PlatoResponseDTO;
import com.elsoldorado.app.model.Plato;
import com.elsoldorado.app.service.MenuService;

@RestController
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) { this.menuService = menuService; }

    @GetMapping
    public List<PlatoResponseDTO> obtenerMenuCompleto() {
        return menuService.obtenerMenuCompleto().stream().map(RestauranteMapper::toPlatoResponse).toList();
    }

    @GetMapping("/{id}")
    public PlatoResponseDTO obtenerPlatoPorId(@PathVariable Long id) {
        return menuService.buscarPorId(id)
                .map(RestauranteMapper::toPlatoResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plato no encontrado"));
    }

    @GetMapping("/inicio")
    public List<PlatoResponseDTO> obtenerVistaPredefinida() {
        return menuService.obtenerVistaPredefinida().stream().map(RestauranteMapper::toPlatoResponse).toList();
    }

    @GetMapping("/destacados")
    public List<PlatoResponseDTO> obtenerPlatosDestacados() {
        return menuService.obtenerPlatosDestacados().stream().map(RestauranteMapper::toPlatoResponse).toList();
    }

    @GetMapping("/categoria/{nombre}")
    public List<PlatoResponseDTO> obtenerPorCategoria(@PathVariable String nombre) {
        return menuService.obtenerPorCategoria(nombre).stream().map(RestauranteMapper::toPlatoResponse).toList();
    }

    @GetMapping("/buscar")
    public List<PlatoResponseDTO> buscar(@RequestParam String texto) {
        return menuService.buscarPorTexto(texto).stream().map(RestauranteMapper::toPlatoResponse).toList();
    }

    @GetMapping("/precio")
    public List<PlatoResponseDTO> buscarPorPrecio(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        return menuService.buscarPorRangoPrecio(min, max).stream().map(RestauranteMapper::toPlatoResponse).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlatoResponseDTO agregarPlato(@RequestBody Plato plato) {
        return RestauranteMapper.toPlatoResponse(menuService.agregarPlato(plato));
    }

    @PutMapping("/{id}")
    public PlatoResponseDTO actualizarPlato(@PathVariable Long id, @RequestBody Plato plato) {
        return RestauranteMapper.toPlatoResponse(menuService.actualizarPlato(id, plato));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarPlato(@PathVariable Long id) { menuService.eliminarPlato(id); }

    @PatchMapping("/{id}/disponibilidad")
    public PlatoResponseDTO cambiarDisponibilidad(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        Boolean disponible = body.get("disponible");
        if (disponible == null) throw new IllegalArgumentException("El campo disponible es obligatorio");
        return RestauranteMapper.toPlatoResponse(menuService.cambiarDisponibilidad(id, disponible));
    }
}
