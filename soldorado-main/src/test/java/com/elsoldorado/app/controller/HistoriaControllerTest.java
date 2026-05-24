package com.elsoldorado.app.controller;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.elsoldorado.app.service.HistoriaService;

@WebMvcTest(HistoriaController.class)
@SuppressWarnings("null") // Para evitar las advertencias de Null safety que vimos antes
public class HistoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // Usamos la nueva anotación de Spring Boot 3.5+
    private HistoriaService historiaService;

    @Test
    public void testObtenerHistoria() throws Exception {
        // Arrange: Preparamos el mapa que esperamos recibir
        Map<String, String> historiaMock = Map.of("historia", "Bienvenidos a El Sol Dorado, fundado en 1995...");
        
        doReturn(historiaMock).when(historiaService).obtenerHistoria();

        // Act & Assert
        mockMvc.perform(get("/historia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.historia").value("Bienvenidos a El Sol Dorado, fundado en 1995..."));
    }
}