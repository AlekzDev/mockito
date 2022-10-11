package net.alekz.examples.services;

import net.alekz.examples.models.Examen;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author alekz
 * @Date 08/10/22
 */
public class Datos {
    public final static List<Examen> EXAMENES = Arrays.asList(
            new Examen(1L, "Matemáticas"),
                new Examen(2L, "Historia"),
                new Examen(3L, "Español"),
                new Examen(4L, "Geografía"),
                new Examen(5L, "Etimología"),
                new Examen(6L, "Química")
        );

    public final static List<Examen> EXAMENES_EMPTY = Collections.emptyList();

    public final static List<String> PREGUNTAS = Arrays.asList(
            "Aritmética",
            "Trigonometría",
            "Derivadas",
            "Integrales"
        );

    public static final Examen EXAMEN = new Examen(7L, "Física");

}
