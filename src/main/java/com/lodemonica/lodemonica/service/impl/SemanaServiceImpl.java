package com.lodemonica.lodemonica.service.impl;

import com.lodemonica.lodemonica.model.Semana;
import com.lodemonica.lodemonica.repository.SemanaRepository;
import com.lodemonica.lodemonica.service.SemanaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SemanaServiceImpl implements SemanaService {

    private final SemanaRepository semanaRepository;

    @Override
    public List<Semana> obtenerTodas() {
        return semanaRepository.findAll();
    }

    @Override
    public Semana obtenerPorId(Integer id) {
        return semanaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Semana no encontrada: " + id));
    }

    @Override
    public Semana guardar(Semana semana) {
        return semanaRepository.save(semana);
    }

    @Override
    public Optional<Semana> obtenerSemanaActual() {
        LocalDate hoy = LocalDate.now();
        return semanaRepository
                .findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(hoy, hoy);
    }
}