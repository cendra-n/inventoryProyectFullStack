package gm.inventoryproject.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para SupplierHasProductsException.
 * <p>
 * Verifica que la excepción conserve el mensaje y sea RuntimeException.
 * </p>
 */
@DisplayName("Tests de SupplierHasProductsException")
class SupplierHasProductsExceptionTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "El proveedor tiene productos asociados",
        "No se puede eliminar el proveedor porque tiene productos",
        "Error: proveedor en uso"
    })
    @DisplayName("Constructor conserva el mensaje de la excepción")
    void testConstructorConservesMessage(String message) {
        // Act
        SupplierHasProductsException exception = new SupplierHasProductsException(message);

        // Assert
        assertEquals(message, exception.getMessage(), "El mensaje debería ser conservado");
    }

    @Test
    @DisplayName("La excepción es RuntimeException")
    void testIsRuntimeException() {
        // Arrange
        SupplierHasProductsException exception = new SupplierHasProductsException("Test");

        // Assert
        assertTrue(exception instanceof RuntimeException, "Debería ser RuntimeException");
    }

    @Test
    @DisplayName("Constructor con mensaje vacío crea excepción válida")
    void testConstructorWithEmptyMessage() {
        // Act
        SupplierHasProductsException exception = new SupplierHasProductsException("");

        // Assert
        assertEquals("", exception.getMessage(), "El mensaje vacío debería ser conservado");
    }
}
