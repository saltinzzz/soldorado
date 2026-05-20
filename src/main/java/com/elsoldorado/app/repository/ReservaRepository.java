package com.elsoldorado.app.repository;

import com.elsoldorado.app.model.EstadoReserva;
import com.elsoldorado.app.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByEstado(EstadoReserva estado);
    List<Reserva> findByFecha(LocalDate fecha);
    boolean existsByMesaIdAndFechaAndHora(Long mesaId, LocalDate fecha, java.time.LocalTime hora);
}