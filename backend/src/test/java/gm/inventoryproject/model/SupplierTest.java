package gm.inventoryproject.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para Supplier.
 * <p>
 * Verifica el comportamiento del builder, constructor sin argumentos,
 * getters/setters y valores por defecto utilizando JUnit 5 puro.
 * </p>
 */
class SupplierTest {

    @Test
    void testBuilder() {
        // Arrange
        Long expectedId = 1L;
        String expectedName = "Simeflat SA";
        String expectedEmail = "contacto@simeflat.com";
        String expectedPhone = "1144151093";

        // Act
        Supplier supplier = Supplier.builder()
                .id(expectedId)
                .name(expectedName)
                .email(expectedEmail)
                .phone(expectedPhone)
                .build();

        // Assert
        assertAll("supplier",
                () -> assertEquals(expectedId, supplier.getId()),
                () -> assertEquals(expectedName, supplier.getName()),
                () -> assertEquals(expectedEmail, supplier.getEmail()),
                () -> assertEquals(expectedPhone, supplier.getPhone())
        );
    }

    @Test
    void testNoArgsConstructor() {
        // Act
        Supplier supplier = new Supplier();

        // Assert
        assertNotNull(supplier);
        assertNull(supplier.getId());
        assertNull(supplier.getName());
        assertNull(supplier.getEmail());
        assertNull(supplier.getPhone());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        Long expectedId = 2L;
        String expectedName = "Tech Solutions";
        String expectedPhone = "01147335520";
        String expectedEmail = "info@techsolutions.com";

        // Act
        Supplier supplier = new Supplier(expectedId, expectedName, expectedPhone, expectedEmail);

        // Assert
        assertAll("supplier",
                () -> assertEquals(expectedId, supplier.getId()),
                () -> assertEquals(expectedName, supplier.getName()),
                () -> assertEquals(expectedPhone, supplier.getPhone()),
                () -> assertEquals(expectedEmail, supplier.getEmail())
        );
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        Supplier supplier = new Supplier();

        // Act
        supplier.setId(3L);
        supplier.setName("Global Import");
        supplier.setEmail("sales@globalimport.com");
        supplier.setPhone("02320684512");

        // Assert
        assertAll("supplier",
                () -> assertEquals(3L, supplier.getId()),
                () -> assertEquals("Global Import", supplier.getName()),
                () -> assertEquals("sales@globalimport.com", supplier.getEmail()),
                () -> assertEquals("02320684512", supplier.getPhone())
        );
    }

    @Test
    void testBuilderWithoutId() {
        // Arrange
        String expectedName = "Distribuidora Norte";
        String expectedEmail = "norte@distribuidora.com";
        String expectedPhone = "1155555555";

        // Act
        Supplier supplier = Supplier.builder()
                .name(expectedName)
                .email(expectedEmail)
                .phone(expectedPhone)
                .build();

        // Assert
        assertAll("supplier",
                () -> assertNull(supplier.getId()),
                () -> assertEquals(expectedName, supplier.getName()),
                () -> assertEquals(expectedEmail, supplier.getEmail()),
                () -> assertEquals(expectedPhone, supplier.getPhone())
        );
    }

    @Test
    void testBuilderWithOnlyRequiredFields() {
        // Arrange
        String expectedName = "Proveedor Test";

        // Act
        Supplier supplier = Supplier.builder()
                .name(expectedName)
                .build();

        // Assert
        assertAll("supplier",
                () -> assertNull(supplier.getId()),
                () -> assertEquals(expectedName, supplier.getName()),
                () -> assertNull(supplier.getEmail()),
                () -> assertNull(supplier.getPhone())
        );
    }

}
