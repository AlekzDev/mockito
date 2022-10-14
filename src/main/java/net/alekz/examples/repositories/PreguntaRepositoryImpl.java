package net.alekz.examples.repositories;

import java.util.Arrays;
import java.util.List;

/**
 * @author alekz
 * @Date 13/10/22
 */
public class PreguntaRepositoryImpl implements PreguntaRepository{
    @Override
    public List<String> findPreguntasPorExamen(Long id) {
        return Arrays.asList(
                "Aritmética",
                "Trigonometría",
                "Derivadas",
                "Integrales"
        );
    }

    @Override
    public void guardar(List<String> preguntas) {
        System.out.println("Simulamos el guardado de las preguntas...");
    }
}
