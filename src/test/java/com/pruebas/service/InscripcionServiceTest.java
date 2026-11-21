package com.pruebas.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pruebas.model.Inscripcion;
import com.pruebas.repository.InscripcionRepository;
import com.pruebas.model.services.InscripcionService;

@ExtendWith(MockitoExtension.class)
class InscripcionServiceTest {

    @Mock
    private InscripcionRepository repository;

    @InjectMocks
    private InscripcionService service;

    @Test
    @DisplayName("Caso 1: Inscribir estudiante correctamente")
    void inscribirEstudianteCorrectamente() {

        Inscripcion nueva = new Inscripcion();
        nueva.setCodigoEstudiante("I2024077");
        nueva.setCursoId(3L);

        Inscripcion guardada = new Inscripcion();
        guardada.setId(1L);
        guardada.setCodigoEstudiante("I2024077");
        guardada.setCursoId(3L);

        when(repository.existsByCodigoEstudianteAndCursoId("I2024077", 3L)).thenReturn(false);
        when(repository.save(any(Inscripcion.class))).thenReturn(guardada);


        Inscripcion resultado = service.inscribir(nueva);


        assertNotNull(resultado, "El resultado no debe ser nulo");
        assertEquals("I2024077", resultado.getCodigoEstudiante());
        assertEquals(3L, resultado.getCursoId());
        verify(repository, times(1)).save(nueva);
    }

    @Test
    @DisplayName("Caso 2: Error al inscribir estudiante ya inscrito")
    void inscribirEstudianteYaInscrito() {

        Inscripcion duplicada = new Inscripcion();
        duplicada.setCodigoEstudiante("I2024077");
        duplicada.setCursoId(3L);

        when(repository.existsByCodigoEstudianteAndCursoId("I2024077", 3L)).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.inscribir(duplicada);
        });

        assertEquals("Estudiante ya inscrito", exception.getMessage());
        verify(repository, never()).save(any(Inscripcion.class));
    }

    @Test
    @DisplayName("Caso 3: Cancelar una inscripción existente")
    void cancelarInscripcionExistente() {

        Inscripcion existente = new Inscripcion();
        existente.setId(1L);
        existente.setCodigoEstudiante("I2024077");
        existente.setCursoId(3L);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));


        assertDoesNotThrow(() -> service.cancelar(1L));
        verify(repository, times(1)).delete(existente);
    }
}