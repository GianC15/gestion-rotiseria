package com.lodemonica.lodemonica.service.impl;

import com.lodemonica.lodemonica.model.Cliente;
import com.lodemonica.lodemonica.model.Cobro;
import com.lodemonica.lodemonica.model.Semana;
import com.lodemonica.lodemonica.repository.ClienteRepository;
import com.lodemonica.lodemonica.repository.CobroRepository;
import com.lodemonica.lodemonica.repository.PedidoRepository;
import com.lodemonica.lodemonica.repository.SemanaRepository;
import com.lodemonica.lodemonica.service.CobroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CobroServiceImpl implements CobroService {

    private final CobroRepository cobroRepository;
    private final ClienteRepository clienteRepository;
    private final SemanaRepository semanaRepository;

    private final PedidoRepository pedidoRepository;

    @Override
    public List<Cobro> obtenerPorSemana(Semana semana) {
        return cobroRepository.findBySemana(semana);
    }

    @Override
    public Optional<Cobro> obtenerPorClienteYSemana(Integer clienteId, Integer semanaId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + clienteId));
        Semana semana = semanaRepository.findById(semanaId)
                .orElseThrow(() -> new RuntimeException("Semana no encontrada: " + semanaId));
        return cobroRepository.findByClienteAndSemana(cliente, semana);
    }

    @Override
    @Transactional
    public Cobro guardar(Cobro cobro) {
        return cobroRepository.save(cobro);
    }

    @Override
    @Transactional
    public Cobro registrarPago(Integer cobroId, BigDecimal monto) {
        Cobro cobro = cobroRepository.findById(cobroId)
                .orElseThrow(() -> new RuntimeException("Cobro no encontrado: " + cobroId));

        BigDecimal nuevoPagado = cobro.getMontoPagado().add(monto);

        if (nuevoPagado.compareTo(cobro.getMontoTotal()) > 0) {
            throw new RuntimeException("El monto pagado supera el total del cobro");
        }

        cobro.setMontoPagado(nuevoPagado);
        cobro.setFechaPago(LocalDate.now());

        if (nuevoPagado.compareTo(cobro.getMontoTotal()) == 0) {
            cobro.setModalidad(Cobro.Modalidad.DIA);
        }

        return cobroRepository.save(cobro);
    }

    @Override
    public List<Cobro> obtenerDeudores() {
        return cobroRepository.findAll().stream()
                .filter(c -> c.getMontoPagado().compareTo(c.getMontoTotal()) < 0)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Cobro recalcularTotal(Integer cobroId) {
        Cobro cobro = cobroRepository.findById(cobroId)
                .orElseThrow(() -> new RuntimeException("Cobro no encontrado: " + cobroId));

        BigDecimal precioVianda = cobro.getSemana().getPrecioVianda();

        long totalViandas = cobro.getSemana().getId() != null ?
                pedidoRepository.contarViandasPorClienteYSemana(
                        cobro.getCliente().getId(),
                        cobro.getSemana().getId()
                ) : 0L;

        cobro.setMontoTotal(precioVianda.multiply(BigDecimal.valueOf(totalViandas)));
        return cobroRepository.save(cobro);
    }
}