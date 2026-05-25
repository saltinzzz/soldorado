package com.elsoldorado.app.service;

import com.elsoldorado.app.model.Categoria;
import com.elsoldorado.app.model.Plato;
import com.elsoldorado.app.repository.CategoriaRepository;
import com.elsoldorado.app.repository.PlatoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private PlatoRepository platoRepository;
    @Mock
    private CategoriaRepository categoriaRepository;

    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(platoRepository, categoriaRepository);
    }

    @Test
    void obtenerMenuCompleto() {
        when(platoRepository.findByDisponibleTrue()).thenReturn(List.of(platoEjemplo()));
        assertEquals(1, menuService.obtenerMenuCompleto().size());
        verify(platoRepository).findByDisponibleTrue();
    }

    @Test
    void agregarPlato() {
        Categoria categoria = new Categoria(1L, "Entradas");
        Plato plato = platoEjemplo();
        plato.setCategoria(categoria);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(platoRepository.save(plato)).thenReturn(plato);

        Plato guardado = menuService.agregarPlato(plato);

        assertEquals("Ceviche", guardado.getNombre());
        verify(platoRepository).save(plato);
    }

    private Plato platoEjemplo() {
        Categoria categoria = new Categoria(1L, "Entradas");
        return new Plato(1L, "Ceviche", "Pescado fresco", new BigDecimal("32.00"), categoria, true, true, true);
    }
}
