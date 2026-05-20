package com.elsoldorado.app.repository;

import com.elsoldorado.app.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {
    List<Mesa> findByDisponibleTrue();
    boolean existsByNumero(int numero);
}