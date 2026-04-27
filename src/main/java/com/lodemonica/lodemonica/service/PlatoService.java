package com.lodemonica.lodemonica.service;

import com.lodemonica.lodemonica.model.Plato;
import java.util.List;

public interface PlatoService {

    List<Plato> obtenerTodos();
    List<Plato> obtenerPrincipales();
    List<Plato> obtenerGuarniciones();
    Plato guardar(Plato plato);
    Plato obtenerPorId(Integer id);
    void eliminar(Integer id);
}