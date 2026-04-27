package com.lodemonica.lodemonica.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "menu_dia")
public class MenuDia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "semana_id", nullable = false)
    private Semana semana;

    @Column(nullable = false, unique = true)
    private LocalDate fecha;

    @ManyToMany
    @JoinTable(
            name = "menu_dia_plato",
            joinColumns = @JoinColumn(name = "menu_dia_id"),
            inverseJoinColumns = @JoinColumn(name = "plato_id")
    )
    private List<Plato> platos;
}