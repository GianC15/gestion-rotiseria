package com.lodemonica.lodemonica.repository;

import com.lodemonica.lodemonica.model.MenuDia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MenuDiaRepository extends JpaRepository<MenuDia, Integer> {

    Optional<MenuDia> findByFecha(LocalDate fecha);
}