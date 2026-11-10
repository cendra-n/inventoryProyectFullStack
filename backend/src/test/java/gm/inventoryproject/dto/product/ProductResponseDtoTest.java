package gm.inventoryproject.dto.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para ProductResponseDto.
 * <p>
 * Verifica el comportamiento de los getters, setters y constructor
 * utilizando JUnit 5 puro sin dependencias externas.
 * </p>
 */
@DisplayName("Tests de ProductResponseDto con JUnit puro")
class ProductResponseDtoTest {

    @Test
    @DisplayName("Constructor con todos los parámetros inicializa correctamente los campos")
    void testConstructorWithAllParameters() {
        // Arrange
        Long expectedId = 1L;
        String expectedName = "Botella de agua";
        String expectedDescription = "Botella de agua no gasificada de 500ml";
        Double expectedPrice = 2999.99;
        Integer expectedStock = 15;
        Long expectedCategoryId = 1L;
        Long expectedSupplierId = 4L;
        String expectedCategoryName = "Bebidas";
        String expectedSupplierName = "Simeflat SA";

        // Act
        ProductResponseDto dto = new ProductResponseDto(
                expectedId,
                expectedName,
                expectedDescription,
                expectedPrice,
                expectedStock,
                expectedCategoryId,
                expectedSupplierId,
                expectedCategoryName,
                expectedSupplierName
        );

        // Assert
        assertNotNull(dto, "El objeto DTO no debería ser null");
        assertEquals(expectedId, dto.getId(), "El ID debería coincidir con el valor proporcionado");
        assertEquals(expectedName, dto.getName(), "El nombre debería coincidir con el valor proporcionado");
        assertEquals(expectedDescription, dto.getDescription(), "La descripción debería coincidir con el valor proporcionado");
        assertEquals(expectedPrice, dto.getPrice(), "El precio debería coincidir con el valor proporcionado");
        assertEquals(expectedStock, dto.getStock(), "El stock debería coincidir con el valor proporcionado");
        assertEquals(expectedCategoryId, dto.getCategoryId(), "El ID de categoría debería coincidir con el valor proporcionado");
        assertEquals(expectedSupplierId, dto.getSupplierId(), "El ID de proveedor debería coincidir con el valor proporcionado");
        assertEquals(expectedCategoryName, dto.getCategoryName(), "El nombre de categoría debería coincidir con el valor proporcionado");
        assertEquals(expectedSupplierName, dto.getSupplierName(), "El nombre de proveedor debería coincidir con el valor proporcionado");
    }

    @Test
    @DisplayName("Setters y getters actualizan y retornan los valores correctamente")
    void testSettersAndGetters() {
        // Arrange
        ProductResponseDto dto = new ProductResponseDto(null, null, null, null, null, null, null, null, null);
        Long newId = 2L;
        String newName = "Coca Cola";
        String newDescription = "Bebida gaseosa 500ml";
        Double newPrice = 1500.0;
        Integer newStock = 50;
        Long newCategoryId = 2L;
        Long newSupplierId = 5L;
        String newCategoryName = "Bebidas";
        String newSupplierName = "Coca Cola SA";

        // Act
        dto.setId(newId);
        dto.setName(newName);
        dto.setDescription(newDescription);
        dto.setPrice(newPrice);
        dto.setStock(newStock);
        dto.setCategoryId(newCategoryId);
        dto.setSupplierId(newSupplierId);
        dto.setCategoryName(newCategoryName);
        dto.setSupplierName(newSupplierName);

        // Assert
        assertEquals(newId, dto.getId(), "El ID debería ser actualizado correctamente");
        assertEquals(newName, dto.getName(), "El nombre debería ser actualizado correctamente");
        assertEquals(newDescription, dto.getDescription(), "La descripción debería ser actualizada correctamente");
        assertEquals(newPrice, dto.getPrice(), "El precio debería ser actualizado correctamente");
        assertEquals(newStock, dto.getStock(), "El stock debería ser actualizado correctamente");
        assertEquals(newCategoryId, dto.getCategoryId(), "El ID de categoría debería ser actualizado correctamente");
        assertEquals(newSupplierId, dto.getSupplierId(), "El ID de proveedor debería ser actualizado correctamente");
        assertEquals(newCategoryName, dto.getCategoryName(), "El nombre de categoría debería ser actualizado correctamente");
        assertEquals(newSupplierName, dto.getSupplierName(), "El nombre de proveedor debería ser actualizado correctamente");
    }

    @Test
    @DisplayName("Constructor acepta valores null sin lanzar excepción")
    void testConstructorWithNullValues() {
        // Act
        ProductResponseDto dto = new ProductResponseDto(null, null, null, null, null, null, null, null, null);

        // Assert
        assertNotNull(dto, "El objeto DTO no debería ser null incluso con valores null");
        assertNull(dto.getId(), "El ID debería ser null");
        assertNull(dto.getName(), "El nombre debería ser null");
        assertNull(dto.getDescription(), "La descripción debería ser null");
        assertNull(dto.getPrice(), "El precio debería ser null");
        assertNull(dto.getStock(), "El stock debería ser null");
        assertNull(dto.getCategoryId(), "El ID de categoría debería ser null");
        assertNull(dto.getSupplierId(), "El ID de proveedor debería ser null");
        assertNull(dto.getCategoryName(), "El nombre de categoría debería ser null");
        assertNull(dto.getSupplierName(), "El nombre de proveedor debería ser null");
    }

    @Test
    @DisplayName("Constructor acepta strings vacíos")
    void testConstructorWithEmptyStrings() {
        // Arrange
        String emptyName = "";
        String emptyDescription = "";
        String emptyCategoryName = "";
        String emptySupplierName = "";

        // Act
        ProductResponseDto dto = new ProductResponseDto(
                1L,
                emptyName,
                emptyDescription,
                0.0,
                0,
                1L,
                1L,
                emptyCategoryName,
                emptySupplierName
        );

        // Assert
        assertEquals(emptyName, dto.getName(), "Debería aceptar string vacío para nombre");
        assertEquals(emptyDescription, dto.getDescription(), "Debería aceptar string vacío para descripción");
        assertEquals(emptyCategoryName, dto.getCategoryName(), "Debería aceptar string vacío para nombre de categoría");
        assertEquals(emptySupplierName, dto.getSupplierName(), "Debería aceptar string vacío para nombre de proveedor");
        assertTrue(dto.getName().isEmpty(), "El nombre debería estar vacío");
        assertTrue(dto.getDescription().isEmpty(), "La descripción debería estar vacía");
        assertTrue(dto.getCategoryName().isEmpty(), "El nombre de categoría debería estar vacío");
        assertTrue(dto.getSupplierName().isEmpty(), "El nombre de proveedor debería estar vacío");
    }

    @Test
    @DisplayName("Constructor acepta valores cero para precio y stock")
    void testConstructorWithZeroValues() {
        // Arrange
        Double zeroPrice = 0.0;
        Integer zeroStock = 0;

        // Act
        ProductResponseDto dto = new ProductResponseDto(
                1L,
                "Producto",
                "Descripción",
                zeroPrice,
                zeroStock,
                1L,
                1L,
                "Categoría",
                "Proveedor"
        );

        // Assert
        assertEquals(zeroPrice, dto.getPrice(), "Debería aceptar precio cero");
        assertEquals(zeroStock, dto.getStock(), "Debería aceptar stock cero");
    }

}
