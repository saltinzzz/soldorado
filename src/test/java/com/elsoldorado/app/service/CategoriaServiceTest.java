package com.elsoldorado.app.service;

import com.elsoldorado.app.model.Categoria;
import com.elsoldorado.app.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {
    @Mock
    private CategoriaRepository categoriaRepository;

    private CategoriaService categoriaService;

    @BeforeEach
    void setUp() {
        categoriaService = new CategoriaService(categoriaRepository);
    }

    @Test
    void obtenerTodasLasCategorias() {
        when(categoriaRepository.findAll()).thenReturn(List.of(new Categoria(1L, "Bebidas")));
        assertEquals(1, categoriaService.obtenerTodasLasCategorias().size());
        verify(categoriaRepository).findAll();
    }

    @Test
    void crearCategoria() {
        Categoria categoria = new Categoria(null, "Entradas");
        when(categoriaRepository.existsByNombreIgnoreCase("Entradas")).thenReturn(false);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(new Categoria(1L, "Entradas"));

        Categoria creada = categoriaService.crearCategoria(categoria);

        assertEquals(1L, creada.getId());
        assertEquals("Entradas", creada.getNombre());
        verify(categoriaRepository).save(categoria);
    }
}
