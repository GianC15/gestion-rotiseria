package com.lodemonica.lodemonica.service.impl;

import com.lodemonica.lodemonica.model.Plato;
import com.lodemonica.lodemonica.repository.PlatoRepository;
import com.lodemonica.lodemonica.service.PlatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlatoServiceImpl implements PlatoService {

    private final PlatoRepository platoRepository;

    @Override
    public List<Plato> obtenerTodos() {
        return platoRepository.findAll();
    }

    @Override
    public List<Plato> obtenerPrincipales() {
        return platoRepository.findByTipo(Plato.TipoPlato.PRINCIPAL);
    }

    @Override
    public List<Plato> obtenerGuarniciones() {
        return platoRepository.findByTipo(Plato.TipoPlato.GUARNICION);
    }

    @Override
    public Plato guardar(Plato plato) {
        return platoRepository.save(plato);
    }

    @Override
    public Plato obtenerPorId(Integer id) {
        return platoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado: " + id));
    }

    @Override
    public void eliminar(Integer id) {
        platoRepository.deleteById(id);
    }
}