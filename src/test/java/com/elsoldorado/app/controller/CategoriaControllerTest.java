package com.elsoldorado.app.controller;

import com.elsoldorado.app.model.Categoria;
import com.elsoldorado.app.service.CategoriaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // <-- 1. NUEVA IMPORTACIÓN
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoriaController.class)
@AutoConfigureMockMvc(addFilters = false)
@SuppressWarnings("null")
class CategoriaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoriaService categoriaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarCategorias() throws Exception {
        Categoria categoria = new Categoria(1L, "Bebidas");
        when(categoriaService.obtenerTodasLasCategorias()).thenReturn(List.of(categoria));

        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Bebidas"));
    }

    @Test
    void crearCategoria() throws Exception {
        Categoria categoria = new Categoria(1L, "Entradas");
        when(categoriaService.crearCategoria(any(Categoria.class))).thenReturn(categoria);

        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Entradas"));
    }

    @Test
    void obtenerCategoriaNoEncontrada() throws Exception {
        when(categoriaService.buscarPorId(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/categorias/99"))
                .andExpect(status().isNotFound());
    }
}