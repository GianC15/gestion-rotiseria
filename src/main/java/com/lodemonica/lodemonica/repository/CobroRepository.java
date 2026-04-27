package com.lodemonica.lodemonica.repository;

import com.lodemonica.lodemonica.model.Cobro;
import com.lodemonica.lodemonica.model.Cliente;
import com.lodemonica.lodemonica.model.Semana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CobroRepository extends JpaRepository<Cobro, Integer> {

    List<Cobro> findBySemana(Semana semana);
    Optional<Cobro> findByClienteAndSemana(Cliente cliente, Semana semana);
}