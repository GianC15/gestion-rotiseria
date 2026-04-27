package com.lodemonica.lodemonica.service;

import com.lodemonica.lodemonica.model.Cliente;
import java.util.List;

public interface ClienteService {

    List<Cliente> obtenerTodos();
    List<Cliente> obtenerActivos();
    Cliente obtenerPorId(Integer id);
    Cliente guardar(Cliente cliente);
    void eliminar(Integer id);
}