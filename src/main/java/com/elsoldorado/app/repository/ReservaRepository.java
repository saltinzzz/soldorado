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

    @Query("""
        SELECT r FROM Reserva r
        WHERE r.fecha BETWEEN :desde AND :hasta
        ORDER BY r.fecha, r.hora
    """)
    List<Reserva> buscarPorRangoFechas(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

    @Query("""
        SELECT COUNT(r) > 0 FROM Reserva r
        WHERE r.mesa.id = :mesaId
          AND r.fecha = :fecha
          AND r.hora = :hora
          AND r.estado <> :estadoExcluido
          AND (:reservaId IS NULL OR r.id <> :reservaId)
    """)
    boolean existeReservaActivaEnMesa(
            @Param("mesaId") Long mesaId,
            @Param("fecha") LocalDate fecha,
            @Param("hora") LocalTime hora,
            @Param("estadoExcluido") EstadoReserva estadoExcluido,
            @Param("reservaId") Long reservaId
    );
}
