package com.elsoldorado.app.dto.mapper;

import com.elsoldorado.app.dto.request.DetalleRequestDTO;
import com.elsoldorado.app.dto.request.PedidoRequestDTO;
import com.elsoldorado.app.dto.request.ReservaRequestDTO;
import com.elsoldorado.app.dto.response.DetalleResponseDTO;
import com.elsoldorado.app.dto.response.MesaResponseDTO;
import com.elsoldorado.app.dto.response.PedidoResponseDTO;
import com.elsoldorado.app.dto.response.PlatoResponseDTO;
import com.elsoldorado.app.dto.response.ReservaResponseDTO;
import com.elsoldorado.app.dto.response.UsuarioResponseDTO;
import com.elsoldorado.app.model.DetallePedido;
import com.elsoldorado.app.model.Mesa;
import com.elsoldorado.app.model.Pedido;
import com.elsoldorado.app.model.Plato;
import com.elsoldorado.app.model.Reserva;
import com.elsoldorado.app.model.Usuario;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class RestauranteMapper {
    private RestauranteMapper() {
    }

    public static Pedido toPedido(PedidoRequestDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setNombreCliente(dto.getNombreCliente());
        pedido.setTelefono(dto.getTelefono());
        pedido.setDireccion(dto.getDireccion());
        pedido.setReferencia(dto.getReferencia());

        List<DetallePedido> detalles = new ArrayList<>();
        if (dto.getDetalles() != null) {
            for (DetalleRequestDTO detalleDTO : dto.getDetalles()) {
                DetallePedido detalle = new DetallePedido();
                detalle.setIdPlato(detalleDTO.getIdPlato());
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setPrecioUnitario(BigDecimal.ZERO);
                detalles.add(detalle);
            }
        }
        pedido.setDetalles(detalles);
        return pedido;
    }

    public static PedidoResponseDTO toPedidoResponse(Pedido pedido) {
        List<DetalleResponseDTO> detalles = pedido.getDetalles().stream()
                .map(RestauranteMapper::toDetalleResponse)
                .toList();
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getNombreCliente(),
                pedido.getTelefono(),
                pedido.getDireccion(),
                pedido.getReferencia(),
                detalles,
                pedido.getTotal(),
                pedido.getFechaHora(),
                pedido.getEstado()
        );
    }

    public static DetalleResponseDTO toDetalleResponse(DetallePedido detalle) {
        return new DetalleResponseDTO(
                detalle.getIdPlato(),
                detalle.getNombrePlato(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getSubtotal()
        );
    }

    public static Reserva toReserva(ReservaRequestDTO dto) {
        Reserva reserva = new Reserva();
        reserva.setNombreCliente(dto.getNombreCliente());
        reserva.setTelefono(dto.getTelefono());
        reserva.setFecha(dto.getFecha());
        reserva.setHora(dto.getHora());
        reserva.setCantidadPersonas(dto.getCantidadPersonas());
        reserva.setObservacion(dto.getObservacion());
        if (dto.getMesaId() != null) {
            Mesa mesa = new Mesa();
            mesa.setId(dto.getMesaId());
            reserva.setMesa(mesa);
        }
        return reserva;
    }

    public static ReservaResponseDTO toReservaResponse(Reserva reserva) {
        Long mesaId = reserva.getMesa() == null ? null : reserva.getMesa().getId();
        Integer mesaNumero = reserva.getMesa() == null ? null : reserva.getMesa().getNumero();
        return new ReservaResponseDTO(
                reserva.getId(),
                reserva.getNombreCliente(),
                reserva.getTelefono(),
                reserva.getFecha(),
                reserva.getHora(),
                reserva.getCantidadPersonas(),
                reserva.getObservacion(),
                reserva.getEstado(),
                mesaId,
                mesaNumero
        );
    }

    public static PlatoResponseDTO toPlatoResponse(Plato plato) {
        Long categoriaId = plato.getCategoria() == null ? null : plato.getCategoria().getId();
        String categoriaNombre = plato.getCategoria() == null ? null : plato.getCategoria().getNombre();
        return new PlatoResponseDTO(
                plato.getId(),
                plato.getNombre(),
                plato.getDescripcion(),
                plato.getPrecio(),
                categoriaId,
                categoriaNombre,
                plato.isDisponible(),
                plato.isDestacado(),
                plato.isVisibleEnInicio()
        );
    }

    public static MesaResponseDTO toMesaResponse(Mesa mesa) {
        return new MesaResponseDTO(mesa.getId(), mesa.getNumero(), mesa.getCapacidad(), mesa.isDisponible());
    }

    public static UsuarioResponseDTO toUsuarioResponse(Usuario usuario) {
        return new UsuarioResponseDTO(usuario.getId(), usuario.getUsername(), usuario.getRol(), usuario.isEnabled());
    }
}
