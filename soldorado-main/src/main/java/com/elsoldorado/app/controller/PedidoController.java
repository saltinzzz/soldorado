package com.elsoldorado.app.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    public List<Pedido> listarPedidos() { return pedidoService.listarPedidos(); }

    @GetMapping("/{id}")
    public Pedido obtenerPedidoPorId(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPorId(id);
        if (pedido == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado");
        return pedido;
    }

    @GetMapping("/buscar")
    public List<Pedido> buscarPorCliente(@RequestParam String cliente) { return pedidoService.buscarPorCliente(cliente); }

    @GetMapping("/estado/{estado}/desde")
    public List<Pedido> buscarPorEstadoDesde(@PathVariable EstadoPedido estado, @RequestParam LocalDateTime fechaHora) {
        return pedidoService.buscarPorEstadoDesde(estado, fechaHora);
    }

    @PatchMapping("/{id}/estado")
    public Pedido cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        EstadoPedido nuevoEstado = EstadoPedido.valueOf(body.get("estado"));
        return pedidoService.cambiarEstado(id, nuevoEstado);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido registrarPedido(@RequestBody Pedido pedido) { return pedidoService.registrarPedido(pedido); }

    @PutMapping("/{id}")
    public Pedido actualizarPedido(@PathVariable Long id, @RequestBody Pedido pedido) { return pedidoService.actualizarPedido(id, pedido); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarPedido(@PathVariable Long id) { pedidoService.eliminarPedido(id); }
}
