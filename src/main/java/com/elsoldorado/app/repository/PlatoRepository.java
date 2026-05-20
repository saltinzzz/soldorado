package com.elsoldorado.app.repository;

import com.elsoldorado.app.model.Plato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlatoRepository extends JpaRepository<Plato, Long> {
    List<Plato> findByDisponibleTrue();
    List<Plato> findByDisponibleTrueAndDestacadoTrue();
    List<Plato> findByDisponibleTrueAndVisibleEnInicioTrue();
    List<Plato> findByDisponibleTrueAndCategoriaNombreIgnoreCase(String nombre);
}