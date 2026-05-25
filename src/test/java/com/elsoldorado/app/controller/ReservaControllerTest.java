package com.elsoldorado.app.controller;

import com.elsoldorado.app.dto.request.ReservaRequestDTO;
import com.elsoldorado.app.model.EstadoReserva;
import com.elsoldorado.app.model.Reserva;
import com.elsoldorado.app.service.ReservaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservaController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReservaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservaService reservaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarReservas() throws Exception {
        when(reservaService.listarReservas()).thenReturn(List.of(reservaEjemplo()));

        mockMvc.perform(get("/reservas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreCliente").value("Maria"));
    }

    @Test
    void registrarReserva() throws Exception {
        when(reservaService.registrarReserva(any(ReservaRequestDTO.class))).thenReturn(reservaEjemplo());

        ReservaRequestDTO request = new ReservaRequestDTO();
        request.setNombreCliente("Maria");
        request.setTelefono("999111222");
        request.setFecha(LocalDate.now().plusDays(1));
        request.setHora(LocalTime.of(15, 0));
        request.setCantidadPersonas(4);

        mockMvc.perform(post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    private Reserva reservaEjemplo() {
        Reserva reserva = new Reserva();
        reserva.setId(1L);
        reserva.setNombreCliente("Maria");
        reserva.setTelefono("999111222");
        reserva.setFecha(LocalDate.now().plusDays(1));
        reserva.setHora(LocalTime.of(15, 0));
        reserva.setCantidadPersonas(4);
        reserva.setEstado(EstadoReserva.PENDIENTE);
        return reserva;
    }
}
