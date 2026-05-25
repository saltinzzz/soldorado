package com.elsoldorado.app.service;

import com.elsoldorado.app.model.DetallePedido;
import com.elsoldorado.app.model.EstadoPedido;
import com.elsoldorado.app.model.Pedido;
import com.elsoldorado.app.model.Plato;
import com.elsoldorado.app.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class PedidoServiceTest {
    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private MenuService menuService;

    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        pedidoService = new PedidoService(pedidoRepository, menuService);
    }

    @Test
    void registrarPedidoExito() {
        Plato plato = new Plato();
        plato.setId(1L);
        plato.setNombre("Ceviche");
        plato.setPrecio(new BigDecimal("32.00"));
        plato.setDisponible(true);
        when(menuService.buscarPorId(1L)).thenReturn(Optional.of(plato));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido pedido = invocation.getArgument(0);
            pedido.setId(10L);
            return pedido;
        });

        Pedido pedido = pedidoBase();
        Pedido registrado = pedidoService.registrarPedido(pedido);

        assertEquals(new BigDecimal("64.00"), registrado.getTotal());
        assertEquals(EstadoPedido.PENDIENTE, registrado.getEstado());
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    void registrarPedidoConPlatoNoDisponible() {
        Plato plato = new Plato();
        plato.setId(1L);
        plato.setNombre("Ceviche");
        plato.setPrecio(new BigDecimal("32.00"));
        plato.setDisponible(false);
        when(menuService.buscarPorId(1L)).thenReturn(Optional.of(plato));

        assertThrows(IllegalArgumentException.class, () -> pedidoService.registrarPedido(pedidoBase()));
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    private Pedido pedidoBase() {
        Pedido pedido = new Pedido();
        pedido.setNombreCliente("Jose");
        pedido.setTelefono("999888777");
        pedido.setDireccion("Av. Siempre Viva");
        pedido.setDetalles(List.of(new DetallePedido(1L, "", 2, BigDecimal.ZERO)));
        return pedido;
    }
}
