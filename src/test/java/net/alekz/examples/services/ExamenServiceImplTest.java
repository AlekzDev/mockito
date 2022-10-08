package net.alekz.examples.services;

import net.alekz.examples.exceptions.NoDataException;
import net.alekz.examples.models.Examen;
import net.alekz.examples.repositories.ExamenRepository;
import net.alekz.examples.repositories.ExamenRepositoryImpl;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExamenServiceImplTest {
    @Test
    public void findExamenByNombreTest() {
        //Contexto
        ExamenRepository repository = new ExamenRepositoryImpl();
        ExamenService service = new ExamenServiceImpl(repository);
        Examen examen = service.findExamenByNombre("Matemáticas");

        assertNotNull(examen);
        assertEquals(1L, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
    }

    @Test
    public void findExamenByNombreMockTest() {
        // Usando Mock se puede similar los datos de entrada
        ExamenRepository repository = mock(ExamenRepository.class);
        ExamenService service = new ExamenServiceImpl(repository);
        List<Examen> datos = Arrays.asList(
                new Examen(1L, "Matemáticas"),
                new Examen(2L, "Historia"),
                new Examen(3L, "Español"),
                new Examen(4L, "Geografía"),
                new Examen(5L, "Etimología"),
                new Examen(6L, "Química")
        );

        when(repository.findAll()).thenReturn(datos);
        Examen examen = service.findExamenByNombre("Etimología");

        assertNotNull(examen);
        assertEquals(5L, examen.getId());
        assertEquals("Etimología", examen.getNombre());
    }

    @Test
    public void findExamenByNombreEmptyTest() {
        ExamenRepository repository = mock(ExamenRepository.class);
        ExamenService service = new ExamenServiceImpl(repository);

        when(repository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(NoDataException.class, () -> {
            service.findExamenByNombre("Matemáticas");
        });

    }
}
