package net.alekz.examples.repositories;

import java.util.List;

/**
 * @author alekz
 * @Date 08/10/22
 */
public interface PreguntaRepository {
    List<String> findPreguntasPorExamen(Long id);
}
