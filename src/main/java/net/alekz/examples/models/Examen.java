package net.alekz.examples.models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alekz
 * @Date 07/10/22
 */
public class Examen {
    private int id;
    private String nombre;
    private List<String> preguntas;

    public Examen(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.preguntas = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<String> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<String> preguntas) {
        this.preguntas = preguntas;
    }
}
