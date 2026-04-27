package com.lodemonica.lodemonica.controller;

import com.lodemonica.lodemonica.dto.DtoMapper;
import com.lodemonica.lodemonica.service.ClienteService;
import com.lodemonica.lodemonica.service.MenuDiaService;
import com.lodemonica.lodemonica.service.SemanaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class InicioController {

    private final SemanaService semanaService;
    private final MenuDiaService menuDiaService;
    private final ClienteService clienteService;
    private final DtoMapper mapper;

    @GetMapping("/")
    public String inicio(Model model) {
        LocalDate hoy = LocalDate.now();

        model.addAttribute("semanaActual",
                semanaService.obtenerSemanaActual().map(mapper::toDTO).orElse(null));
        model.addAttribute("menuHoy",
                menuDiaService.obtenerPorFecha(hoy).map(mapper::toDTO).orElse(null));
        model.addAttribute("totalClientes",
                clienteService.obtenerActivos().size());
        model.addAttribute("fechaHoy", hoy);

        return "index";
    }
}