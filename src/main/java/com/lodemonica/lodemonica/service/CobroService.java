package com.lodemonica.lodemonica.service;

import com.lodemonica.lodemonica.model.Cobro;
import com.lodemonica.lodemonica.model.Semana;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CobroService {

    List<Cobro> obtenerPorSemana(Semana semana);
    Optional<Cobro> obtenerPorClienteYSemana(Integer clienteId, Integer semanaId);
    Cobro guardar(Cobro cobro);
    Cobro registrarPago(Integer cobroId, BigDecimal monto);
    List<Cobro> obtenerDeudores();

    Cobro recalcularTotal(Integer cobroId);
}