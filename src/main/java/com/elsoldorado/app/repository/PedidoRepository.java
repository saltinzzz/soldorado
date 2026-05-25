package com.elsoldorado.app.repository;

import com.elsoldorado.app.model.EstadoPedido;
import com.elsoldorado.app.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByEstado(EstadoPedido estado);
    List<Pedido> findByNombreClienteIgnoreCase(String nombreCliente);

    @Query("SELECT p FROM Pedido p WHERE LOWER(p.nombreCliente) LIKE LOWER(CONCAT('%', :cliente, '%')) ORDER BY p.fechaHora DESC")
    List<Pedido> buscarPorCliente(@Param("cliente") String cliente);

    @Query("SELECT p FROM Pedido p WHERE p.estado = :estado AND p.fechaHora >= :fechaHora ORDER BY p.fechaHora DESC")
    List<Pedido> buscarPorEstadoDesde(@Param("estado") EstadoPedido estado, @Param("fechaHora") LocalDateTime fechaHora);

    @Query("SELECT p FROM Pedido p WHERE p.fechaHora BETWEEN :desde AND :hasta ORDER BY p.fechaHora DESC")
    List<Pedido> buscarPorRangoFechas(@Param("desde") LocalDateTime desde, @Param("hasta") LocalDateTime hasta);

    @Query("SELECT p FROM Pedido p WHERE p.total >= :monto ORDER BY p.total DESC")
    List<Pedido> buscarConTotalMayorOIgual(@Param("monto") BigDecimal monto);
}
