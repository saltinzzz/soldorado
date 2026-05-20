package com.elsoldorado.app.repository;

import com.elsoldorado.app.model.EstadoPedido;
import com.elsoldorado.app.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByEstado(EstadoPedido estado);
    List<Pedido> findByNombreClienteIgnoreCase(String nombreCliente);
}