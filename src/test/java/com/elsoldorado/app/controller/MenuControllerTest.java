package com.elsoldorado.app.controller;

import com.elsoldorado.app.model.Plato;
import com.elsoldorado.app.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuController.class)
@AutoConfigureMockMvc(addFilters = false)
class MenuControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MenuService menuService;

    @Test
    void obtenerMenuCompleto() throws Exception {
        Plato plato = new Plato();
        plato.setId(1L);
        plato.setNombre("Ceviche");
        plato.setPrecio(new BigDecimal("32.00"));
        plato.setDisponible(true);

        when(menuService.obtenerMenuCompleto()).thenReturn(List.of(plato));

        mockMvc.perform(get("/menu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Ceviche"));
    }

    @Test
    void obtenerPlatoPorId() throws Exception {
        Plato plato = new Plato();
        plato.setId(1L);
        plato.setNombre("Ceviche");
        plato.setPrecio(new BigDecimal("32.00"));
        plato.setDisponible(true);

        when(menuService.buscarPorId(1L)).thenReturn(Optional.of(plato));

        mockMvc.perform(get("/menu/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ceviche"));
    }
}
