package com.elsoldorado.app.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.elsoldorado.app.model.Plato;
import com.elsoldorado.app.service.MenuService;

@WebMvcTest(MenuController.class)
@SuppressWarnings("null") // Para evitar las advertencias de "Null safety"
public class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // Versión compatible con Spring Boot 3.5.13
    private MenuService menuService;

    @Test
    public void testObtenerMenuCompleto() throws Exception {
        Plato p1 = new Plato();
        p1.setNombre("Lomo Saltado");

        doReturn(Arrays.asList(p1)).when(menuService).obtenerMenuCompleto();

        mockMvc.perform(get("/menu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Lomo Saltado"));
    }

    @Test
    public void testObtenerPlatoPorId_CuandoExiste() throws Exception {
        Plato p = new Plato();
        p.setNombre("Cebiche");

        doReturn(Optional.of(p)).when(menuService).buscarPorId(1L);

        mockMvc.perform(get("/menu/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Cebiche"));
    }

    @Test
    public void testObtenerPlatoPorId_CuandoNoExiste() throws Exception {
        doReturn(Optional.empty()).when(menuService).buscarPorId(anyLong());

        mockMvc.perform(get("/menu/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testObtenerPorCategoria() throws Exception {
        Plato p = new Plato();
        p.setNombre("Suspiro a la Limeña");

        doReturn(Arrays.asList(p)).when(menuService).obtenerPorCategoria("Postres");

        mockMvc.perform(get("/menu/categoria/Postres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Suspiro a la Limeña"));
    }
}