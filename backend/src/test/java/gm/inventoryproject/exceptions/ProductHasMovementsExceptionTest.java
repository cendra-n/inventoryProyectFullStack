package gm.inventoryproject.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para ProductHasMovementsException.
 * <p>
 * Verifica que la excepción conserve el mensaje y sea RuntimeException.
 * </p>
 */
@DisplayName("Tests de ProductHasMovementsException")
class ProductHasMovementsExceptionTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "No se puede eliminar un producto con movimientos de inventario",
        "El producto tiene movimientos asociados",
        "Error: producto con historial de movimientos"
    })
    @DisplayName("Constructor conserva el mensaje de la excepción")
    void testConstructorConservesMessage(String message) {
        // Act
        ProductHasMovementsException exception = new ProductHasMovementsException(message);

        // Assert
        assertEquals(message, exception.getMessage(), "El mensaje debería ser conservado");
    }

    @Test
    @DisplayName("La excepción es RuntimeException")
    void testIsRuntimeException() {
        // Arrange
        ProductHasMovementsException exception = new ProductHasMovementsException("Test");

        // Assert
        assertTrue(exception instanceof RuntimeException, "Debería ser RuntimeException");
    }

    @Test
    @DisplayName("Constructor con mensaje vacío crea excepción válida")
    void testConstructorWithEmptyMessage() {
        // Act
        ProductHasMovementsException exception = new ProductHasMovementsException("");

        // Assert
        assertEquals("", exception.getMessage(), "El mensaje vacío debería ser conservado");
    }
}
