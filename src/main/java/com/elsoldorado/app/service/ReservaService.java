package com.elsoldorado.app.service;

import com.elsoldorado.app.dto.mapper.RestauranteMapper;
import com.elsoldorado.app.dto.request.ReservaRequestDTO;
import com.elsoldorado.app.model.EstadoReserva;
import com.elsoldorado.app.model.Mesa;
import com.elsoldorado.app.model.Reserva;
import com.elsoldorado.app.repository.MesaRepository;
import com.elsoldorado.app.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;
    private final MesaRepository mesaRepository;

    public ReservaService(ReservaRepository reservaRepository, MesaRepository mesaRepository) {
        this.reservaRepository = reservaRepository;
        this.mesaRepository = mesaRepository;
    }

    public List<Reserva> listarReservas() { return reservaRepository.findAll(); }
    public Reserva buscarPorId(Long id) { return reservaRepository.findById(id).orElse(null); }
    public List<Reserva> buscarProximas(LocalDate desde) { return reservaRepository.buscarProximas(desde); }
    public List<Reserva> buscarPorCliente(String cliente) { return reservaRepository.buscarPorCliente(cliente); }
    public List<Reserva> buscarPorRangoFechas(LocalDate desde, LocalDate hasta) { return reservaRepository.buscarPorRangoFechas(desde, hasta); }

    @Transactional
    public Reserva registrarReserva(ReservaRequestDTO dto) {
        return registrarReserva(RestauranteMapper.toReserva(dto));
    }

    @Transactional
    public Reserva registrarReserva(Reserva reserva) {
        validarReserva(reserva, null);
        if (reserva.getEstado() == null) reserva.setEstado(EstadoReserva.PENDIENTE);
        asignarMesaValidada(reserva, null);
        return reservaRepository.save(reserva);
    }

    @Transactional
    public Reserva cambiarEstado(Long id, EstadoReserva nuevoEstado) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado es obligatorio");
        }
        reserva.setEstado(nuevoEstado);
        return reservaRepository.save(reserva);
    }

    @Transactional
    public Reserva actualizarReserva(Long id, ReservaRequestDTO dto) {
        return actualizarReserva(id, RestauranteMapper.toReserva(dto));
    }

    @Transactional
    public Reserva actualizarReserva(Long id, Reserva reservaActualizada) {
        Reserva reservaExistente = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        validarReserva(reservaActualizada, id);
        reservaExistente.setNombreCliente(reservaActualizada.getNombreCliente());
        reservaExistente.setTelefono(reservaActualizada.getTelefono());
        reservaExistente.setFecha(reservaActualizada.getFecha());
        reservaExistente.setHora(reservaActualizada.getHora());
        reservaExistente.setCantidadPersonas(reservaActualizada.getCantidadPersonas());
        reservaExistente.setObservacion(reservaActualizada.getObservacion());
        reservaExistente.setEstado(reservaActualizada.getEstado() == null ? reservaExistente.getEstado() : reservaActualizada.getEstado());
        reservaExistente.setMesa(reservaActualizada.getMesa());
        asignarMesaValidada(reservaExistente, id);
        return reservaRepository.save(reservaExistente);
    }

    @Transactional
    public void eliminarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        reservaRepository.delete(reserva);
    }

    private void asignarMesaValidada(Reserva reserva, Long reservaId) {
        if (reserva.getMesa() == null || reserva.getMesa().getId() == null) {
            return;
        }
        Mesa mesa = mesaRepository.findById(reserva.getMesa().getId())
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
        if (!mesa.isDisponible()) {
            throw new IllegalArgumentException("La mesa seleccionada no está disponible");
        }
        if (mesa.getCapacidad() < reserva.getCantidadPersonas()) {
            throw new IllegalArgumentException("La capacidad de la mesa es menor a la cantidad de personas");
        }
        boolean ocupada = reservaRepository.existeReservaActivaEnMesa(
                mesa.getId(), reserva.getFecha(), reserva.getHora(), EstadoReserva.CANCELADA, reservaId);
        if (ocupada) {
            throw new IllegalArgumentException("La mesa ya tiene una reserva activa en esa fecha y hora");
        }
        reserva.setMesa(mesa);
    }

    private void validarReserva(Reserva reserva, Long reservaId) {
        if (reserva == null) throw new IllegalArgumentException("La reserva es obligatoria");
        if (reserva.getNombreCliente() == null || reserva.getNombreCliente().isBlank()) throw new IllegalArgumentException("El nombre del cliente es obligatorio");
        if (reserva.getTelefono() == null || reserva.getTelefono().isBlank()) throw new IllegalArgumentException("El teléfono es obligatorio");
        if (reserva.getFecha() == null || reserva.getFecha().isBefore(LocalDate.now())) throw new IllegalArgumentException("La fecha de reserva no es válida");
        if (reserva.getHora() == null) throw new IllegalArgumentException("La hora de reserva es obligatoria");
        LocalTime apertura = LocalTime.of(12, 0);
        LocalTime cierre = LocalTime.of(22, 0);
        if (reserva.getHora().isBefore(apertura) || reserva.getHora().isAfter(cierre)) throw new IllegalArgumentException("La hora está fuera del horario de atención");
        if (reserva.getCantidadPersonas() <= 0) throw new IllegalArgumentException("La cantidad de personas debe ser mayor que cero");
    }
}
