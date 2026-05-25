package com.elsoldorado.app.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DetalleRequestDTO {
    @NotNull(message = "El id del plato es obligatorio")
    private Long idPlato;

    @Min(value = 1, message = "La cantidad debe ser mayor que cero")
    private int cantidad;

    public Long getIdPlato() { return idPlato; }
    public void setIdPlato(Long idPlato) { this.idPlato = idPlato; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
