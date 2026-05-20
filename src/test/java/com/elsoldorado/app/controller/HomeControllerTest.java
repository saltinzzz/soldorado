package com.elsoldorado.app.controller;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.elsoldorado.app.service.MenuService;

@WebMvcTest(HomeController.class)
@SuppressWarnings("null")
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MenuService menuService;

    @Test
    public void testInicio() throws Exception {
        // Arrange: Simulamos las respuestas del MenuService
        List<String> platosMock = Arrays.asList("Lomo Saltado", "Cebiche");
        String vistaMock = "Vista General";

      doReturn(vistaMock).when(menuService).obtenerVistaPredefinida();
    doReturn(platosMock).when(menuService).obtenerPlatosDestacados();

        // Act & Assert
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Bienvenido al restaurante de comida peruana"))
                .andExpect(jsonPath("$.menuPredefinido").value(vistaMock))
                .andExpect(jsonPath("$.platosDestacados[0]").value("Lomo Saltado"));
    }
}