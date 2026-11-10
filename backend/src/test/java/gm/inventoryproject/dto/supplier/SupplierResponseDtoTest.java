package gm.inventoryproject.dto.supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para SupplierResponseDto.
 * <p>
 * Verifica el comportamiento de los getters, setters y constructor
 * utilizando JUnit 5 puro sin dependencias externas.
 * </p>
 */
@DisplayName("Tests de SupplierResponseDto con JUnit puro")
class SupplierResponseDtoTest {

    @Test
    @DisplayName("Constructor con todos los parámetros inicializa correctamente los campos")
    void testConstructorWithAllParameters() {
        // Arrange
        Long expectedId = 1L;
        String expectedName = "Simeflat SA";
        String expectedEmail = "contacto@simeflat.com";
        String expectedPhone = "1144151093";

        // Act
        SupplierResponseDto dto = new SupplierResponseDto(expectedId, expectedName, expectedEmail, expectedPhone);

        // Assert
        assertNotNull(dto, "El objeto DTO no debería ser null");
        assertEquals(expectedId, dto.getId(), "El ID debería coincidir con el valor proporcionado");
        assertEquals(expectedName, dto.getName(), "El nombre debería coincidir con el valor proporcionado");
        assertEquals(expectedEmail, dto.getEmail(), "El email debería coincidir con el valor proporcionado");
        assertEquals(expectedPhone, dto.getPhone(), "El teléfono debería coincidir con el valor proporcionado");
    }

    @Test
    @DisplayName("Setters y getters actualizan y retornan los valores correctamente")
    void testSettersAndGetters() {
        // Arrange
        SupplierResponseDto dto = new SupplierResponseDto(null, null, null, null);
        Long newId = 2L;
        String newName = "Tech Solutions";
        String newEmail = "info@techsolutions.com";
        String newPhone = "01147335520";

        // Act
        dto.setId(newId);
        dto.setName(newName);
        dto.setEmail(newEmail);
        dto.setPhone(newPhone);

        // Assert
        assertEquals(newId, dto.getId(), "El ID debería ser actualizado correctamente");
        assertEquals(newName, dto.getName(), "El nombre debería ser actualizado correctamente");
        assertEquals(newEmail, dto.getEmail(), "El email debería ser actualizado correctamente");
        assertEquals(newPhone, dto.getPhone(), "El teléfono debería ser actualizado correctamente");
    }

    @Test
    @DisplayName("Constructor acepta valores null sin lanzar excepción")
    void testConstructorWithNullValues() {
        // Act
        SupplierResponseDto dto = new SupplierResponseDto(null, null, null, null);

        // Assert
        assertNotNull(dto, "El objeto DTO no debería ser null incluso con valores null");
        assertNull(dto.getId(), "El ID debería ser null");
        assertNull(dto.getName(), "El nombre debería ser null");
        assertNull(dto.getEmail(), "El email debería ser null");
        assertNull(dto.getPhone(), "El teléfono debería ser null");
    }

    @Test
    @DisplayName("Constructor acepta strings vacíos")
    void testConstructorWithEmptyStrings() {
        // Arrange
        String emptyName = "";
        String emptyEmail = "";
        String emptyPhone = "";

        // Act
        SupplierResponseDto dto = new SupplierResponseDto(1L, emptyName, emptyEmail, emptyPhone);

        // Assert
        assertEquals(emptyName, dto.getName(), "Debería aceptar string vacío para nombre");
        assertEquals(emptyEmail, dto.getEmail(), "Debería aceptar string vacío para email");
        assertEquals(emptyPhone, dto.getPhone(), "Debería aceptar string vacío para teléfono");
        assertTrue(dto.getName().isEmpty(), "El nombre debería estar vacío");
        assertTrue(dto.getEmail().isEmpty(), "El email debería estar vacío");
        assertTrue(dto.getPhone().isEmpty(), "El teléfono debería estar vacío");
    }

}
