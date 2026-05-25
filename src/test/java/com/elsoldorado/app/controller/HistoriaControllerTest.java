package com.elsoldorado.app.controller;

import com.elsoldorado.app.service.HistoriaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HistoriaController.class)
@AutoConfigureMockMvc(addFilters = false)
class HistoriaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistoriaService historiaService;

    @Test
    void obtenerHistoria() throws Exception {
        when(historiaService.obtenerHistoria()).thenReturn("Historia del restaurante");
        mockMvc.perform(get("/historia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.historia").value("Historia del restaurante"));
    }
}
