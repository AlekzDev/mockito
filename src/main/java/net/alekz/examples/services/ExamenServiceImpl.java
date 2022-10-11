package net.alekz.examples.services;

import net.alekz.examples.exceptions.NoDataException;
import net.alekz.examples.models.Examen;
import net.alekz.examples.repositories.ExamenRepository;
import net.alekz.examples.repositories.PreguntaRepository;
import net.bytebuddy.implementation.bytecode.Throw;

import java.util.List;
import java.util.Optional;

/**
 * @author alekz
 * @Date 07/10/22
 */
public class ExamenServiceImpl implements ExamenService {
    private final ExamenRepository examenRepository;
    private final PreguntaRepository preguntaRepository;

    public ExamenServiceImpl(ExamenRepository examenRepository, PreguntaRepository preguntaRepository) {
        this.examenRepository = examenRepository;
        this.preguntaRepository = preguntaRepository;
    }

    @Override
    public Examen findExamenByNombre(String nombre) throws NoDataException {
        Optional<Examen> examenOptional = examenRepository.findAll()
                .stream()
                .filter(e -> e.getNombre().equals(nombre))
                .findFirst();
        return examenOptional.orElseThrow(() -> new NoDataException("Examen no encontrado"));
    }

    @Override
    public Examen findExamenByNameWithPreguntas(String nombre) {
        try {
            Examen examen = findExamenByNombre(nombre);
            List<String> preguntas = preguntaRepository.findPreguntasPorExamen(examen.getId());
            examen.setPreguntas(preguntas);
            return examen;
        }catch (NoDataException nde){
            //System.err.println("No se encontró examen: " + nombre);
            return null;
        }
    }

    @Override
    public Examen guardar(Examen examen) {
        if(!examen.getPreguntas().isEmpty())
            preguntaRepository.guardar(examen.getPreguntas());

        return examenRepository.guardar(examen);
    }
}
