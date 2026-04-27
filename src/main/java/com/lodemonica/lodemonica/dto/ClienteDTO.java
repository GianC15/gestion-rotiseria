package com.lodemonica.lodemonica.dto;

import lombok.Data;

@Data
public class ClienteDTO {
    private Integer id;
    private String nombre;
    private String telefono;
    private Boolean activo;
}