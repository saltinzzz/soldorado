package com.elsoldorado.app.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.elsoldorado.app.model.EstadoReserva;
import com.elsoldorado.app.model.Reserva;

public class ReservaServiceTest {

    private ReservaService reservaService;

    @BeforeEach
    public void setUp() {
        // Inicializamos el servicio. El constructor cargará las 5 reservas de ejemplo.
        reservaService = new ReservaService();
    }

    @Test
    public void testListarReservas() {
        List<Reserva> resultado = reservaService.listarReservas();
        assertEquals(5, resultado.size(), "Debería iniciar con 5 reservas de ejemplo");
    }

    @Test
    public void testRegistrarReserva_Exito() {
        Reserva nueva = new Reserva();
        nueva.setNombreCliente("Jose Developer");
        nueva.setTelefono("999111222");
        nueva.setFecha(LocalDate.now().plusDays(1)); // Mañana
        nueva.setHora(LocalTime.of(15, 0)); // 3:00 PM (Dentro del horario)
        nueva.setCantidadPersonas(4);

        Reserva registrada = reservaService.registrarReserva(nueva);

        assertNotNull(registrada.getId());
        assertEquals(6L, registrada.getId());
        assertEquals(EstadoReserva.PENDIENTE, registrada.getEstado());
    }

    @Test
    public void testRegistrarReserva_ErrorHorarioInvalido() {
        Reserva nueva = new Reserva();
        nueva.setNombreCliente("Cliente Nocturno");
        nueva.setTelefono("987654321");
        nueva.setFecha(LocalDate.now().plusDays(1));
        nueva.setHora(LocalTime.of(23, 30)); // Fuera de horario (Cierre 22:00)
        nueva.setCantidadPersonas(2);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservaService.registrarReserva(nueva);
        });

        assertEquals("La hora está fuera del horario de atención", exception.getMessage());
    }

    @Test
    public void testRegistrarReserva_ErrorFechaPasada() {
        Reserva nueva = new Reserva();
        nueva.setNombreCliente("Viajero del Tiempo");
        nueva.setFecha(LocalDate.now().minusDays(1)); // Ayer
        nueva.setHora(LocalTime.of(13, 0));

        assertThrows(RuntimeException.class, () -> {
            reservaService.registrarReserva(nueva);
        });
    }

    @Test
    public void testCambiarEstado() {
        // Usamos el ID 2 de las reservas de ejemplo
        Reserva actualizada = reservaService.cambiarEstado(2L, EstadoReserva.CONFIRMADA);
        
        assertEquals(EstadoReserva.CONFIRMADA, actualizada.getEstado());
    }

    @Test
    public void testEliminarReserva() {
        reservaService.eliminarReserva(1L);
        assertEquals(4, reservaService.listarReservas().size());
    }

    @Test
    public void testBuscarPorId_NoExiste() {
        Reserva resultado = reservaService.buscarPorId(99L);
        assertNull(resultado);
    }
}