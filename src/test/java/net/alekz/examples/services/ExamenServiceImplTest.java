package net.alekz.examples.services;

import net.alekz.examples.exceptions.NoDataException;
import net.alekz.examples.models.Examen;
import net.alekz.examples.repositories.ExamenRepository;
import net.alekz.examples.repositories.ExamenRepositoryImpl;
import net.alekz.examples.repositories.PreguntaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExamenServiceImplTest {
    ExamenRepository examenRepository;
    PreguntaRepository preguntaRepository;
    ExamenService service;

    @BeforeEach
    void init() {
        // Usando Mock se puede similar los datos de entrada
        examenRepository = mock(ExamenRepository.class);
        preguntaRepository = mock(PreguntaRepository.class);
        service = new ExamenServiceImpl(examenRepository, preguntaRepository);
    }

    @Test
    public void findExamenByNombreTest() {
        //Contexto
        ExamenRepository repository = new ExamenRepositoryImpl();
        ExamenService service = new ExamenServiceImpl(repository, null);
        Examen examen = service.findExamenByNombre("Matemáticas");

        assertNotNull(examen);
        assertEquals(1L, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
    }

    @Test
    public void findExamenByNombreMockTest() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        Examen examen = service.findExamenByNombre("Etimología");

        assertNotNull(examen);
        assertEquals(5L, examen.getId());
        assertEquals("Etimología", examen.getNombre());
    }

    @Test
    public void findExamenByNombreEmptyTest() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES_EMPTY);
        assertThrows(NoDataException.class, () -> {
            service.findExamenByNombre("Matemáticas");
        });
    }

    @Test
    void findPreguntasByExamenTest() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamen(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenByNameWithPreguntas("Matemáticas");
        assertEquals(4, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Trigonometría"));
    }
}
