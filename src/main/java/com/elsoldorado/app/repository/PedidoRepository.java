package com.elsoldorado.app.repository;

import com.elsoldorado.app.model.EstadoPedido;
import com.elsoldorado.app.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
