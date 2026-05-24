package com.elsoldorado.app.service;

import com.elsoldorado.app.model.DetallePedido;
import com.elsoldorado.app.model.EstadoPedido;
import com.elsoldorado.app.model.Pedido;
import com.elsoldorado.app.model.Plato;
import com.elsoldorado.app.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final MenuService menuService;

    public PedidoService(PedidoRepository pedidoRepository, MenuService menuService) {
        this.pedidoRepository = pedidoRepository;
        this.menuService = menuService;
    }

    public List<Pedido> listarPedidos() { return pedidoRepository.findAll(); }
    public Pedido buscarPorId(Long id) { return pedidoRepository.findById(id).orElse(null); }
    public List<Pedido> buscarPorCliente(String cliente) { return pedidoRepository.buscarPorCliente(cliente); }
    public List<Pedido> buscarPorEstadoDesde(EstadoPedido estado, LocalDateTime fechaHora) { return pedidoRepository.buscarPorEstadoDesde(estado, fechaHora); }

    public Pedido registrarPedido(Pedido pedido) {
        validarPedido(pedido);
        prepararDetallesYTotal(pedido);
        pedido.setFechaHora(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        return pedidoRepository.save(pedido);
    }

    public Pedido cambiarEstado(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    public Pedido actualizarPedido(Long id, Pedido pedidoActualizado) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        validarPedido(pedidoActualizado);
        pedidoExistente.setNombreCliente(pedidoActualizado.getNombreCliente());
        pedidoExistente.setTelefono(pedidoActualizado.getTelefono());
        pedidoExistente.setDireccion(pedidoActualizado.getDireccion());
        pedidoExistente.setReferencia(pedidoActualizado.getReferencia());
        pedidoExistente.getDetalles().clear();
        pedidoExistente.getDetalles().addAll(pedidoActualizado.getDetalles());
        prepararDetallesYTotal(pedidoExistente);
        if (pedidoActualizado.getEstado() != null) pedidoExistente.setEstado(pedidoActualizado.getEstado());
        return pedidoRepository.save(pedidoExistente);
    }

    public void eliminarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedidoRepository.delete(pedido);
    }

    private void prepararDetallesYTotal(Pedido pedido) {
        BigDecimal total = BigDecimal.ZERO;
        List<DetallePedido> detallesValidados = new ArrayList<>();
        for (DetallePedido detalle : pedido.getDetalles()) {
            if (detalle.getCantidad() <= 0) throw new RuntimeException("La cantidad del plato debe ser mayor que cero");
            Plato plato = menuService.buscarPorId(detalle.getIdPlato())
                    .orElseThrow(() -> new RuntimeException("Plato no encontrado: " + detalle.getIdPlato()));
            if (!plato.isDisponible()) throw new RuntimeException("El plato no está disponible: " + plato.getNombre());
            DetallePedido detalleNuevo = new DetallePedido(plato.getId(), plato.getNombre(), detalle.getCantidad(), plato.getPrecio());
            detallesValidados.add(detalleNuevo);
            total = total.add(detalleNuevo.getSubtotal());
        }
        pedido.setDetalles(detallesValidados);
        pedido.setTotal(total);
    }

    private void validarPedido(Pedido pedido) {
        if (pedido.getNombreCliente() == null || pedido.getNombreCliente().isBlank()) throw new RuntimeException("El nombre del cliente es obligatorio");
        if (pedido.getTelefono() == null || pedido.getTelefono().isBlank()) throw new RuntimeException("El teléfono es obligatorio");
        if (pedido.getDireccion() == null || pedido.getDireccion().isBlank()) throw new RuntimeException("La dirección es obligatoria");
        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) throw new RuntimeException("El pedido debe contener al menos un plato");
    }
}
