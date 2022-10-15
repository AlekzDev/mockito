package net.alekz.examples.services;

import net.alekz.examples.exceptions.NoDataException;
import net.alekz.examples.models.Examen;
import net.alekz.examples.repositories.ExamenRepository;
import net.alekz.examples.repositories.ExamenRepositoryImpl;
import net.alekz.examples.repositories.PreguntaRepository;
import net.alekz.examples.repositories.PreguntaRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExamenServiceImplTest {
    @Mock
    ExamenRepositoryImpl examenRepository;
    @Mock
    PreguntaRepositoryImpl preguntaRepository;
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

    public static class Argumentos implements ArgumentMatcher<Long> {
        @Override
        public boolean matches(Long aLong) {
            return aLong % 2 == 0;
        }

        @Override
        public String toString() {
            return "Mensaje personalizado de error // el id debe ser par";
        }
    }

    @Test
    public void testArgumentCaptor() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        service.findExamenByNameWithPreguntas("Historia");

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(preguntaRepository).findPreguntasPorExamen(captor.capture());

        assertEquals(2L, captor.getValue());
    }

    @Test
    void testDoThrow() {
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);

        doThrow(IllegalArgumentException.class).when(preguntaRepository).guardar(anyList());

        assertThrows(IllegalArgumentException.class, () -> {
            service.guardar(examen);
        });
    }

    @Test
    void testDoAnswer() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        doAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id == 1L ? Datos.PREGUNTAS : Collections.emptyList();
        }).when(preguntaRepository).findPreguntasPorExamen(anyLong());

        Examen examen = service.findExamenByNameWithPreguntas("Matemáticas");
        assertEquals(4, examen.getPreguntas().size());
    }

    @Test
    void doAnswerGuardarExamenTest() {
        Examen examenPreguntas = Datos.EXAMEN;
        examenPreguntas.setPreguntas(Datos.PREGUNTAS);

        doAnswer(new Answer<Examen>() {
            Long id = 1L;

            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(id++);
                return examen;
            }
        }).when(examenRepository).guardar(any(Examen.class));

        Examen examen = service.guardar(Datos.EXAMEN);

        assertNotNull(examen);
        assertEquals(1L, examen.getId());
        assertEquals("Física", examen.getNombre());
        System.out.println("Examen - preguntas " + examen.getPreguntas());
        verify(examenRepository).guardar(any(Examen.class));

    }

    @Test
    void testDoCallRealMethod() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        doCallRealMethod().when(preguntaRepository).findPreguntasPorExamen(anyLong());

        Examen examen = service.findExamenByNameWithPreguntas("Matemáticas");
        assertNotNull(examen);
        assertEquals(1L, examen.getId());
        assertTrue(examen.getPreguntas().contains("Trigonometría"));
    }


    @Test
    void testSpy() {
        //Con test invocamos al método real o al mock cuando usamos when
        ExamenRepository examenRepository = spy(ExamenRepositoryImpl.class);
        PreguntaRepository preguntaRepository = spy(PreguntaRepositoryImpl.class);
        ExamenService examenService = new ExamenServiceImpl(examenRepository, preguntaRepository);

        //Siempre usar Do's con spy
        doReturn(Datos.PREGUNTAS).when(preguntaRepository).findPreguntasPorExamen(anyLong());


        Examen examen = examenService.findExamenByNameWithPreguntas("Matemáticas");
        assertNotNull(examen);
        assertEquals(1L, examen.getId());
        assertTrue(examen.getPreguntas().contains("Trigonometría"));

    }

    @Test
    void testOrder1() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        service.findExamenByNameWithPreguntas("Matemáticas");
        service.findExamenByNameWithPreguntas("Historia");

        InOrder inOrder = inOrder(preguntaRepository);
        inOrder.verify(preguntaRepository).findPreguntasPorExamen(1L);
        inOrder.verify(preguntaRepository).findPreguntasPorExamen(2L);

    }

    @Test
    void testOrder2() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        service.findExamenByNameWithPreguntas("Matemáticas");
        service.findExamenByNameWithPreguntas("Historia");

        InOrder inOrder = inOrder(examenRepository, preguntaRepository);
        inOrder.verify(examenRepository).findAll();
        inOrder.verify(preguntaRepository).findPreguntasPorExamen(1L);
        inOrder.verify(examenRepository).findAll();
        inOrder.verify(preguntaRepository).findPreguntasPorExamen(2L);

    }


    @Test
    void testTimes() {
        when(examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        service.findExamenByNameWithPreguntas("Matemáticas");

        verify(preguntaRepository).findPreguntasPorExamen(anyLong());
        verify(preguntaRepository, times(1)).findPreguntasPorExamen(anyLong());
        verify(preguntaRepository, atLeast(1)).findPreguntasPorExamen(anyLong());
        verify(preguntaRepository, atLeastOnce()).findPreguntasPorExamen(anyLong());
        verify(preguntaRepository, atMost(1)).findPreguntasPorExamen(anyLong());
        verify(preguntaRepository, atMostOnce()).findPreguntasPorExamen(anyLong());
    }

    @Test
    void testTimesNever() {
        when(examenRepository.findAll()).thenReturn(Collections.emptyList());
        service.findExamenByNameWithPreguntas("Matemáticas");

        verify(preguntaRepository,never()).findPreguntasPorExamen(anyLong());
        verifyNoInteractions(preguntaRepository);
    }
}
