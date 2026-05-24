package com.elsoldorado.app.repository;

import com.elsoldorado.app.model.Plato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PlatoRepository extends JpaRepository<Plato, Long> {
    List<Plato> findByDisponibleTrue();
    List<Plato> findByDisponibleTrueAndDestacadoTrue();
    List<Plato> findByDisponibleTrueAndVisibleEnInicioTrue();
    List<Plato> findByDisponibleTrueAndCategoriaNombreIgnoreCase(String nombre);

    @Query("""
        SELECT p FROM Plato p
        WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
           OR LOWER(p.categoria.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
        ORDER BY p.nombre
    """)
    List<Plato> buscarPorNombreOCategoria(@Param("texto") String texto);

    @Query("""
        SELECT p FROM Plato p
        WHERE p.precio BETWEEN :min AND :max
        ORDER BY p.precio ASC
    """)
    List<Plato> buscarPorRangoPrecio(@Param("min") BigDecimal min, @Param("max") BigDecimal max);
}
