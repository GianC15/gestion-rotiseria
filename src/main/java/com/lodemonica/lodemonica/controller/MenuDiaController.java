package com.lodemonica.lodemonica.controller;

import com.lodemonica.lodemonica.dto.DtoMapper;
import com.lodemonica.lodemonica.model.MenuDia;
import com.lodemonica.lodemonica.model.Plato;
import com.lodemonica.lodemonica.service.MenuDiaService;
import com.lodemonica.lodemonica.service.PlatoService;
import com.lodemonica.lodemonica.service.SemanaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuDiaController {

    private final MenuDiaService menuDiaService;
    private final PlatoService platoService;
    private final SemanaService semanaService;
    private final DtoMapper mapper;

    @GetMapping
    public String hoy(Model model) {
        LocalDate hoy = LocalDate.now();
        model.addAttribute("menuHoy",
                menuDiaService.obtenerPorFecha(hoy).map(mapper::toDTO).orElse(null));
        model.addAttribute("semanaActual",
                semanaService.obtenerSemanaActual().map(mapper::toDTO).orElse(null));
        model.addAttribute("principalesDisponibles",
                mapper.toPlalDTOList(platoService.obtenerPrincipales()));
        model.addAttribute("guarnicionsDisponibles",
                mapper.toPlalDTOList(platoService.obtenerGuarniciones()));
        model.addAttribute("fechaHoy", hoy.toString());
        return "menu/hoy";
    }

    @GetMapping("/{fecha}")
    public String porFecha(@PathVariable String fecha, Model model) {
        LocalDate localDate = LocalDate.parse(fecha);
        model.addAttribute("menuDia",
                menuDiaService.obtenerPorFecha(localDate).map(mapper::toDTO).orElse(null));
        model.addAttribute("fecha", localDate);
        model.addAttribute("principalesDisponibles",
                mapper.toPlalDTOList(platoService.obtenerPrincipales()));
        model.addAttribute("guarnicionsDisponibles",
                mapper.toPlalDTOList(platoService.obtenerGuarniciones()));
        return "menu/dia";
    }

    @PostMapping("/crear")
    public String crear(@RequestParam String fecha) {
        LocalDate localDate = LocalDate.parse(fecha);
        semanaService.obtenerSemanaActual().ifPresent(semana -> {
            MenuDia menuDia = new MenuDia();
            menuDia.setFecha(localDate);
            menuDia.setSemana(semana);
            menuDiaService.guardar(menuDia);
        });
        return "redirect:/menu/" + fecha;
    }

    @PostMapping("/agregar-plato")
    public String agregarPlato(@RequestParam Integer menuDiaId,
                               @RequestParam Integer platoId,
                               @RequestParam String fecha) {
        Plato plato = platoService.obtenerPorId(platoId);
        menuDiaService.agregarPlato(menuDiaId, plato);
        return "redirect:/menu/" + fecha;
    }

    @PostMapping("/quitar-plato")
    public String quitarPlato(@RequestParam Integer menuDiaId,
                              @RequestParam Integer platoId,
                              @RequestParam String fecha) {
        menuDiaService.quitarPlato(menuDiaId, platoId);
        return "redirect:/menu/" + fecha;
    }

    @GetMapping("/semana")
    public String semana(Model model) {
        semanaService.obtenerSemanaActual().ifPresent(semana -> {
            List<LocalDate> diasSemana = semana.getFechaInicio()
                    .datesUntil(semana.getFechaFin().plusDays(1))
                    .collect(Collectors.toList());

            Map<LocalDate, com.lodemonica.lodemonica.dto.MenuDiaDTO> menuPorDia =
                    new LinkedHashMap<>();
            Map<LocalDate, String> nombresDias = new LinkedHashMap<>();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd/MM",
                    new Locale("es", "AR"));

            for (LocalDate dia : diasSemana) {
                menuDiaService.obtenerPorFecha(dia).ifPresentOrElse(
                        menu -> menuPorDia.put(dia, mapper.toDTO(menu)),
                        () -> menuPorDia.put(dia, null)
                );
                String nombre = formatter.format(dia);
                nombresDias.put(dia,
                        nombre.substring(0, 1).toUpperCase() + nombre.substring(1));
            }

            model.addAttribute("semana", mapper.toDTO(semana));
            model.addAttribute("menuPorDia", menuPorDia);
            model.addAttribute("nombresDias", nombresDias);
        });

        model.addAttribute("semanaActual",
                semanaService.obtenerSemanaActual().map(mapper::toDTO).orElse(null));
        model.addAttribute("principalesDisponibles",
                mapper.toPlalDTOList(platoService.obtenerPrincipales()));
        model.addAttribute("guarnicionsDisponibles",
                mapper.toPlalDTOList(platoService.obtenerGuarniciones()));

        return "menu/semana";
    }
}