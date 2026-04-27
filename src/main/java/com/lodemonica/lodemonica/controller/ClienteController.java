package com.lodemonica.lodemonica.controller;

import com.lodemonica.lodemonica.dto.DtoMapper;
import com.lodemonica.lodemonica.model.Cliente;
import com.lodemonica.lodemonica.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final DtoMapper mapper;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", mapper.toClienteDTOList(clienteService.obtenerActivos()));
        model.addAttribute("clienteNuevo", new Cliente());
        return "clientes/listar";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Integer id, Model model) {
        model.addAttribute("cliente", mapper.toDTO(clienteService.obtenerPorId(id)));
        return "clientes/detalle";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Cliente cliente,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("clientes",
                    mapper.toClienteDTOList(clienteService.obtenerActivos()));
            model.addAttribute("clienteNuevo", cliente);
            return "clientes/listar";
        }
        clienteService.guardar(cliente);
        return "redirect:/clientes";
    }

    @PostMapping("/editar/{id}")
    public String editarGuardar(@PathVariable Integer id,
                                @Valid @ModelAttribute Cliente cliente,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("cliente", cliente);
            return "clientes/editar";
        }
        cliente.setId(id);
        clienteService.guardar(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        clienteService.eliminar(id);
        return "redirect:/clientes";
    }
}