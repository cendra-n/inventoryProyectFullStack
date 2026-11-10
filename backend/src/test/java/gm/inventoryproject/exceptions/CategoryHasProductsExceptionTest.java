package gm.inventoryproject.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para CategoryHasProductsException.
 * <p>
 * Verifica que la excepción conserve el mensaje y sea RuntimeException.
 * </p>
 */
@DisplayName("Tests de CategoryHasProductsException")
class CategoryHasProductsExceptionTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "La categoría tiene productos asociados",
        "No se puede eliminar la categoría porque tiene productos",
        "Error: categoría en uso"
    })
    @DisplayName("Constructor conserva el mensaje de la excepción")
    void testConstructorConservesMessage(String message) {
        // Act
        CategoryHasProductsException exception = new CategoryHasProductsException(message);

        // Assert
        assertEquals(message, exception.getMessage(), "El mensaje debería ser conservado");
    }

    @Test
    @DisplayName("La excepción es RuntimeException")
    void testIsRuntimeException() {
        // Arrange
        CategoryHasProductsException exception = new CategoryHasProductsException("Test");

        // Assert
        assertTrue(exception instanceof RuntimeException, "Debería ser RuntimeException");
    }

    @Test
    @DisplayName("Constructor con mensaje vacío crea excepción válida")
    void testConstructorWithEmptyMessage() {
        // Act
        CategoryHasProductsException exception = new CategoryHasProductsException("");

        // Assert
        assertEquals("", exception.getMessage(), "El mensaje vacío debería ser conservado");
    }
}
