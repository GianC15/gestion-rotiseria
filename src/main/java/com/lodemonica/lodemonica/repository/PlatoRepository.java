package com.lodemonica.lodemonica.repository;

import com.lodemonica.lodemonica.model.Plato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlatoRepository extends JpaRepository<Plato, Integer> {

    List<Plato> findByTipo(Plato.TipoPlato tipo);
}