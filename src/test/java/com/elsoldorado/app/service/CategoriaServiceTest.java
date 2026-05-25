package com.elsoldorado.app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.elsoldorado.app.model.Categoria;
import com.elsoldorado.app.repository.CategoriaRepository;

public class CategoriaServiceTest {

    private CategoriaRepository categoriaRepository;
    private CategoriaService categoriaService;

    @BeforeEach
    public void setUp() {
        // Reiniciamos el servicio antes de cada test para tener los datos originales
        categoriaRepository = mock(CategoriaRepository.class);
        categoriaService = new CategoriaService(categoriaRepository);
    }

    @Test
    public void testObtenerTodasLasCategorias() {
        List<Categoria> resultado = categoriaService.obtenerTodasLasCategorias();
        assertEquals(4, resultado.size(), "Debería iniciar con 4 categorías");
    }

    @Test
    public void testCrearCategoria_Exito() {
        Categoria nueva = new Categoria();
        nueva.setNombre("Pescados");

        Categoria creada = categoriaService.crearCategoria(nueva);

        assertNotNull(creada.getId());
        assertEquals(5L, creada.getId(), "El primer ID autogenerado debe ser 5");
        assertEquals("Pescados", creada.getNombre());
        assertEquals(5, categoriaService.obtenerTodasLasCategorias().size());
    }

    @Test
    public void testCrearCategoria_ErrorNombreDuplicado() {
        Categoria duplicada = new Categoria();
        duplicada.setNombre("Entradas"); // Ya existe en el constructor

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            categoriaService.crearCategoria(duplicada);
        });

        assertEquals("Ya existe una categoría con ese nombre", exception.getMessage());
    }

    @Test
    public void testActualizarCategoria_Exito() {
        Categoria cambios = new Categoria();
        cambios.setNombre("Entradas Calientes");

        Optional<Categoria> actualizada = categoriaService.actualizarCategoria(1L, cambios);

        assertTrue(actualizada.isPresent());
        assertEquals("Entradas Calientes", actualizada.get().getNombre());
    }

    @Test
    public void testActualizarCategoria_ErrorNombreDuplicado() {
        Categoria cambios = new Categoria();
        cambios.setNombre("Bebidas"); // Intentamos renombrar ID 1 a algo que ya existe (ID 3)

        assertThrows(IllegalArgumentException.class, () -> {
            categoriaService.actualizarCategoria(1L, cambios);
        });
    }

    @Test
    public void testEliminarCategoria_Exito() {
        boolean eliminado = categoriaService.eliminarCategoria(1L);

        assertTrue(eliminado);
        assertEquals(3, categoriaService.obtenerTodasLasCategorias().size());
    }

    @Test
    public void testEliminarCategoria_NoExistente() {
        boolean eliminado = categoriaService.eliminarCategoria(99L);
        assertFalse(eliminado);
    }
}