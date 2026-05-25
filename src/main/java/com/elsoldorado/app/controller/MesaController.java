package com.elsoldorado.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.elsoldorado.app.model.Mesa;
import com.elsoldorado.app.service.MesaService;

@RestController
@RequestMapping("/mesas")
public class MesaController {

    private final MesaService mesaService;

    public MesaController(MesaService mesaService) { this.mesaService = mesaService; }

    @GetMapping
    public List<Mesa> listarMesas() { return mesaService.listarMesas(); }

    @GetMapping("/disponibles")
    public List<Mesa> listarDisponibles() { return mesaService.listarDisponibles(); }

    @GetMapping("/disponibles/capacidad/{capacidad}")
    public List<Mesa> listarDisponiblesPorCapacidad(@PathVariable int capacidad) { return mesaService.buscarDisponiblesPorCapacidad(capacidad); }

    @GetMapping("/{id}")
    public Mesa obtenerPorId(@PathVariable Long id) {
        return mesaService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mesa no encontrada"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mesa crearMesa(@RequestBody Mesa mesa) { return mesaService.crearMesa(mesa); }

    @PatchMapping("/{id}/disponibilidad")
    public Mesa cambiarDisponibilidad(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        boolean disponible = body.get("disponible");
        return mesaService.cambiarDisponibilidad(id, disponible);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarMesa(@PathVariable Long id) { mesaService.eliminarMesa(id); }
}
