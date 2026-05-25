package com.elsoldorado.app.dto.response;

import java.math.BigDecimal;

public class DetalleResponseDTO {
    private Long idPlato;
    private String nombrePlato;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public DetalleResponseDTO(Long idPlato, String nombrePlato, int cantidad, BigDecimal precioUnitario, BigDecimal subtotal) {
        this.idPlato = idPlato;
        this.nombrePlato = nombrePlato;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    public Long getIdPlato() { return idPlato; }
    public String getNombrePlato() { return nombrePlato; }
    public int getCantidad() { return cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public BigDecimal getSubtotal() { return subtotal; }
}
