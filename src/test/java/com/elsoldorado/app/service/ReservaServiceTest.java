package com.elsoldorado.app.service;

import com.elsoldorado.app.model.EstadoReserva;
import com.elsoldorado.app.model.Reserva;
import com.elsoldorado.app.repository.MesaRepository;
import com.elsoldorado.app.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {
    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private MesaRepository mesaRepository;

    private ReservaService reservaService;

    @BeforeEach
    void setUp() {
        reservaService = new ReservaService(reservaRepository, mesaRepository);
    }

    @Test
    void registrarReservaExito() {
        Reserva reserva = reservaBase();
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> {
            Reserva guardada = invocation.getArgument(0);
            guardada.setId(1L);
            return guardada;
        });

        Reserva registrada = reservaService.registrarReserva(reserva);

        assertEquals(1L, registrada.getId());
        assertEquals(EstadoReserva.PENDIENTE, registrada.getEstado());
    }

    @Test
    void registrarReservaConFechaPasada() {
        Reserva reserva = reservaBase();
        reserva.setFecha(LocalDate.now().minusDays(1));

        assertThrows(IllegalArgumentException.class, () -> reservaService.registrarReserva(reserva));
    }

    private Reserva reservaBase() {
        Reserva reserva = new Reserva();
        reserva.setNombreCliente("Maria");
        reserva.setTelefono("999111222");
        reserva.setFecha(LocalDate.now().plusDays(1));
        reserva.setHora(LocalTime.of(15, 0));
        reserva.setCantidadPersonas(4);
        return reserva;
    }
}
