package com.lodemonica.lodemonica.controller;

import com.lodemonica.lodemonica.dto.DtoMapper;
import com.lodemonica.lodemonica.model.Plato;
import com.lodemonica.lodemonica.service.PlatoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/platos")
@RequiredArgsConstructor
public class PlatoController {

    private final PlatoService platoService;
    private final DtoMapper mapper;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("platos", mapper.toPlalDTOList(platoService.obtenerTodos()));
        model.addAttribute("platoNuevo", new Plato());
        return "platos/listar";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Plato plato,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("platos",
                    mapper.toPlalDTOList(platoService.obtenerTodos()));
            model.addAttribute("platoNuevo", plato);
            return "platos/listar";
        }
        platoService.guardar(plato);
        return "redirect:/platos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        platoService.eliminar(id);
        return "redirect:/platos";
    }
}