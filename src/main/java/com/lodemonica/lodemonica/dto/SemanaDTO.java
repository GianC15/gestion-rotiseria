package com.lodemonica.lodemonica.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SemanaDTO {
    private Integer id;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal precioVianda;
}