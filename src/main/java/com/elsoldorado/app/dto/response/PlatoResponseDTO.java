package com.elsoldorado.app.dto.response;

import java.math.BigDecimal;

public class PlatoResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Long categoriaId;
    private String categoria;
    private boolean disponible;
    private boolean destacado;
    private boolean visibleEnInicio;

    public PlatoResponseDTO(Long id, String nombre, String descripcion,
            BigDecimal precio, Long categoriaId, String categoria, boolean disponible, boolean destacado, boolean visibleEnInicio) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoriaId = categoriaId;
        this.categoria = categoria;
        this.disponible = disponible;
        this.destacado = destacado;
        this.visibleEnInicio = visibleEnInicio;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public Long getCategoriaId() { return categoriaId; }
    public String getCategoria() { return categoria; }
    public boolean isDisponible() { return disponible; }
    public boolean isDestacado() { return destacado; }
    public boolean isVisibleEnInicio() { return visibleEnInicio; }
}
