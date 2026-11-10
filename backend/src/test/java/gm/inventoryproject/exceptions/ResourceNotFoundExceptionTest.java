package gm.inventoryproject.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para ResourceNotFoundException.
 * <p>
 * Verifica que la excepción conserve el mensaje y sea RuntimeException.
 * </p>
 */
@DisplayName("Tests de ResourceNotFoundException")
class ResourceNotFoundExceptionTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "No se encontró un producto con ID: 1",
        "No se encontró una categoría con ID: 2",
        "No se encontró un proveedor con ID: 3"
    })
    @DisplayName("Constructor conserva el mensaje de la excepción")
    void testConstructorConservesMessage(String message) {
        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertEquals(message, exception.getMessage(), "El mensaje debería ser conservado");
    }

    @Test
    @DisplayName("La excepción es RuntimeException")
    void testIsRuntimeException() {
        // Arrange
        ResourceNotFoundException exception = new ResourceNotFoundException("Test");

        // Assert
        assertTrue(exception instanceof RuntimeException, "Debería ser RuntimeException");
    }

    @Test
    @DisplayName("Constructor con mensaje vacío crea excepción válida")
    void testConstructorWithEmptyMessage() {
        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException("");

        // Assert
        assertEquals("", exception.getMessage(), "El mensaje vacío debería ser conservado");
    }
}
