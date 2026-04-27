package com.lodemonica.lodemonica.dto;

import com.lodemonica.lodemonica.model.Cobro;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CobroDTO {
    private Integer id;
    private ClienteDTO cliente;
    private SemanaDTO semana;
    private BigDecimal montoTotal;
    private BigDecimal montoPagado;
    private BigDecimal saldo;
    private LocalDate fechaPago;
    private Cobro.Modalidad modalidad;
}