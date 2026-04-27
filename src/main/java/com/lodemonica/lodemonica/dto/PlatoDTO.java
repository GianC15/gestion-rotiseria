package com.lodemonica.lodemonica.dto;

import com.lodemonica.lodemonica.model.Plato;
import lombok.Data;

@Data
public class PlatoDTO {
    private Integer id;
    private String nombre;
    private Plato.TipoPlato tipo;
}