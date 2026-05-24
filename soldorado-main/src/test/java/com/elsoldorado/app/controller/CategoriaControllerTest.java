package com.elsoldorado.app.controller;

// 1. Imports de JUnit y Mockito
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


// 2. Imports de Spring Test (MockMvc)
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// 3. Imports de TU PROYECTO (Asegúrate que estos caminos sean correctos)
import com.elsoldorado.app.model.Categoria;
import com.elsoldorado.app.service.CategoriaService;

// 4. Import para JSON
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CategoriaController.class)
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoriaService categoriaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SuppressWarnings("null")
    public void testObtenerTodasLasCategorias() throws Exception {
        Categoria c1 = new Categoria();
        c1.setNombre("Bebidas");
        
        when(categoriaService.obtenerTodasLasCategorias()).thenReturn(Arrays.asList(c1));

        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre").value("Bebidas"));
    }

    @Test
    public void testObtenerCategoriaPorId_CuandoExiste() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setNombre("Postres");
        
        when(categoriaService.buscarPorId(1L)).thenReturn(Optional.of(categoria));

        mockMvc.perform(get("/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Postres"));
    }

    @Test
    public void testObtenerCategoriaPorId_CuandoNoExiste() throws Exception {
        when(categoriaService.buscarPorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/categorias/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @SuppressWarnings("null")
    public void testCrearCategoria() throws Exception {
        Categoria nueva = new Categoria();
        nueva.setNombre("Entradas");
        
        when(categoriaService.crearCategoria(any(Categoria.class))).thenReturn(nueva);

        mockMvc.perform(post("/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Entradas"));
    }

    @Test
    public void testEliminarCategoria_CuandoNoExiste() throws Exception {
        when(categoriaService.eliminarCategoria(1L)).thenReturn(false);

        mockMvc.perform(delete("/categorias/1"))
                .andExpect(status().isNotFound());
    }
}