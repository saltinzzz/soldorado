package com.elsoldorado.app.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.elsoldorado.app.dto.mapper.RestauranteMapper;
import com.elsoldorado.app.dto.request.ReservaRequestDTO;
import com.elsoldorado.app.dto.response.ReservaResponseDTO;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.elsoldorado.app.model.EstadoReserva;
import com.elsoldorado.app.model.Reserva;
import com.elsoldorado.app.service.ReservaService;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) { this.reservaService = reservaService; }

    @GetMapping
    public List<ReservaResponseDTO> listarReservas() {
        return reservaService.listarReservas().stream().map(RestauranteMapper::toReservaResponse).toList();
    }

    @GetMapping("/{id}")
    public ReservaResponseDTO obtenerReservaPorId(@PathVariable Long id) {
        Reserva reserva = reservaService.buscarPorId(id);
        if (reserva == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada");
        return RestauranteMapper.toReservaResponse(reserva);
    }

    @GetMapping("/proximas")
    public List<ReservaResponseDTO> buscarProximas(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde) {
        return reservaService.buscarProximas(desde).stream().map(RestauranteMapper::toReservaResponse).toList();
    }

    @GetMapping("/buscar")
    public List<ReservaResponseDTO> buscarPorCliente(@RequestParam String cliente) {
        return reservaService.buscarPorCliente(cliente).stream().map(RestauranteMapper::toReservaResponse).toList();
    }

    @GetMapping("/rango-fechas")
    public List<ReservaResponseDTO> buscarPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return reservaService.buscarPorRangoFechas(desde, hasta).stream().map(RestauranteMapper::toReservaResponse).toList();
    }

    @PatchMapping("/{id}/estado")
    public ReservaResponseDTO cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String estado = body.get("estado");
        if (estado == null || estado.isBlank()) throw new IllegalArgumentException("El campo estado es obligatorio");
        EstadoReserva nuevoEstado = EstadoReserva.valueOf(estado);
        return RestauranteMapper.toReservaResponse(reservaService.cambiarEstado(id, nuevoEstado));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservaResponseDTO registrarReserva(@Valid @RequestBody ReservaRequestDTO reserva) {
        return RestauranteMapper.toReservaResponse(reservaService.registrarReserva(reserva));
    }

    @PutMapping("/{id}")
    public ReservaResponseDTO actualizarReserva(@PathVariable Long id, @Valid @RequestBody ReservaRequestDTO reserva) {
        return RestauranteMapper.toReservaResponse(reservaService.actualizarReserva(id, reserva));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarReserva(@PathVariable Long id) { reservaService.eliminarReserva(id); }
}
