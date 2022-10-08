package net.alekz.examples.services;

import net.alekz.examples.models.Examen;
import net.alekz.examples.repositories.ExamenRepository;

import java.util.Optional;

/**
 * @author alekz
 * @Date 07/10/22
 */
public class ExamenServiceImpl implements ExamenService {
    private ExamenRepository examenRepository;

    public ExamenServiceImpl(ExamenRepository examenRepository) {
        this.examenRepository = examenRepository;
    }

    @Override
    public Examen findExamenByNombre(String nombre) {
        Optional<Examen> examenOptional = examenRepository.findAll()
                .stream()
                .filter(e -> e.getNombre().contains(nombre))
                .findFirst();
        return examenOptional.orElse(null);
    }
}
