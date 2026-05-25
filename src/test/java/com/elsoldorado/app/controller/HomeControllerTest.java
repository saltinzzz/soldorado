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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HomeController.class)
@AutoConfigureMockMvc(addFilters = false)
class HomeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MenuService menuService;

    @Test
    void inicio() throws Exception {
        Plato plato = new Plato();
        plato.setId(1L);
        plato.setNombre("Lomo Saltado");
        plato.setPrecio(new BigDecimal("28.00"));
        plato.setDisponible(true);

        when(menuService.obtenerVistaPredefinida()).thenReturn(List.of(plato));
        when(menuService.obtenerPlatosDestacados()).thenReturn(List.of(plato));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Bienvenido al restaurante de comida peruana"))
                .andExpect(jsonPath("$.menuPredefinido[0].nombre").value("Lomo Saltado"));
    }
}
