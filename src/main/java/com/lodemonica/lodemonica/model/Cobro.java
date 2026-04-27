package com.lodemonica.lodemonica.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "cobro",
        uniqueConstraints = @UniqueConstraint(columnNames = {"cliente_id", "semana_id"}))
public class Cobro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "semana_id", nullable = false)
    private Semana semana;

    @Column(name = "monto_total", nullable = false, precision = 8, scale = 2)
    private BigDecimal montoTotal = BigDecimal.ZERO;

    @Column(name = "monto_pagado", nullable = false, precision = 8, scale = 2)
    private BigDecimal montoPagado = BigDecimal.ZERO;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Modalidad modalidad;

    public enum Modalidad {
        DIA, SEMANA, FIADO
    }
}