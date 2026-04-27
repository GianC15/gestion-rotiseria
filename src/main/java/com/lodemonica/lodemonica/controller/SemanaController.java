package com.lodemonica.lodemonica.controller;

import com.lodemonica.lodemonica.dto.DtoMapper;
import com.lodemonica.lodemonica.model.Semana;
import com.lodemonica.lodemonica.service.SemanaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/semanas")
@RequiredArgsConstructor
public class SemanaController {

    private final SemanaService semanaService;
    private final DtoMapper mapper;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("semanas",
                semanaService.obtenerTodas().stream()
                        .map(mapper::toDTO)
                        .collect(java.util.stream.Collectors.toList()));
        model.addAttribute("semanaActual",
                semanaService.obtenerSemanaActual().map(mapper::toDTO).orElse(null));
        model.addAttribute("semaNueva", new Semana());
        return "semanas/listar";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Semana semana,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("semanas",
                    semanaService.obtenerTodas().stream()
                            .map(mapper::toDTO)
                            .collect(java.util.stream.Collectors.toList()));
            model.addAttribute("semanaActual",
                    semanaService.obtenerSemanaActual().map(mapper::toDTO).orElse(null));
            model.addAttribute("semaNueva", semana);
            return "semanas/listar";
        }
        semanaService.guardar(semana);
        return "redirect:/semanas";
    }
}