package com.elsoldorado.app.repository;

import com.elsoldorado.app.model.EstadoReserva;
import com.elsoldorado.app.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByEstado(EstadoReserva estado);
    List<Reserva> findByFecha(LocalDate fecha);
    boolean existsByMesaIdAndFechaAndHora(Long mesaId, LocalDate fecha, LocalTime hora);

    @Query("SELECT r FROM Reserva r WHERE r.fecha >= :desde ORDER BY r.fecha, r.hora")
    List<Reserva> buscarProximas(@Param("desde") LocalDate desde);

    @Query("SELECT r FROM Reserva r WHERE LOWER(r.nombreCliente) LIKE LOWER(CONCAT('%', :cliente, '%')) ORDER BY r.fecha, r.hora")
    List<Reserva> buscarPorCliente(@Param("cliente") String cliente);
}
