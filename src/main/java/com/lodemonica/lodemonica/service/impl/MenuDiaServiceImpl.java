package com.lodemonica.lodemonica.service.impl;

import com.lodemonica.lodemonica.model.MenuDia;
import com.lodemonica.lodemonica.model.Plato;
import com.lodemonica.lodemonica.repository.MenuDiaRepository;
import com.lodemonica.lodemonica.service.MenuDiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuDiaServiceImpl implements MenuDiaService {

    private final MenuDiaRepository menuDiaRepository;

    @Override
    public List<MenuDia> obtenerTodos() {
        return menuDiaRepository.findAll();
    }

    @Override
    public MenuDia obtenerPorId(Integer id) {
        return menuDiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menú del día no encontrado: " + id));
    }

    @Override
    public Optional<MenuDia> obtenerPorFecha(LocalDate fecha) {
        return menuDiaRepository.findByFecha(fecha);
    }

    @Override
    public MenuDia guardar(MenuDia menuDia) {
        return menuDiaRepository.save(menuDia);
    }

    @Override
    public MenuDia agregarPlato(Integer menuDiaId, Plato plato) {
        MenuDia menuDia = obtenerPorId(menuDiaId);
        if (!menuDia.getPlatos().contains(plato)) {
            menuDia.getPlatos().add(plato);
            menuDiaRepository.save(menuDia);
        }
        return menuDia;
    }

    @Override
    public MenuDia quitarPlato(Integer menuDiaId, Integer platoId) {
        MenuDia menuDia = obtenerPorId(menuDiaId);
        menuDia.getPlatos().removeIf(p -> p.getId().equals(platoId));
        return menuDiaRepository.save(menuDia);
    }
}