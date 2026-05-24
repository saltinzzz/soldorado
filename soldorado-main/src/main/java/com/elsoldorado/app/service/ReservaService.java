package com.elsoldorado.app.service;

import com.elsoldorado.app.model.EstadoReserva;
import com.elsoldorado.app.model.Reserva;
import com.elsoldorado.app.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public List<Reserva> listarReservas() { return reservaRepository.findAll(); }
    public Reserva buscarPorId(Long id) { return reservaRepository.findById(id).orElse(null); }
    public List<Reserva> buscarProximas(LocalDate desde) { return reservaRepository.buscarProximas(desde); }
    public List<Reserva> buscarPorCliente(String cliente) { return reservaRepository.buscarPorCliente(cliente); }

    public Reserva registrarReserva(Reserva reserva) {
        validarReserva(reserva);
        if (reserva.getEstado() == null) reserva.setEstado(EstadoReserva.PENDIENTE);
        return reservaRepository.save(reserva);
    }

    public Reserva cambiarEstado(Long id, EstadoReserva nuevoEstado) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        reserva.setEstado(nuevoEstado);
        return reservaRepository.save(reserva);
    }

    public Reserva actualizarReserva(Long id, Reserva reservaActualizada) {
        Reserva reservaExistente = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        validarReserva(reservaActualizada);
        reservaExistente.setNombreCliente(reservaActualizada.getNombreCliente());
        reservaExistente.setTelefono(reservaActualizada.getTelefono());
        reservaExistente.setFecha(reservaActualizada.getFecha());
        reservaExistente.setHora(reservaActualizada.getHora());
        reservaExistente.setCantidadPersonas(reservaActualizada.getCantidadPersonas());
        reservaExistente.setObservacion(reservaActualizada.getObservacion());
        reservaExistente.setEstado(reservaActualizada.getEstado() == null ? reservaExistente.getEstado() : reservaActualizada.getEstado());
        reservaExistente.setMesa(reservaActualizada.getMesa());
        return reservaRepository.save(reservaExistente);
    }

    public void eliminarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        reservaRepository.delete(reserva);
    }

    private void validarReserva(Reserva reserva) {
        if (reserva.getNombreCliente() == null || reserva.getNombreCliente().isBlank()) throw new RuntimeException("El nombre del cliente es obligatorio");
        if (reserva.getTelefono() == null || reserva.getTelefono().isBlank()) throw new RuntimeException("El teléfono es obligatorio");
        if (reserva.getFecha() == null || reserva.getFecha().isBefore(LocalDate.now())) throw new RuntimeException("La fecha de reserva no es válida");
        if (reserva.getHora() == null) throw new RuntimeException("La hora de reserva es obligatoria");
        LocalTime apertura = LocalTime.of(12, 0);
        LocalTime cierre = LocalTime.of(22, 0);
        if (reserva.getHora().isBefore(apertura) || reserva.getHora().isAfter(cierre)) throw new RuntimeException("La hora está fuera del horario de atención");
        if (reserva.getCantidadPersonas() <= 0) throw new RuntimeException("La cantidad de personas debe ser mayor que cero");
    }
}
