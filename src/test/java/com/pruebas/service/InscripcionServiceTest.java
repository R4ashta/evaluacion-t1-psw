package com.pruebas.service;

import com.pruebas.model.Inscripcion;
import com.pruebas.model.services.InscripcionService;
import com.pruebas.repository.InscripcionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InscripcionServiceTest {

    private static final String ALUMNO = "Jimmy John Huo Pérez Espinoza";
    private static final String CODIGO = "202504498";

    @Mock
    private InscripcionRepository repository;

    @InjectMocks
    private InscripcionService service;

    private Inscripcion inscripcion;

    @BeforeEach
    void setUp() {
        System.out.println("Alumno: " + ALUMNO + " Código: " + CODIGO);
        inscripcion = new Inscripcion();
        inscripcion.setCodigoEstudiante("I2024077");
        inscripcion.setCursoId(3L);
    }

    @Test
    void inscribirEstudianteCorrectamente() {
        Inscripcion guardada = new Inscripcion();
        guardada.setId(1L);
        guardada.setCodigoEstudiante("I2024077");
        guardada.setCursoId(3L);

        when(repository.existsByCodigoEstudianteAndCursoId("I2024077", 3L)).thenReturn(false);
        when(repository.save(any(Inscripcion.class))).thenReturn(guardada);

        Inscripcion resultado = service.inscribir(inscripcion);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("I2024077", resultado.getCodigoEstudiante());
        verify(repository).save(inscripcion);
    }

    @Test
    void inscribirEstudianteYaInscritoLanzaExcepcion() {
        when(repository.existsByCodigoEstudianteAndCursoId("I2024077", 3L)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.inscribir(inscripcion));

        assertEquals("Estudiante ya inscrito", ex.getMessage());
        verify(repository, never()).save(any(Inscripcion.class));
    }

    @Test
    void cancelarInscripcionExistente() {
        inscripcion.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(inscripcion));

        assertDoesNotThrow(() -> service.cancelar(1L));
        verify(repository, times(1)).delete(inscripcion);
    }
}