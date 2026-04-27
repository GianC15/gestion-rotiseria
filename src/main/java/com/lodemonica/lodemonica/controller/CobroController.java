package com.lodemonica.lodemonica.controller;

import com.lodemonica.lodemonica.dto.DtoMapper;
import com.lodemonica.lodemonica.model.Cobro;
import com.lodemonica.lodemonica.model.Cliente;
import com.lodemonica.lodemonica.model.Semana;
import com.lodemonica.lodemonica.service.ClienteService;
import com.lodemonica.lodemonica.service.CobroService;
import com.lodemonica.lodemonica.service.SemanaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cobros")
@RequiredArgsConstructor
public class CobroController {

    private final CobroService cobroService;
    private final ClienteService clienteService;
    private final SemanaService semanaService;
    private final DtoMapper mapper;

    @GetMapping
    public String semanaActual(Model model) {
        semanaService.obtenerSemanaActual().ifPresent(semana -> {
            List<Cobro> cobros = cobroService.obtenerPorSemana(semana);

            BigDecimal totalCobrado = cobros.stream()
                    .map(Cobro::getMontoPagado)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalAdeudado = cobros.stream()
                    .map(c -> c.getMontoTotal().subtract(c.getMontoPagado()))
                    .filter(s -> s.compareTo(BigDecimal.ZERO) > 0)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            model.addAttribute("semana", mapper.toDTO(semana));
            model.addAttribute("cobros", mapper.toCobroDTOList(cobros));
            model.addAttribute("totalCobrado", totalCobrado);
            model.addAttribute("totalAdeudado", totalAdeudado);
        });

        model.addAttribute("clientes",
                mapper.toClienteDTOList(clienteService.obtenerActivos()));
        model.addAttribute("semanaActual",
                semanaService.obtenerSemanaActual().map(mapper::toDTO).orElse(null));
        return "cobros/semana";
    }

    @PostMapping("/registrar")
    public String registrar(@RequestParam Integer clienteId,
                            @RequestParam Integer semanaId,
                            @RequestParam String modalidad) {
        Semana semana = semanaService.obtenerPorId(semanaId);
        Cliente cliente = clienteService.obtenerPorId(clienteId);

        Cobro cobro = cobroService.obtenerPorClienteYSemana(clienteId, semanaId)
                .orElseGet(() -> {
                    Cobro nuevo = new Cobro();
                    nuevo.setCliente(cliente);
                    nuevo.setSemana(semana);
                    nuevo.setModalidad(Cobro.Modalidad.valueOf(modalidad));
                    nuevo.setMontoTotal(BigDecimal.ZERO);
                    nuevo.setMontoPagado(BigDecimal.ZERO);
                    return cobroService.guardar(nuevo);
                });

        cobroService.recalcularTotal(cobro.getId());
        return "redirect:/cobros";
    }

    @PostMapping("/pago/{id}")
    public String registrarPago(@PathVariable Integer id,
                                @RequestParam BigDecimal monto) {
        cobroService.registrarPago(id, monto);
        return "redirect:/cobros";
    }

    @GetMapping("/recalcular/{id}")
    public String recalcular(@PathVariable Integer id) {
        cobroService.recalcularTotal(id);
        return "redirect:/cobros";
    }

    @GetMapping("/deudores")
    public String deudores(Model model) {
        List<Cobro> deudores = cobroService.obtenerDeudores();

        BigDecimal totalDeuda = deudores.stream()
                .map(c -> c.getMontoTotal().subtract(c.getMontoPagado()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("deudores", mapper.toCobroDTOList(deudores));
        model.addAttribute("totalDeuda", totalDeuda);
        return "cobros/deudores";
    }
}