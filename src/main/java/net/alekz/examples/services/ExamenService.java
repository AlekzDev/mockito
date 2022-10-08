package net.alekz.examples.services;

import net.alekz.examples.models.Examen;

public interface ExamenService {
    Examen findExamenByNombre(String nombre);
}
