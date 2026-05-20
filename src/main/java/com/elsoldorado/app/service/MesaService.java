package com.elsoldorado.app.service;
import com.elsoldorado.app.model.Mesa;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MesaService {
    private final List<Mesa> mesas = new ArrayList<>();
    private Long secuencia = 1L;

    public MesaService() {
        mesas.add(new Mesa(1L, 1, 2, true));
        mesas.add(new Mesa(2L, 2, 4, true));
        mesas.add(new Mesa(3L, 3, 6, false));
        mesas.add(new Mesa(4L, 4, 4, true));
        secuencia = 5L;
    }

    public List<Mesa> listarMesas() { 
        return mesas; 
    }

    public List<Mesa> listarDisponibles() {
        return mesas.stream()
                .filter(Mesa::isDisponible)
                .collect(Collectors.toList());
    }

    public Optional<Mesa> buscarPorId(Long id) {
        return mesas.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

    public Mesa crearMesa(Mesa mesa) {
        if (mesa.getCapacidad() <= 0)
            throw new RuntimeException("La capacidad debe ser mayor que cero");
        mesa.setId(secuencia++);
        mesa.setDisponible(true);
        mesas.add(mesa);
        return mesa;
    }

    public Mesa cambiarDisponibilidad(Long id, boolean disponible) {
        Mesa mesa = buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
        mesa.setDisponible(disponible);
        return mesa;
    }

    public void eliminarMesa(Long id) {
        Mesa mesa = buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
        mesas.remove(mesa);
    }
}