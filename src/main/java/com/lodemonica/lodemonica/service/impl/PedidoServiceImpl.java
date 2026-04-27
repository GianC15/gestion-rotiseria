package com.lodemonica.lodemonica.service.impl;

import com.lodemonica.lodemonica.model.Cliente;
import com.lodemonica.lodemonica.model.MenuDia;
import com.lodemonica.lodemonica.model.Pedido;
import com.lodemonica.lodemonica.model.PedidoItem;
import com.lodemonica.lodemonica.repository.ClienteRepository;
import com.lodemonica.lodemonica.repository.CobroRepository;
import com.lodemonica.lodemonica.repository.MenuDiaRepository;
import com.lodemonica.lodemonica.repository.PedidoRepository;
import com.lodemonica.lodemonica.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final MenuDiaRepository menuDiaRepository;

    private final CobroRepository cobroRepository;

    @Override
    public List<Pedido> obtenerPorMenuDia(MenuDia menuDia) {
        return pedidoRepository.findByMenuDia(menuDia);
    }

    @Override
    public Optional<Pedido> obtenerPorClienteYFecha(Integer clienteId, Integer menuDiaId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + clienteId));
        MenuDia menuDia = menuDiaRepository.findById(menuDiaId)
                .orElseThrow(() -> new RuntimeException("Menú del día no encontrado: " + menuDiaId));
        return pedidoRepository.findByClienteAndMenuDia(cliente, menuDia);
    }

    @Override
    @Transactional
    public Pedido guardar(Pedido pedido) {
        Pedido guardado = pedidoRepository.save(pedido);
        recalcularCobroSiExiste(guardado);
        return guardado;
    }

    @Override
    @Transactional
    public Pedido agregarItem(Integer pedidoId, PedidoItem item) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + pedidoId));
        item.setPedido(pedido);
        pedido.getItems().add(item);
        Pedido guardado = pedidoRepository.save(pedido);
        recalcularCobroSiExiste(guardado);
        return guardado;
    }

    @Override
    @Transactional
    public Pedido quitarItem(Integer pedidoId, Integer itemId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + pedidoId));
        pedido.getItems().removeIf(i -> i.getId().equals(itemId));
        Pedido guardado = pedidoRepository.save(pedido);
        recalcularCobroSiExiste(guardado);
        return guardado;
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        pedidoRepository.deleteById(id);
    }

    @Override
    public Map<String, Integer> obtenerResumenProduccion(List<Pedido> pedidos) {
        Map<String, Integer> resumen = new HashMap<>();
        for (Pedido pedido : pedidos) {
            for (PedidoItem item : pedido.getItems()) {
                if (item.getPlatoPrincipal() != null) {
                    String nombre = item.getPlatoPrincipal().getNombre();
                    resumen.merge(nombre, item.getCantidad(), Integer::sum);
                }
                if (item.getGuarnicion() != null) {
                    String nombre = item.getGuarnicion().getNombre();
                    resumen.merge(nombre, item.getCantidad(), Integer::sum);
                }
            }
        }
        return resumen;
    }
    @Override
    public Pedido obtenerPorId(Integer id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + id));
    }
    private void recalcularCobroSiExiste(Pedido pedido) {
        Integer clienteId = pedido.getCliente().getId();
        Integer semanaId = pedido.getMenuDia().getSemana().getId();

        cobroRepository.findByClienteAndSemana(
                pedido.getCliente(),
                pedido.getMenuDia().getSemana()
        ).ifPresent(cobro -> {
            Long totalViandas = pedidoRepository
                    .contarViandasPorClienteYSemana(clienteId, semanaId);
            BigDecimal precioVianda = cobro.getSemana().getPrecioVianda();
            cobro.setMontoTotal(precioVianda.multiply(BigDecimal.valueOf(totalViandas)));
            cobroRepository.save(cobro);
        });
    }
}