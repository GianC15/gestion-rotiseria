package com.lodemonica.lodemonica.dto;

import lombok.Data;
import java.util.List;

@Data
public class PedidoDTO {
    private Integer id;
    private ClienteDTO cliente;
    private MenuDiaDTO menuDia;
    private List<PedidoItemDTO> items;
}