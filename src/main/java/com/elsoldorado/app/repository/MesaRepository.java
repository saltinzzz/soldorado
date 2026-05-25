package com.elsoldorado.app.repository;

import com.elsoldorado.app.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {
    List<Mesa> findByDisponibleTrue();
    boolean existsByNumero(int numero);

    @Query("SELECT m FROM Mesa m WHERE m.disponible = true AND m.capacidad >= :capacidad ORDER BY m.capacidad, m.numero")
    List<Mesa> buscarDisponiblesPorCapacidad(@Param("capacidad") int capacidad);
}
