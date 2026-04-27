package com.lodemonica.lodemonica.service;

import com.lodemonica.lodemonica.model.Semana;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SemanaService {

    List<Semana> obtenerTodas();
    Semana obtenerPorId(Integer id);
    Semana guardar(Semana semana);
    Optional<Semana> obtenerSemanaActual();
}