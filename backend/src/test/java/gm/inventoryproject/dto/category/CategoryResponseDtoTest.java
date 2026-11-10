package gm.inventoryproject.dto.category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para CategoryResponseDto.
 * <p>
 * Verifica el comportamiento de los getters, setters y constructor
 * utilizando JUnit 5 puro sin dependencias externas.
 * </p>
 */
@DisplayName("Tests de CategoryResponseDto con JUnit puro")
class CategoryResponseDtoTest {

    @Test
    @DisplayName("Constructor con todos los parámetros inicializa correctamente los campos")
    void testConstructorWithAllParameters() {
        // Arrange
        Long expectedId = 1L;
        String expectedName = "Electrónica";
        String expectedDescription = "Dispositivos electrónicos y accesorios";

        // Act
        CategoryResponseDto dto = new CategoryResponseDto(expectedId, expectedName, expectedDescription);

        // Assert
        assertNotNull(dto, "El objeto DTO no debería ser null");
        assertEquals(expectedId, dto.getId(), "El ID debería coincidir con el valor proporcionado");
        assertEquals(expectedName, dto.getName(), "El nombre debería coincidir con el valor proporcionado");
        assertEquals(expectedDescription, dto.getDescription(), "La descripción debería coincidir con el valor proporcionado");
    }

    @Test
    @DisplayName("Setters y getters actualizan y retornan los valores correctamente")
    void testSettersAndGetters() {
        // Arrange
        CategoryResponseDto dto = new CategoryResponseDto(null, null, null);
        Long newId = 2L;
        String newName = "Alimentos";
        String newDescription = "Productos alimenticios";

        // Act
        dto.setId(newId);
        dto.setName(newName);
        dto.setDescription(newDescription);

        // Assert
        assertEquals(newId, dto.getId(), "El ID debería ser actualizado correctamente");
        assertEquals(newName, dto.getName(), "El nombre debería ser actualizado correctamente");
        assertEquals(newDescription, dto.getDescription(), "La descripción debería ser actualizada correctamente");
    }

    @Test
    @DisplayName("Constructor acepta valores null sin lanzar excepción")
    void testConstructorWithNullValues() {
        // Act
        CategoryResponseDto dto = new CategoryResponseDto(null, null, null);

        // Assert
        assertNotNull(dto, "El objeto DTO no debería ser null incluso con valores null");
        assertNull(dto.getId(), "El ID debería ser null");
        assertNull(dto.getName(), "El nombre debería ser null");
        assertNull(dto.getDescription(), "La descripción debería ser null");
    }

    @Test
    @DisplayName("Constructor acepta strings vacíos")
    void testConstructorWithEmptyStrings() {
        // Arrange
        String emptyName = "";
        String emptyDescription = "";

        // Act
        CategoryResponseDto dto = new CategoryResponseDto(1L, emptyName, emptyDescription);

        // Assert
        assertEquals(emptyName, dto.getName(), "Debería aceptar string vacío para nombre");
        assertEquals(emptyDescription, dto.getDescription(), "Debería aceptar string vacío para descripción");
        assertTrue(dto.getName().isEmpty(), "El nombre debería estar vacío");
        assertTrue(dto.getDescription().isEmpty(), "La descripción debería estar vacía");
    }

}
