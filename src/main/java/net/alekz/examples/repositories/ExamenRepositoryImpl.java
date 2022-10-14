package net.alekz.examples.repositories;

import net.alekz.examples.models.Examen;

import java.util.Arrays;
import java.util.List;

/**
 * @author alekz
 * @Date 07/10/22
 */
public class ExamenRepositoryImpl implements ExamenRepository{
    @Override
    public List<Examen> findAll() {
        return Arrays.asList(
                new Examen(1L,"Matemáticas"),
                new Examen(2L,"Historia"),
                new Examen(3L,"Español")
        );
    }

    @Override
    public Examen guardar(Examen examen) {
        return new Examen(1L, "Matemáticas");
    }
}
