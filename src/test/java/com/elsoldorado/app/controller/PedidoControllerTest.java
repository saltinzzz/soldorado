package com.elsoldorado.app.controller;

import com.elsoldorado.app.dto.request.DetalleRequestDTO;
import com.elsoldorado.app.dto.request.PedidoRequestDTO;
import com.elsoldorado.app.model.DetallePedido;
import com.elsoldorado.app.model.EstadoPedido;
import com.elsoldorado.app.model.Pedido;
import com.elsoldorado.app.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PedidoController.class)
@AutoConfigureMockMvc(addFilters = false)
@SuppressWarnings("null")
class PedidoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarPedidos() throws Exception {
        when(pedidoService.listarPedidos()).thenReturn(List.of(pedidoEjemplo()));

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreCliente").value("Jose"));
    }

    @Test
    void registrarPedido() throws Exception {
        when(pedidoService.registrarPedido(any(PedidoRequestDTO.class))).thenReturn(pedidoEjemplo());

        PedidoRequestDTO request = new PedidoRequestDTO();
        request.setNombreCliente("Jose");
        request.setTelefono("999888777");
        request.setDireccion("Av. Siempre Viva");
        DetalleRequestDTO detalle = new DetalleRequestDTO();
        detalle.setIdPlato(1L);
        detalle.setCantidad(2);
        request.setDetalles(List.of(detalle));

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.total").value(64.00));
    }

    private Pedido pedidoEjemplo() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setNombreCliente("Jose");
        pedido.setTelefono("999888777");
        pedido.setDireccion("Av. Siempre Viva");
        pedido.setFechaHora(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        DetallePedido detalle = new DetallePedido(1L, "Ceviche", 2, new BigDecimal("32.00"));
        pedido.setDetalles(List.of(detalle));
        pedido.setTotal(new BigDecimal("64.00"));
        return pedido;
    }
}
