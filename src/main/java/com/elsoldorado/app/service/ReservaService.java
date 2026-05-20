package com.elsoldorado.app.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.elsoldorado.app.model.EstadoReserva;
import com.elsoldorado.app.model.Reserva;

@Service
public class ReservaService {
    private final List<Reserva> reservas = new ArrayList<>();
    private Long secuencia = 1L;

    public ReservaService() {
        inicializarReservasDeEjemplo();
    }

    private void inicializarReservasDeEjemplo() {
        reservas.add(new Reserva(1L, "Pedro Vargas", "987111222", LocalDate.now().plusDays(1), LocalTime.of(13, 30), 2,
                "Cumpleaños", EstadoReserva.CONFIRMADA));
        reservas.add(new Reserva(2L, "Sofía Blanco", "988222333", LocalDate.now().plusDays(2), LocalTime.of(19, 0), 4,
                "Mesa cerca a la ventana", EstadoReserva.PENDIENTE));
        reservas.add(new Reserva(3L, "Miguel Castro", "999333444", LocalDate.now().plusDays(3), LocalTime.of(20, 30), 3,
                "Vegetariano", EstadoReserva.CONFIRMADA));
        reservas.add(new Reserva(4L, "Elena Rojas", "976554433", LocalDate.now().plusDays(4), LocalTime.of(18, 0), 5,
                "Aniversario", EstadoReserva.PENDIENTE));
        reservas.add(new Reserva(5L, "Raúl Fuentes", "995666777", LocalDate.now().plusDays(5), LocalTime.of(21, 0), 2,
                "Mesa para dos", EstadoReserva.PENDIENTE));
        secuencia = 6L;
    }

    public List<Reserva> listarReservas() {
        return reservas;
    }

    public Reserva buscarPorId(Long id) {
        return reservas.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Reserva registrarReserva(Reserva reserva) {
        validarReserva(reserva);

        reserva.setId(secuencia++);
        reserva.setEstado(EstadoReserva.PENDIENTE);

        reservas.add(reserva);
        return reserva;
    }

    public Reserva cambiarEstado(Long id, EstadoReserva nuevoEstado) {
        Reserva reserva = reservas.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        reserva.setEstado(nuevoEstado);
        return reserva;
    }

    public Reserva actualizarReserva(Long id, Reserva reservaActualizada) {
        Reserva reservaExistente = reservas.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // Validar la reserva actualizada
        validarReserva(reservaActualizada);

        // Actualizar campos (mantener id)
        reservaExistente.setNombreCliente(reservaActualizada.getNombreCliente());
        reservaExistente.setTelefono(reservaActualizada.getTelefono());
        reservaExistente.setFecha(reservaActualizada.getFecha());
        reservaExistente.setHora(reservaActualizada.getHora());
        reservaExistente.setCantidadPersonas(reservaActualizada.getCantidadPersonas());
        reservaExistente.setObservacion(reservaActualizada.getObservacion());
        reservaExistente.setEstado(reservaActualizada.getEstado());

        return reservaExistente;
    }

    public void eliminarReserva(Long id) {
        Reserva reserva = reservas.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        reservas.remove(reserva);
    }

    private void validarReserva(Reserva reserva) {
        if (reserva.getNombreCliente() == null || reserva.getNombreCliente().isBlank()) {
            throw new RuntimeException("El nombre del cliente es obligatorio");
        }

        if (reserva.getTelefono() == null || reserva.getTelefono().isBlank()) {
            throw new RuntimeException("El teléfono es obligatorio");
        }

        if (reserva.getFecha() == null || reserva.getFecha().isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha de reserva no es válida");
        }

        if (reserva.getHora() == null) {
            throw new RuntimeException("La hora de reserva es obligatoria");
        }

        LocalTime apertura = LocalTime.of(12, 0);
        LocalTime cierre = LocalTime.of(22, 0);

        if (reserva.getHora().isBefore(apertura) || reserva.getHora().isAfter(cierre)) {
            throw new RuntimeException("La hora está fuera del horario de atención");
        }

        if (reserva.getCantidadPersonas() <= 0) {
            throw new RuntimeException("La cantidad de personas debe ser mayor que cero");
        }
    }
}
