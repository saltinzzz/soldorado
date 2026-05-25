package com.elsoldorado.app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
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
import com.elsoldorado.app.repository.PedidoRepository;

public class PedidoServiceTest {

    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private MenuService menuService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // El constructor de PedidoService llama a inicializarPedidosDeEjemplo
        pedidoService = new PedidoService(pedidoRepository, menuService);
    }

    @Test
    public void testListarPedidos() {
        List<Pedido> pedidosMock = new ArrayList<>();

        pedidosMock.add(new Pedido());
        pedidosMock.add(new Pedido());
        pedidosMock.add(new Pedido());
        pedidosMock.add(new Pedido());
        pedidosMock.add(new Pedido());

        when(pedidoRepository.findAll()).thenReturn(pedidosMock);

        List<Pedido> resultado = pedidoService.listarPedidos();

        assertEquals(5, resultado.size());
        verify(pedidoRepository).findAll();
    }

    @Test
    public void testRegistrarPedido_Exito() {
        Plato platoMock = new Plato();
        platoMock.setId(1L);
        platoMock.setNombre("Ceviche");
        platoMock.setPrecio(new BigDecimal("30.00"));
        platoMock.setDisponible(true);

        when(menuService.buscarPorId(1L)).thenReturn(Optional.of(platoMock));

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setNombreCliente("Jose");
        nuevoPedido.setTelefono("999888777");
        nuevoPedido.setDireccion("Calle Falsa 123");

        List<DetallePedido> detalles = new ArrayList<>();
        detalles.add(new DetallePedido(1L, "", 2, BigDecimal.ZERO));
        nuevoPedido.setDetalles(detalles);

        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido pedidoGuardado = invocation.getArgument(0);
            pedidoGuardado.setId(10L);
            return pedidoGuardado;
        });

        Pedido registrado = pedidoService.registrarPedido(nuevoPedido);

        assertNotNull(registrado.getId());
        assertEquals(0, new BigDecimal("60.00").compareTo(registrado.getTotal()));
        assertEquals(EstadoPedido.PENDIENTE, registrado.getEstado());

        verify(menuService).buscarPorId(1L);
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    public void testRegistrarPedido_ErrorPlatoNoDisponible() {
        Plato platoInactivo = new Plato();
        platoInactivo.setId(1L);
        platoInactivo.setNombre("Ceviche");
        platoInactivo.setPrecio(new BigDecimal("30.00"));
        platoInactivo.setDisponible(false);

        when(menuService.buscarPorId(1L)).thenReturn(Optional.of(platoInactivo));

        Pedido pedido = new Pedido();
        pedido.setNombreCliente("Test");
        pedido.setTelefono("123");
        pedido.setDireccion("Direccion");
        pedido.setDetalles(List.of(new DetallePedido(1L, "", 1, BigDecimal.ZERO)));

        assertThrows(RuntimeException.class, () -> {
            pedidoService.registrarPedido(pedido);
        });

        verify(menuService).buscarPorId(1L);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    public void testCambiarEstado() {
        Pedido pedidoExistente = new Pedido();
        pedidoExistente.setId(1L);
        pedidoExistente.setEstado(EstadoPedido.PENDIENTE);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoExistente));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pedido pedido = pedidoService.cambiarEstado(1L, EstadoPedido.EN_CAMINO);

        assertEquals(EstadoPedido.EN_CAMINO, pedido.getEstado());

        verify(pedidoRepository).findById(1L);
        verify(pedidoRepository).save(pedidoExistente);
    }

    @Test
    public void testEliminarPedido() {
        Pedido pedidoExistente = new Pedido();
        pedidoExistente.setId(1L);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoExistente));

        pedidoService.eliminarPedido(1L);

        verify(pedidoRepository).findById(1L);
        verify(pedidoRepository).delete(pedidoExistente);
    }
}