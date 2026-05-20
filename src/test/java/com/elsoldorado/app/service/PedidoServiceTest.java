package com.elsoldorado.app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.elsoldorado.app.model.DetallePedido;
import com.elsoldorado.app.model.EstadoPedido;
import com.elsoldorado.app.model.Pedido;
import com.elsoldorado.app.model.Plato;

public class PedidoServiceTest {

    private PedidoService pedidoService;

    @Mock
    private MenuService menuService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // El constructor de PedidoService llama a inicializarPedidosDeEjemplo
        pedidoService = new PedidoService(menuService);
    }

    @Test
    public void testListarPedidos() {
        List<Pedido> resultado = pedidoService.listarPedidos();
        // El servicio inicia con 5 pedidos de ejemplo en el constructor
        assertEquals(5, resultado.size());
    }

    @Test
    public void testRegistrarPedido_Exito() {
        // 1. Preparar datos
        Plato platoMock = new Plato();
        platoMock.setId(1L);
        platoMock.setNombre("Ceviche");
        platoMock.setPrecio(30.0);
        platoMock.setDisponible(true);

        when(menuService.buscarPorId(1L)).thenReturn(Optional.of(platoMock));

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setNombreCliente("Jose");
        nuevoPedido.setTelefono("999888777");
        nuevoPedido.setDireccion("Calle Falsa 123");
        
        List<DetallePedido> detalles = new ArrayList<>();
        detalles.add(new DetallePedido(1L, "", 2, 0.0)); // Cantidad 2
        nuevoPedido.setDetalles(detalles);

        // 2. Act
        Pedido registrado = pedidoService.registrarPedido(nuevoPedido);

        // 3. Assert
        assertNotNull(registrado.getId());
        assertEquals(60.0, registrado.getTotal(), "El total debe ser 30.0 * 2 = 60.0");
        assertEquals(EstadoPedido.PENDIENTE, registrado.getEstado());
    }

    @Test
    public void testRegistrarPedido_ErrorPlatoNoDisponible() {
        Plato platoInactivo = new Plato();
        platoInactivo.setDisponible(false);
        when(menuService.buscarPorId(1L)).thenReturn(Optional.of(platoInactivo));

        Pedido pedido = new Pedido();
        pedido.setNombreCliente("Test");
        pedido.setTelefono("123");
        pedido.setDireccion("Direccion");
        pedido.setDetalles(List.of(new DetallePedido(1L, "", 1, 0.0)));

        assertThrows(RuntimeException.class, () -> {
            pedidoService.registrarPedido(pedido);
        }, "Debería lanzar error si el plato no está disponible");
    }

    @Test
    public void testCambiarEstado() {
        // El ID 1 existe por el inicializador de ejemplo
        Pedido pedido = pedidoService.cambiarEstado(1L, EstadoPedido.EN_CAMINO);
        
        assertEquals(EstadoPedido.EN_CAMINO, pedido.getEstado());
    }

    @Test
    public void testEliminarPedido() {
        pedidoService.eliminarPedido(1L);
        assertEquals(4, pedidoService.listarPedidos().size());
    }
}