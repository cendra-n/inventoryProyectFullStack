package gm.inventoryproject.dto.inventorymovement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para InventoryMovementResponseDto.
 * <p>
 * Verifica el comportamiento de los getters, setters y constructor
 * utilizando JUnit 5 puro sin dependencias externas.
 * </p>
 */
@DisplayName("Tests de InventoryMovementResponseDto con JUnit puro")
class InventoryMovementResponseDtoTest {

    @Test
    @DisplayName("Constructor con todos los parámetros inicializa correctamente los campos")
    void testConstructorWithAllParameters() {
        // Arrange
        Long expectedId = 1L;
        Integer expectedQuantity = 10;
        String expectedType = "IN";
        LocalDateTime expectedDate = LocalDateTime.of(2026, 11, 9, 10, 30);
        Long expectedProductId = 3L;
        String expectedProductName = "Botella de agua";

        // Act
        InventoryMovementResponseDto dto = new InventoryMovementResponseDto(
                expectedId,
                expectedQuantity,
                expectedType,
                expectedDate,
                expectedProductId,
                expectedProductName
        );

        // Assert
        assertNotNull(dto, "El objeto DTO no debería ser null");
        assertEquals(expectedId, dto.getId(), "El ID debería coincidir con el valor proporcionado");
        assertEquals(expectedQuantity, dto.getQuantity(), "La cantidad debería coincidir con el valor proporcionado");
        assertEquals(expectedType, dto.getType(), "El tipo debería coincidir con el valor proporcionado");
        assertEquals(expectedDate, dto.getDate(), "La fecha debería coincidir con el valor proporcionado");
        assertEquals(expectedProductId, dto.getProductId(), "El ID de producto debería coincidir con el valor proporcionado");
        assertEquals(expectedProductName, dto.getProductName(), "El nombre de producto debería coincidir con el valor proporcionado");
    }

    @Test
    @DisplayName("Setters y getters actualizan y retornan los valores correctamente")
    void testSettersAndGetters() {
        // Arrange
        InventoryMovementResponseDto dto = new InventoryMovementResponseDto(null, null, null, null, null, null);
        Long newId = 2L;
        Integer newQuantity = 5;
        String newType = "OUT";
        LocalDateTime newDate = LocalDateTime.of(2026, 11, 10, 15, 45);
        Long newProductId = 4L;
        String newProductName = "Coca Cola";

        // Act
        dto.setId(newId);
        dto.setQuantity(newQuantity);
        dto.setType(newType);
        dto.setDate(newDate);
        dto.setProductId(newProductId);
        dto.setProductName(newProductName);

        // Assert
        assertEquals(newId, dto.getId(), "El ID debería ser actualizado correctamente");
        assertEquals(newQuantity, dto.getQuantity(), "La cantidad debería ser actualizada correctamente");
        assertEquals(newType, dto.getType(), "El tipo debería ser actualizado correctamente");
        assertEquals(newDate, dto.getDate(), "La fecha debería ser actualizada correctamente");
        assertEquals(newProductId, dto.getProductId(), "El ID de producto debería ser actualizado correctamente");
        assertEquals(newProductName, dto.getProductName(), "El nombre de producto debería ser actualizado correctamente");
    }

    @Test
    @DisplayName("Constructor acepta valores null sin lanzar excepción")
    void testConstructorWithNullValues() {
        // Act
        InventoryMovementResponseDto dto = new InventoryMovementResponseDto(null, null, null, null, null, null);

        // Assert
        assertNotNull(dto, "El objeto DTO no debería ser null incluso con valores null");
        assertNull(dto.getId(), "El ID debería ser null");
        assertNull(dto.getQuantity(), "La cantidad debería ser null");
        assertNull(dto.getType(), "El tipo debería ser null");
        assertNull(dto.getDate(), "La fecha debería ser null");
        assertNull(dto.getProductId(), "El ID de producto debería ser null");
        assertNull(dto.getProductName(), "El nombre de producto debería ser null");
    }

    @Test
    @DisplayName("Constructor acepta strings vacíos")
    void testConstructorWithEmptyStrings() {
        // Arrange
        String emptyType = "";
        String emptyProductName = "";

        // Act
        InventoryMovementResponseDto dto = new InventoryMovementResponseDto(
                1L,
                10,
                emptyType,
                LocalDateTime.now(),
                3L,
                emptyProductName
        );

        // Assert
        assertEquals(emptyType, dto.getType(), "Debería aceptar string vacío para type");
        assertEquals(emptyProductName, dto.getProductName(), "Debería aceptar string vacío para productName");
        assertTrue(dto.getType().isEmpty(), "El type debería estar vacío");
        assertTrue(dto.getProductName().isEmpty(), "El productName debería estar vacío");
    }

    @Test
    @DisplayName("Constructor acepta cantidad cero")
    void testConstructorWithZeroQuantity() {
        // Arrange
        Integer zeroQuantity = 0;

        // Act
        InventoryMovementResponseDto dto = new InventoryMovementResponseDto(
                1L,
                zeroQuantity,
                "IN",
                LocalDateTime.now(),
                3L,
                "Producto"
        );

        // Assert
        assertEquals(zeroQuantity, dto.getQuantity(), "Debería aceptar cantidad cero");
    }

    @Test
    @DisplayName("Constructor acepta valores IN y OUT para tipo")
    void testTypeValues() {
        // Arrange & Act
        InventoryMovementResponseDto dtoIn = new InventoryMovementResponseDto(1L, 10, "IN", LocalDateTime.now(), 3L, "Producto");
        InventoryMovementResponseDto dtoOut = new InventoryMovementResponseDto(2L, 5, "OUT", LocalDateTime.now(), 4L, "Producto");

        // Assert
        assertEquals("IN", dtoIn.getType(), "Debería aceptar tipo IN");
        assertEquals("OUT", dtoOut.getType(), "Debería aceptar tipo OUT");
    }

}
