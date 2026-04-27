package com.lodemonica.lodemonica.service;

import com.lodemonica.lodemonica.model.MenuDia;
import com.lodemonica.lodemonica.model.Pedido;
import com.lodemonica.lodemonica.model.PedidoItem;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PedidoService {

    List<Pedido> obtenerPorMenuDia(MenuDia menuDia);
    Optional<Pedido> obtenerPorClienteYFecha(Integer clienteId, Integer menuDiaId);


    Map<String, Integer> obtenerResumenProduccion(List<Pedido> pedidos);
    Pedido guardar(Pedido pedido);
    Pedido agregarItem(Integer pedidoId, PedidoItem item);
    Pedido quitarItem(Integer pedidoId, Integer itemId);
    void eliminar(Integer id);
    Pedido obtenerPorId(Integer id);
}