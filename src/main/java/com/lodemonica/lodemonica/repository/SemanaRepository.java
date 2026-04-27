package com.lodemonica.lodemonica.repository;

import com.lodemonica.lodemonica.model.Semana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SemanaRepository extends JpaRepository<Semana, Integer> {

    Optional<Semana> findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(
            LocalDate fecha1, LocalDate fecha2);
}