package com.lodemonica.lodemonica.repository;

import com.lodemonica.lodemonica.model.MenuDia;
import com.lodemonica.lodemonica.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.lodemonica.lodemonica.model.Cliente;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByMenuDia(MenuDia menuDia);
    Optional<Pedido> findByClienteAndMenuDia(Cliente cliente, MenuDia menuDia);

    @Query("SELECT COALESCE(SUM(i.cantidad), 0) FROM PedidoItem i " +
            "JOIN i.pedido p " +
            "JOIN p.menuDia md " +
            "WHERE p.cliente.id = :clienteId " +
            "AND md.semana.id = :semanaId")
    Long contarViandasPorClienteYSemana(@Param("clienteId") Integer clienteId,
                                        @Param("semanaId") Integer semanaId);
}