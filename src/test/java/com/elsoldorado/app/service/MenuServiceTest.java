package com.elsoldorado.app.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.elsoldorado.app.model.Plato;
import com.elsoldorado.app.repository.PlatoRepository;

public class MenuServiceTest {

    private MenuService menuService;

    @Mock
    private PlatoRepository platoRepository;

    @BeforeEach
    public void setUp() {
        // Inicializa los objetos anotados con @Mock
        MockitoAnnotations.openMocks(this);
        menuService = new MenuService(platoRepository);

        // Creamos la instancia del servicio inyectando el mock
        menuService = new MenuService(platoRepository);
    }

    @Test
    public void testObtenerMenuCompleto() {
        // Act: En el código todos los platos (6) están creados con disponible = true
        List<Plato> resultado = menuService.obtenerMenuCompleto();

        // Assert
        assertEquals(6, resultado.size(), "Debería retornar los 6 platos disponibles");
    }

    @Test
    public void testObtenerPlatosDestacados() {
        // Act: Según el código, el Suspiro a la Limeña (ID 6) es el único con destacado = false
        List<Plato> destacados = menuService.obtenerPlatosDestacados();

        // Assert
        assertEquals(5, destacados.size(), "Debería haber 5 platos destacados");
        assertTrue(destacados.stream().noneMatch(p -> p.getNombre().equals("Suspiro a la Limeña")));
    }

    @Test
    public void testObtenerVistaPredefinida() {
        // Act: Ají de Gallina (ID 4) y Suspiro (ID 6) tienen visibleEnInicio = false
        List<Plato> vista = menuService.obtenerVistaPredefinida();

        // Assert
        assertEquals(4, vista.size(), "Debería haber 4 platos visibles en el inicio");
    }

    @Test
    public void testObtenerPorCategoria() {
        // Act
        List<Plato> entradas = menuService.obtenerPorCategoria("Entradas");

        // Assert
        assertEquals(2, entradas.size(), "Deberían haber 2 platos en la categoría Entradas");
        assertTrue(entradas.stream().allMatch(p -> p.getNombre().equals("Ceviche") || p.getNombre().equals("Papa a la Huancaína")));
    }

    @Test
    public void testBuscarPorId_Existente() {
        // Act
        Optional<Plato> plato = menuService.buscarPorId(3L);

        // Assert
        assertTrue(plato.isPresent());
        assertEquals("Lomo Saltado", plato.get().getNombre());
    }
}