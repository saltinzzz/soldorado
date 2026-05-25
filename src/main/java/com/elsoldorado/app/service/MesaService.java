package com.elsoldorado.app.service;

import com.elsoldorado.app.model.Mesa;
import com.elsoldorado.app.repository.MesaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@SuppressWarnings("null")
public class MesaService {
    private final MesaRepository mesaRepository;

    public MesaService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    public List<Mesa> listarMesas() { return mesaRepository.findAll(); }
    public List<Mesa> listarDisponibles() { return mesaRepository.findByDisponibleTrue(); }
    public List<Mesa> buscarDisponiblesPorCapacidad(int capacidad) { return mesaRepository.buscarDisponiblesPorCapacidad(capacidad); }
    public Optional<Mesa> buscarPorId(Long id) { return mesaRepository.findById(id); }

    public Mesa crearMesa(Mesa mesa) {
        if (mesa.getCapacidad() <= 0) throw new RuntimeException("La capacidad debe ser mayor que cero");
        if (mesaRepository.existsByNumero(mesa.getNumero())) throw new RuntimeException("Ya existe una mesa con ese número");
        if (!mesa.isDisponible()) mesa.setDisponible(true);
        return mesaRepository.save(mesa);
    }

    public Mesa cambiarDisponibilidad(Long id, boolean disponible) {
        Mesa mesa = mesaRepository.findById(id).orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
        mesa.setDisponible(disponible);
        return mesaRepository.save(mesa);
    }

    public void eliminarMesa(Long id) {
        Mesa mesa = mesaRepository.findById(id).orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
        mesaRepository.delete(mesa);
    }
}
