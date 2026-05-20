package com.elsoldorado.app.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.elsoldorado.app.model.DetallePedido;
import com.elsoldorado.app.model.EstadoPedido;
import com.elsoldorado.app.model.Pedido;
import com.elsoldorado.app.model.Plato;

@Service
public class PedidoService {
    private final List<Pedido> pedidos = new ArrayList<>();
    private final MenuService menuService;
    private Long secuencia = 1L;

    public PedidoService(MenuService menuService) {
        this.menuService = menuService;
        inicializarPedidosDeEjemplo();
    }

    private void inicializarPedidosDeEjemplo() {
        pedidos.add(new Pedido(1L, "Ana Gómez", "987654321", "Av. Perú 123", "Frente al mercado",
                List.of(new DetallePedido(1L, "Ceviche", 1, 32.00), new DetallePedido(5L, "Chicha Morada", 1, 8.00)),
                40.00, LocalDateTime.now().minusHours(3), EstadoPedido.ENTREGADO));

        pedidos.add(new Pedido(2L, "Juan Pérez", "912345678", "Jr. Lima 456", "Casa amarilla",
                List.of(new DetallePedido(2L, "Papa a la Huancaína", 2, 18.00)),
                36.00, LocalDateTime.now().minusDays(2), EstadoPedido.EN_CAMINO));

        pedidos.add(new Pedido(3L, "María Ruiz", "999888777", "Calle 10 #100", "Cerca al parque",
                List.of(new DetallePedido(3L, "Lomo Saltado", 1, 28.00), new DetallePedido(4L, "Ají de Gallina", 1, 24.00)),
                52.00, LocalDateTime.now().minusDays(1), EstadoPedido.EN_PREPARACION));

        pedidos.add(new Pedido(4L, "Carlos Díaz", "988776655", "Urbanización Los Jardines", "Portón verde",
                List.of(new DetallePedido(5L, "Chicha Morada", 2, 8.00)),
                16.00, LocalDateTime.now().minusHours(6), EstadoPedido.PENDIENTE));

        pedidos.add(new Pedido(5L, "Lucía Méndez", "911223344", "Av. Arequipa 789", "Al lado de la iglesia",
                List.of(new DetallePedido(1L, "Ceviche", 1, 32.00), new DetallePedido(6L, "Suspiro a la Limeña", 1, 14.00)),
                46.00, LocalDateTime.now().minusMinutes(30), EstadoPedido.CANCELADO));

        secuencia = 6L;
    }

    public List<Pedido> listarPedidos() {
        return pedidos;
    }

    public Pedido buscarPorId(Long id) {
        return pedidos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Pedido registrarPedido(Pedido pedido) {
        validarPedido(pedido);

        double total = 0.0;
        List<DetallePedido> detallesValidados = new ArrayList<>();

        for (DetallePedido detalle : pedido.getDetalles()) {
            if (detalle.getCantidad() <= 0) {
                throw new RuntimeException("La cantidad del plato debe ser mayor que cero");
            }

            Plato plato = menuService.buscarPorId(detalle.getIdPlato())
                    .orElseThrow(() -> new RuntimeException("Plato no encontrado: " + detalle.getIdPlato()));

            if (!plato.isDisponible()) {
                throw new RuntimeException("El plato no está disponible: " + plato.getNombre());
            }

            DetallePedido detalleNuevo = new DetallePedido(
                    plato.getId(),
                    plato.getNombre(),
                    detalle.getCantidad(),
                    plato.getPrecio()
            );

            detallesValidados.add(detalleNuevo);
            total += detalleNuevo.getSubtotal();
        }

        pedido.setId(secuencia++);
        pedido.setDetalles(detallesValidados);
        pedido.setTotal(total);
        pedido.setFechaHora(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);

        pedidos.add(pedido);
        return pedido;
    }

    public Pedido cambiarEstado(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedido.setEstado(nuevoEstado);
        return pedido;
    }

    public Pedido actualizarPedido(Long id, Pedido pedidoActualizado) {
        Pedido pedidoExistente = pedidos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Validar el pedido actualizado
        validarPedido(pedidoActualizado);

        // Actualizar campos (mantener id, fechaHora, total calculado)
        pedidoExistente.setNombreCliente(pedidoActualizado.getNombreCliente());
        pedidoExistente.setTelefono(pedidoActualizado.getTelefono());
        pedidoExistente.setDireccion(pedidoActualizado.getDireccion());
        pedidoExistente.setReferencia(pedidoActualizado.getReferencia());
        pedidoExistente.setDetalles(pedidoActualizado.getDetalles());
        pedidoExistente.setEstado(pedidoActualizado.getEstado());

        // Recalcular total basado en detalles
        double total = pedidoExistente.getDetalles().stream()
                .mapToDouble(DetallePedido::getSubtotal)
                .sum();
        pedidoExistente.setTotal(total);

        return pedidoExistente;
    }

    public void eliminarPedido(Long id) {
        Pedido pedido = pedidos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedidos.remove(pedido);
    }

    private void validarPedido(Pedido pedido) {
        if (pedido.getNombreCliente() == null || pedido.getNombreCliente().isBlank()) {
            throw new RuntimeException("El nombre del cliente es obligatorio");
        }

        if (pedido.getTelefono() == null || pedido.getTelefono().isBlank()) {
            throw new RuntimeException("El teléfono es obligatorio");
        }

        if (pedido.getDireccion() == null || pedido.getDireccion().isBlank()) {
            throw new RuntimeException("La dirección es obligatoria");
        }

        if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
            throw new RuntimeException("El pedido debe contener al menos un plato");
        }
    }
}
