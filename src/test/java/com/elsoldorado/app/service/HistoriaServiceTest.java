package com.elsoldorado.app.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HistoriaServiceTest {
    @Test
    void obtenerHistoria() {
        HistoriaService service = new HistoriaService();
        String historia = service.obtenerHistoria();
        assertNotNull(historia);
        assertFalse(historia.isBlank());
    }
}
