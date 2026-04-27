package com.lodemonica.lodemonica.dto;

import lombok.Data;

@Data
public class PedidoItemDTO {
    private Integer id;
    private Integer pedidoId;
    private PlatoDTO platoPrincipal;
    private PlatoDTO guarnicion;
    private Integer cantidad;
}