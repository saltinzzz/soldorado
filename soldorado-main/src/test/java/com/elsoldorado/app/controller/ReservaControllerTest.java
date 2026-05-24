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

import com.elsoldorado.app.model.Reserva;
import com.elsoldorado.app.service.ReservaService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ReservaController.class)
@SuppressWarnings("null")
public class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservaService reservaService;

    @Test
    public void testListarReservas() throws Exception {
        Reserva r = new Reserva();
        doReturn(Arrays.asList(r)).when(reservaService).listarReservas();

        mockMvc.perform(get("/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void testObtenerReservaPorId_CuandoExiste() throws Exception {
        Reserva r = new Reserva();
        doReturn(r).when(reservaService).buscarPorId(1L);

        mockMvc.perform(get("/reservas/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testObtenerReservaPorId_CuandoNoExiste() throws Exception {
        doReturn(null).when(reservaService).buscarPorId(99L);

        mockMvc.perform(get("/reservas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRegistrarReserva() throws Exception {
        Reserva reserva = new Reserva();
        doReturn(reserva).when(reservaService).registrarReserva(any(Reserva.class));

        mockMvc.perform(post("/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reserva)))
                .andExpect(status().isOk());
    }

    @Test
    public void testActualizarReserva() throws Exception {
        Reserva reserva = new Reserva();
        doReturn(reserva).when(reservaService).actualizarReserva(eq(1L), any(Reserva.class));

        mockMvc.perform(put("/reservas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reserva)))
                .andExpect(status().isOk());
    }

    @Test
    public void testEliminarReserva() throws Exception {
        doNothing().when(reservaService).eliminarReserva(1L);

        mockMvc.perform(delete("/reservas/1"))
                .andExpect(status().isOk());
    }
}