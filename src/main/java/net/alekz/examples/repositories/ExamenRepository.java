package net.alekz.examples.repositories;

import net.alekz.examples.models.Examen;

import java.util.List;

/**
 * @author alekz
 * @Date 07/10/22
 */
public interface ExamenRepository {
    List<Examen> findAll();
}
