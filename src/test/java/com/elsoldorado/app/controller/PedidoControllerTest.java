package com.elsoldorado.app.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.elsoldorado.app.model.Pedido;
import com.elsoldorado.app.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PedidoController.class)
@SuppressWarnings("null")
public class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PedidoService pedidoService;

    @Test
    public void testListarPedidos() throws Exception {
        Pedido p = new Pedido();
        // Asume que tienes un campo id o cliente, ajústalo según tu modelo Pedido
        doReturn(Arrays.asList(p)).when(pedidoService).listarPedidos();

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testObtenerPedidoPorId_CuandoExiste() throws Exception {
        Pedido p = new Pedido();
        doReturn(p).when(pedidoService).buscarPorId(1L);

        mockMvc.perform(get("/pedidos/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testObtenerPedidoPorId_CuandoNoExiste() throws Exception {
        doReturn(null).when(pedidoService).buscarPorId(99L);

        mockMvc.perform(get("/pedidos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRegistrarPedido() throws Exception {
        Pedido pedido = new Pedido();
        doReturn(pedido).when(pedidoService).registrarPedido(any(Pedido.class));

        mockMvc.perform(post("/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isOk());
    }

    @Test
    public void testActualizarPedido() throws Exception {
        Pedido pedido = new Pedido();
        doReturn(pedido).when(pedidoService).actualizarPedido(eq(1L), any(Pedido.class));

        mockMvc.perform(put("/pedidos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isOk());
    }

    @Test
    public void testEliminarPedido() throws Exception {
        doNothing().when(pedidoService).eliminarPedido(1L);

        mockMvc.perform(delete("/pedidos/1"))
                .andExpect(status().isOk());
    }
}