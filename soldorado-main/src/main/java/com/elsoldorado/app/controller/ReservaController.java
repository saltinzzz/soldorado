package com.elsoldorado.app.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    public List<Reserva> listarReservas() { return reservaService.listarReservas(); }

    @GetMapping("/{id}")
    public Reserva obtenerReservaPorId(@PathVariable Long id) {
        Reserva reserva = reservaService.buscarPorId(id);
        if (reserva == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada");
        return reserva;
    }

    @GetMapping("/proximas")
    public List<Reserva> buscarProximas(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde) {
        return reservaService.buscarProximas(desde);
    }

    @GetMapping("/buscar")
    public List<Reserva> buscarPorCliente(@RequestParam String cliente) { return reservaService.buscarPorCliente(cliente); }

    @PatchMapping("/{id}/estado")
    public Reserva cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        EstadoReserva nuevoEstado = EstadoReserva.valueOf(body.get("estado"));
        return reservaService.cambiarEstado(id, nuevoEstado);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reserva registrarReserva(@RequestBody Reserva reserva) { return reservaService.registrarReserva(reserva); }

    @PutMapping("/{id}")
    public Reserva actualizarReserva(@PathVariable Long id, @RequestBody Reserva reserva) { return reservaService.actualizarReserva(id, reserva); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarReserva(@PathVariable Long id) { reservaService.eliminarReserva(id); }
}
