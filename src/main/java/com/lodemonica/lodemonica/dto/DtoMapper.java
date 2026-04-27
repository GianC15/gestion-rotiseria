package com.lodemonica.lodemonica.dto;

import com.lodemonica.lodemonica.model.*;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoMapper {

    public ClienteDTO toDTO(Cliente cliente) {
        if (cliente == null) return null;
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setTelefono(cliente.getTelefono());
        dto.setActivo(cliente.getActivo());
        return dto;
    }

    public PlatoDTO toDTO(Plato plato) {
        if (plato == null) return null;
        PlatoDTO dto = new PlatoDTO();
        dto.setId(plato.getId());
        dto.setNombre(plato.getNombre());
        dto.setTipo(plato.getTipo());
        return dto;
    }

    public SemanaDTO toDTO(Semana semana) {
        if (semana == null) return null;
        SemanaDTO dto = new SemanaDTO();
        dto.setId(semana.getId());
        dto.setFechaInicio(semana.getFechaInicio());
        dto.setFechaFin(semana.getFechaFin());
        dto.setPrecioVianda(semana.getPrecioVianda());
        return dto;
    }

    public MenuDiaDTO toDTO(MenuDia menuDia) {
        if (menuDia == null) return null;
        MenuDiaDTO dto = new MenuDiaDTO();
        dto.setId(menuDia.getId());
        dto.setFecha(menuDia.getFecha());
        dto.setSemanaId(menuDia.getSemana().getId());
        dto.setPlatos(menuDia.getPlatos().stream()
                .map(this::toDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    public PedidoItemDTO toDTO(PedidoItem item) {
        if (item == null) return null;
        PedidoItemDTO dto = new PedidoItemDTO();
        dto.setId(item.getId());
        dto.setPedidoId(item.getPedido().getId());
        dto.setPlatoPrincipal(toDTO(item.getPlatoPrincipal()));
        dto.setGuarnicion(toDTO(item.getGuarnicion()));
        dto.setCantidad(item.getCantidad());
        return dto;
    }

    public PedidoDTO toDTO(Pedido pedido) {
        if (pedido == null) return null;
        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setCliente(toDTO(pedido.getCliente()));
        dto.setMenuDia(toDTO(pedido.getMenuDia()));
        dto.setItems(pedido.getItems().stream()
                .map(this::toDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    public CobroDTO toDTO(Cobro cobro) {
        if (cobro == null) return null;
        CobroDTO dto = new CobroDTO();
        dto.setId(cobro.getId());
        dto.setCliente(toDTO(cobro.getCliente()));
        dto.setSemana(toDTO(cobro.getSemana()));
        dto.setMontoTotal(cobro.getMontoTotal());
        dto.setMontoPagado(cobro.getMontoPagado());
        dto.setSaldo(cobro.getMontoTotal().subtract(cobro.getMontoPagado()));
        dto.setFechaPago(cobro.getFechaPago());
        dto.setModalidad(cobro.getModalidad());
        return dto;
    }

    public List<ClienteDTO> toClienteDTOList(List<Cliente> clientes) {
        return clientes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<PlatoDTO> toPlalDTOList(List<Plato> platos) {
        return platos.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<PedidoDTO> toPedidoDTOList(List<Pedido> pedidos) {
        return pedidos.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<CobroDTO> toCobroDTOList(List<Cobro> cobros) {
        return cobros.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<MenuDiaDTO> toMenuDiaDTOList(List<MenuDia> menus) {
        return menus.stream().map(this::toDTO).collect(Collectors.toList());
    }
}