package net.alekz.examples.services;

import net.alekz.examples.exceptions.NoDataException;
import net.alekz.examples.models.Examen;
import net.alekz.examples.repositories.ExamenRepository;
import net.alekz.examples.repositories.ExamenRepositoryImpl;
import net.alekz.examples.repositories.PreguntaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExamenServiceImplTest {
    @Mock
    ExamenRepository examenRepository;
    @Mock
    PreguntaRepository preguntaRepository;
    @InjectMocks
    ExamenServiceImpl service;

    /*@BeforeEach
    void init() {
        //Para inyectar las dependencias con @Mock
        MockitoAnnotations.openMocks(this);

        // Usando Mock se puede simular los datos de entrada
        //examenRepository = mock(ExamenRepository.class);
        //preguntaRepository = mock(PreguntaRepository.class);
        //service = new ExamenServiceImpl(examenRepository, preguntaRepository);
    }

     */

    @Test
    public void findExamenByNombreTest() {
        //Contexto sin MOCK
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

    @Test
    void findPreguntasByExamenVerifyTest() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamen(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenByNameWithPreguntas("Matemáticas");
        assertEquals(4, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Trigonometría"));
        //Con el comando verify validamos que se haya ejecutado un llamado al servicio
        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamen(1L);
    }

    @Test
    void findEmptyExamenVerifyTest() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        lenient().when(preguntaRepository.findPreguntasPorExamen(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenByNameWithPreguntas("NoExists");
        verify(examenRepository).findAll();
        assertNull(examen);

    }


    @Test
    void guardarExamenTest() {
        Examen examenPreguntas = Datos.EXAMEN;
        examenPreguntas.setPreguntas(Datos.PREGUNTAS);

        when(examenRepository.guardar(any(Examen.class))).then(new Answer<Examen>() {
            Long id = 1L;

            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(id++);
                return examen;
            }
        });

        Examen examen = service.guardar(Datos.EXAMEN);

        assertNotNull(examen);
        assertEquals(1L, examen.getId());
        assertEquals("Física", examen.getNombre());
        System.out.println("Examen - preguntas " + examen.getPreguntas());
        verify(examenRepository).guardar(any(Examen.class));

    }

    @Test
    void manejoExcepcionesTest() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamen(anyLong())).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> {
            service.findExamenByNameWithPreguntas("Matemáticas");
        });
    }

    @Test
    void argumentMatchersTest() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamen(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenByNameWithPreguntas("Matemáticas");

        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamen(argThat(arg -> arg.equals(1L)));
    }

    @Test
    void argumentMatchersClassTest() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamen(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenByNameWithPreguntas("Historia");

        verify(examenRepository).findAll();
        verify(preguntaRepository).findPreguntasPorExamen(argThat(new Argumentos()));
    }

    public static class Argumentos implements ArgumentMatcher<Long>{
        @Override
        public boolean matches(Long aLong) {
            return aLong%2 == 0;
        }

        @Override
        public String toString() {
            return "Mensaje personalizado de error // el id debe ser par";
        }
    }
}
