package com.lodemonica.lodemonica.controller;

import com.lodemonica.lodemonica.dto.DtoMapper;
import com.lodemonica.lodemonica.model.*;
import com.lodemonica.lodemonica.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final ClienteService clienteService;
    private final MenuDiaService menuDiaService;
    private final PlatoService platoService;
    private final SemanaService semanaService;
    private final DtoMapper mapper;

    @GetMapping("/dia/{fecha}")
    public String porDia(@PathVariable String fecha, Model model) {
        LocalDate localDate = LocalDate.parse(fecha);

        menuDiaService.obtenerPorFecha(localDate).ifPresent(menuDia -> {
            List<Pedido> pedidos = pedidoService.obtenerPorMenuDia(menuDia);

            List<Plato> principalesDelDia = menuDia.getPlatos().stream()
                    .filter(p -> p.getTipo() == Plato.TipoPlato.PRINCIPAL)
                    .collect(Collectors.toList());

            List<Plato> guarnicionsDelDia = menuDia.getPlatos().stream()
                    .filter(p -> p.getTipo() == Plato.TipoPlato.GUARNICION)
                    .collect(Collectors.toList());

            model.addAttribute("menuDia", mapper.toDTO(menuDia));
            model.addAttribute("pedidos", mapper.toPedidoDTOList(pedidos));
            model.addAttribute("resumenProduccion",
                    pedidoService.obtenerResumenProduccion(pedidos));
            model.addAttribute("principalesDisponibles",
                    mapper.toPlalDTOList(principalesDelDia));
            model.addAttribute("guarnicionsDisponibles",
                    mapper.toPlalDTOList(guarnicionsDelDia));
        });

        model.addAttribute("clientes",
                mapper.toClienteDTOList(clienteService.obtenerActivos()));
        model.addAttribute("fecha", localDate);
        return "pedidos/dia";
    }

    @PostMapping("/guardar")
    public String guardar(@RequestParam Integer clienteId,
                          @RequestParam Integer menuDiaId,
                          @RequestParam Integer platoPrincipalId,
                          @RequestParam(required = false) Integer guarnicionId,
                          @RequestParam Integer cantidad,
                          @RequestParam String fecha,
                          @RequestParam(required = false, defaultValue = "dia") String origen) {

        MenuDia menuDia = menuDiaService.obtenerPorId(menuDiaId);
        Cliente cliente = clienteService.obtenerPorId(clienteId);

        Pedido pedido = pedidoService.obtenerPorClienteYFecha(clienteId, menuDiaId)
                .orElseGet(() -> {
                    Pedido nuevo = new Pedido();
                    nuevo.setCliente(cliente);
                    nuevo.setMenuDia(menuDia);
                    nuevo.setItems(new ArrayList<>());
                    return pedidoService.guardar(nuevo);
                });

        PedidoItem item = new PedidoItem();
        item.setPlatoPrincipal(platoService.obtenerPorId(platoPrincipalId));
        if (guarnicionId != null) {
            item.setGuarnicion(platoService.obtenerPorId(guarnicionId));
        }
        item.setCantidad(cantidad);
        pedidoService.agregarItem(pedido.getId(), item);

        if (origen.equals("semana")) {
            return "redirect:/pedidos/semana/" + clienteId;
        }
        return "redirect:/pedidos/dia/" + fecha;
    }

    @GetMapping("/quitar-item/{pedidoId}/{itemId}")
    public String quitarItem(@PathVariable Integer pedidoId,
                             @PathVariable Integer itemId,
                             @RequestParam(required = false) String fecha) {
        Pedido pedido = pedidoService.obtenerPorId(pedidoId);
        if (fecha == null) {
            fecha = pedido.getMenuDia().getFecha().toString();
        }
        pedidoService.quitarItem(pedidoId, itemId);
        return "redirect:/pedidos/dia/" + fecha;
    }

    @GetMapping("/semana/{clienteId}")
    public String pedidoSemana(@PathVariable Integer clienteId, Model model) {
        Cliente cliente = clienteService.obtenerPorId(clienteId);

        semanaService.obtenerSemanaActual().ifPresent(semana -> {
            List<LocalDate> diasSemana = semana.getFechaInicio()
                    .datesUntil(semana.getFechaFin().plusDays(1))
                    .collect(Collectors.toList());

            Map<LocalDate, com.lodemonica.lodemonica.dto.MenuDiaDTO> menuPorDia =
                    new LinkedHashMap<>();
            Map<LocalDate, List<com.lodemonica.lodemonica.dto.PedidoDTO>> pedidosPorDia =
                    new LinkedHashMap<>();
            Map<LocalDate, String> nombresDias = new LinkedHashMap<>();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd/MM",
                    new Locale("es", "AR"));

            for (LocalDate dia : diasSemana) {
                menuDiaService.obtenerPorFecha(dia).ifPresentOrElse(
                        menu -> {
                            menuPorDia.put(dia, mapper.toDTO(menu));
                            pedidosPorDia.put(dia,
                                    mapper.toPedidoDTOList(pedidoService.obtenerPorMenuDia(menu)));
                        },
                        () -> {
                            menuPorDia.put(dia, null);
                            pedidosPorDia.put(dia, new ArrayList<>());
                        }
                );
                String nombre = formatter.format(dia);
                nombresDias.put(dia,
                        nombre.substring(0, 1).toUpperCase() + nombre.substring(1));
            }

            model.addAttribute("semana", mapper.toDTO(semana));
            model.addAttribute("menuPorDia", menuPorDia);
            model.addAttribute("pedidosPorDia", pedidosPorDia);
            model.addAttribute("nombresDias", nombresDias);
        });

        model.addAttribute("cliente", mapper.toDTO(cliente));
        model.addAttribute("semanaActual",
                semanaService.obtenerSemanaActual().map(mapper::toDTO).orElse(null));
        return "pedidos/semana-cliente";
    }
}