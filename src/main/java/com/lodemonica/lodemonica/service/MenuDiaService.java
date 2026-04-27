package com.lodemonica.lodemonica.service;

import com.lodemonica.lodemonica.model.MenuDia;
import com.lodemonica.lodemonica.model.Plato;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MenuDiaService {

    List<MenuDia> obtenerTodos();
    MenuDia obtenerPorId(Integer id);
    Optional<MenuDia> obtenerPorFecha(LocalDate fecha);
    MenuDia guardar(MenuDia menuDia);
    MenuDia agregarPlato(Integer menuDiaId, Plato plato);
    MenuDia quitarPlato(Integer menuDiaId, Integer platoId);
}