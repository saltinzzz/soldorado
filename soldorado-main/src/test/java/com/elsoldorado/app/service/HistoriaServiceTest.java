package com.elsoldorado.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HistoriaServiceTest {

    private HistoriaService historiaService;

    @BeforeEach
    public void setUp() {
        // Inicializamos el servicio manualmente ya que no tiene dependencias
        historiaService = new HistoriaService();
    }

    @Test
    public void testObtenerHistoria() {
        // Act
        String resultado = historiaService.obtenerHistoria();

        // Assert
        assertNotNull(resultado, "La historia no debería ser nula");
        assertTrue(resultado.contains("restaurante de comida peruana"), "La historia debe mencionar que es comida peruana");
        assertTrue(resultado.contains("sabores tradicionales del Perú"), "La historia debe mencionar los sabores tradicionales");
        
        // Verificamos el texto exacto que definiste en tu Service
        String esperado = "Somos un restaurante de comida peruana creado con la pasión de compartir "
                        + "los sabores tradicionales del Perú, brindando a nuestros clientes una "
                        + "experiencia cálida, auténtica y familiar.";
        assertEquals(esperado, resultado);
    }
}