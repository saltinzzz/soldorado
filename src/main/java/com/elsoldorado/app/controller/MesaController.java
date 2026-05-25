package com.elsoldorado.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.elsoldorado.app.dto.mapper.RestauranteMapper;
import com.elsoldorado.app.dto.response.MesaResponseDTO;
import com.elsoldorado.app.model.Mesa;
import com.elsoldorado.app.service.MesaService;

@RestController
@RequestMapping("/mesas")
public class MesaController {

    private final MesaService mesaService;

    public MesaController(MesaService mesaService) { this.mesaService = mesaService; }

    @GetMapping
    public List<MesaResponseDTO> listarMesas() {
        return mesaService.listarMesas().stream().map(RestauranteMapper::toMesaResponse).toList();
    }

    @GetMapping("/disponibles")
    public List<MesaResponseDTO> listarDisponibles() {
        return mesaService.listarDisponibles().stream().map(RestauranteMapper::toMesaResponse).toList();
    }

    @GetMapping("/disponibles/capacidad/{capacidad}")
    public List<MesaResponseDTO> listarDisponiblesPorCapacidad(@PathVariable int capacidad) {
        return mesaService.buscarDisponiblesPorCapacidad(capacidad).stream().map(RestauranteMapper::toMesaResponse).toList();
    }

    @GetMapping("/{id}")
    public MesaResponseDTO obtenerPorId(@PathVariable Long id) {
        return mesaService.buscarPorId(id)
                .map(RestauranteMapper::toMesaResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mesa no encontrada"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MesaResponseDTO crearMesa(@RequestBody Mesa mesa) {
        return RestauranteMapper.toMesaResponse(mesaService.crearMesa(mesa));
    }

    @PatchMapping("/{id}/disponibilidad")
    public MesaResponseDTO cambiarDisponibilidad(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        Boolean disponible = body.get("disponible");
        if (disponible == null) throw new IllegalArgumentException("El campo disponible es obligatorio");
        return RestauranteMapper.toMesaResponse(mesaService.cambiarDisponibilidad(id, disponible));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarMesa(@PathVariable Long id) { mesaService.eliminarMesa(id); }
}
