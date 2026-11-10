package gm.inventoryproject.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para DuplicateFieldException.
 * <p>
 * Verifica que la excepción conserve el mensaje y sea RuntimeException.
 * </p>
 */
@DisplayName("Tests de DuplicateFieldException")
class DuplicateFieldExceptionTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "El email ya está registrado",
        "El teléfono ya está registrado",
        "El nombre ya está registrado"
    })
    @DisplayName("Constructor conserva el mensaje de la excepción")
    void testConstructorConservesMessage(String message) {
        // Act
        DuplicateFieldException exception = new DuplicateFieldException(message);

        // Assert
        assertEquals(message, exception.getMessage(), "El mensaje debería ser conservado");
    }

    @Test
    @DisplayName("La excepción es RuntimeException")
    void testIsRuntimeException() {
        // Arrange
        DuplicateFieldException exception = new DuplicateFieldException("Test");

        // Assert
        assertTrue(exception instanceof RuntimeException, "Debería ser RuntimeException");
    }

    @Test
    @DisplayName("Constructor con mensaje vacío crea excepción válida")
    void testConstructorWithEmptyMessage() {
        // Act
        DuplicateFieldException exception = new DuplicateFieldException("");

        // Assert
        assertEquals("", exception.getMessage(), "El mensaje vacío debería ser conservado");
    }
}
