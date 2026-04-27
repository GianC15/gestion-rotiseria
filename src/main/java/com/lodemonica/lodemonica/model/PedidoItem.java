package com.lodemonica.lodemonica.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pedido_item")
public class PedidoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "plato_principal_id")
    private Plato platoPrincipal;

    @ManyToOne
    @JoinColumn(name = "guarnicion_id")
    private Plato guarnicion;

    @Column(nullable = false)
    private Integer cantidad = 1;
}