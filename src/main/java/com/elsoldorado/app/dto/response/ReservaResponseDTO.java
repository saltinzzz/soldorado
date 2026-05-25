package com.elsoldorado.app.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import com.elsoldorado.app.model.EstadoReserva;

public class ReservaResponseDTO {
    private Long id;
    private String nombreCliente;
    private String telefono;
    private LocalDate fecha;
    private LocalTime hora;
    private int cantidadPersonas;
    private String observacion;
    private EstadoReserva estado;
    private Long mesaId;
    private Integer mesaNumero;

    public ReservaResponseDTO(Long id, String nombreCliente, String telefono,
            LocalDate fecha, LocalTime hora, int cantidadPersonas,
            String observacion, EstadoReserva estado, Long mesaId, Integer mesaNumero) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.telefono = telefono;
        this.fecha = fecha;
        this.hora = hora;
        this.cantidadPersonas = cantidadPersonas;
        this.observacion = observacion;
        this.estado = estado;
        this.mesaId = mesaId;
        this.mesaNumero = mesaNumero;
    }

    public Long getId() { return id; }
    public String getNombreCliente() { return nombreCliente; }
    public String getTelefono() { return telefono; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHora() { return hora; }
    public int getCantidadPersonas() { return cantidadPersonas; }
    public String getObservacion() { return observacion; }
    public EstadoReserva getEstado() { return estado; }
    public Long getMesaId() { return mesaId; }
    public Integer getMesaNumero() { return mesaNumero; }
}
