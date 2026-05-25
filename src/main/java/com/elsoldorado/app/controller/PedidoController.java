package com.elsoldorado.app.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.elsoldorado.app.dto.mapper.RestauranteMapper;
import com.elsoldorado.app.dto.request.PedidoRequestDTO;
import com.elsoldorado.app.dto.response.PedidoResponseDTO;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.elsoldorado.app.model.EstadoPedido;
import com.elsoldorado.app.model.Pedido;
import com.elsoldorado.app.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) { this.pedidoService = pedidoService; }

    @GetMapping
    public List<PedidoResponseDTO> listarPedidos() {
        return pedidoService.listarPedidos().stream().map(RestauranteMapper::toPedidoResponse).toList();
    }

    @GetMapping("/{id}")
    public PedidoResponseDTO obtenerPedidoPorId(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPorId(id);
        if (pedido == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado");
        return RestauranteMapper.toPedidoResponse(pedido);
    }

    @GetMapping("/buscar")
    public List<PedidoResponseDTO> buscarPorCliente(@RequestParam String cliente) {
        return pedidoService.buscarPorCliente(cliente).stream().map(RestauranteMapper::toPedidoResponse).toList();
    }

    @GetMapping("/estado/{estado}/desde")
    public List<PedidoResponseDTO> buscarPorEstadoDesde(@PathVariable EstadoPedido estado,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHora) {
        return pedidoService.buscarPorEstadoDesde(estado, fechaHora).stream().map(RestauranteMapper::toPedidoResponse).toList();
    }

    @GetMapping("/rango-fechas")
    public List<PedidoResponseDTO> buscarPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        return pedidoService.buscarPorRangoFechas(desde, hasta).stream().map(RestauranteMapper::toPedidoResponse).toList();
    }

    @GetMapping("/total-minimo")
    public List<PedidoResponseDTO> buscarPorTotalMinimo(@RequestParam BigDecimal monto) {
        return pedidoService.buscarConTotalMayorOIgual(monto).stream().map(RestauranteMapper::toPedidoResponse).toList();
    }

    @PatchMapping("/{id}/estado")
    public PedidoResponseDTO cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String estado = body.get("estado");
        if (estado == null || estado.isBlank()) throw new IllegalArgumentException("El campo estado es obligatorio");
        EstadoPedido nuevoEstado = EstadoPedido.valueOf(estado);
        return RestauranteMapper.toPedidoResponse(pedidoService.cambiarEstado(id, nuevoEstado));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponseDTO registrarPedido(@Valid @RequestBody PedidoRequestDTO pedido) {
        return RestauranteMapper.toPedidoResponse(pedidoService.registrarPedido(pedido));
    }

    @PutMapping("/{id}")
    public PedidoResponseDTO actualizarPedido(@PathVariable Long id, @Valid @RequestBody PedidoRequestDTO pedido) {
        return RestauranteMapper.toPedidoResponse(pedidoService.actualizarPedido(id, pedido));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarPedido(@PathVariable Long id) { pedidoService.eliminarPedido(id); }
}
