package gm.inventoryproject.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para InventoryMovement.
 * <p>
 * Verifica el comportamiento del builder, constructor sin argumentos,
 * getters/setters y valores por defecto utilizando JUnit 5 puro.
 * </p>
 */
class InventoryMovementTest {

    @Test
    void testBuilder() {
        // Arrange
        Long expectedId = 1L;
        Integer expectedQuantity = 10;
        InventoryMovement.MovementType expectedType = InventoryMovement.MovementType.IN;
        LocalDateTime expectedDate = LocalDateTime.of(2026, 11, 9, 10, 30);
        Product expectedProduct = Product.builder().id(3L).name("Botella de agua").build();

        // Act
        InventoryMovement movement = InventoryMovement.builder()
                .id(expectedId)
                .quantity(expectedQuantity)
                .type(expectedType)
                .date(expectedDate)
                .product(expectedProduct)
                .build();

        // Assert
        assertAll("movement",
                () -> assertEquals(expectedId, movement.getId()),
                () -> assertEquals(expectedQuantity, movement.getQuantity()),
                () -> assertEquals(expectedType, movement.getType()),
                () -> assertEquals(expectedDate, movement.getDate()),
                () -> assertEquals(expectedProduct, movement.getProduct())
        );
    }

    @Test
    void testNoArgsConstructor() {
        // Act
        InventoryMovement movement = new InventoryMovement();

        // Assert
        assertNotNull(movement);
        assertNull(movement.getId());
        assertNull(movement.getQuantity());
        assertNull(movement.getType());
        // date tiene inicializador LocalDateTime.now() pero @Builder sin @Builder.Default no lo respeta
        // Constructor no-args SÍ respeta el inicializador
        assertNotNull(movement.getDate(), "El constructor no-args respeta el inicializador date = LocalDateTime.now()");
        assertNull(movement.getProduct());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        Long expectedId = 2L;
        Integer expectedQuantity = 5;
        InventoryMovement.MovementType expectedType = InventoryMovement.MovementType.OUT;
        LocalDateTime expectedDate = LocalDateTime.of(2026, 11, 10, 15, 45);
        Product expectedProduct = Product.builder().id(4L).name("Coca Cola").build();

        // Act
        InventoryMovement movement = new InventoryMovement(
                expectedId,
                expectedQuantity,
                expectedType,
                expectedDate,
                expectedProduct
        );

        // Assert
        assertAll("movement",
                () -> assertEquals(expectedId, movement.getId()),
                () -> assertEquals(expectedQuantity, movement.getQuantity()),
                () -> assertEquals(expectedType, movement.getType()),
                () -> assertEquals(expectedDate, movement.getDate()),
                () -> assertEquals(expectedProduct, movement.getProduct())
        );
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        InventoryMovement movement = new InventoryMovement();
        Product product = Product.builder().id(3L).name("Botella de agua").build();
        LocalDateTime date = LocalDateTime.of(2026, 11, 9, 10, 30);

        // Act
        movement.setId(3L);
        movement.setQuantity(15);
        movement.setType(InventoryMovement.MovementType.IN);
        movement.setDate(date);
        movement.setProduct(product);

        // Assert
        assertAll("movement",
                () -> assertEquals(3L, movement.getId()),
                () -> assertEquals(15, movement.getQuantity()),
                () -> assertEquals(InventoryMovement.MovementType.IN, movement.getType()),
                () -> assertEquals(date, movement.getDate()),
                () -> assertEquals(product, movement.getProduct())
        );
    }

    @Test
    void testBuilderWithoutId() {
        // Arrange
        Integer expectedQuantity = 20;
        InventoryMovement.MovementType expectedType = InventoryMovement.MovementType.IN;
        LocalDateTime expectedDate = LocalDateTime.of(2026, 11, 9, 10, 30);
        Product expectedProduct = Product.builder().id(5L).name("Fanta").build();

        // Act
        InventoryMovement movement = InventoryMovement.builder()
                .quantity(expectedQuantity)
                .type(expectedType)
                .date(expectedDate)
                .product(expectedProduct)
                .build();

        // Assert
        assertAll("movement",
                () -> assertNull(movement.getId()),
                () -> assertEquals(expectedQuantity, movement.getQuantity()),
                () -> assertEquals(expectedType, movement.getType()),
                () -> assertEquals(expectedDate, movement.getDate()),
                () -> assertEquals(expectedProduct, movement.getProduct())
        );
    }

    @Test
    void testBuilderWithOnlyRequiredFields() {
        // Arrange
        Integer expectedQuantity = 10;
        InventoryMovement.MovementType expectedType = InventoryMovement.MovementType.OUT;
        Product expectedProduct = Product.builder().id(3L).name("Botella de agua").build();

        // Act
        InventoryMovement movement = InventoryMovement.builder()
                .quantity(expectedQuantity)
                .type(expectedType)
                .product(expectedProduct)
                .build();

        // Assert
        // Builder sin @Builder.Default no respeta el inicializador date = LocalDateTime.now()
        assertNull(movement.getDate(), "Builder debería dejar date como null (sin @Builder.Default)");
        assertAll("movement",
                () -> assertNull(movement.getId()),
                () -> assertEquals(expectedQuantity, movement.getQuantity()),
                () -> assertEquals(expectedType, movement.getType()),
                () -> assertEquals(expectedProduct, movement.getProduct())
        );
    }

    @Test
    void testBuilderDateBehavior() {
        // Arrange & Act
        InventoryMovement movementWithBuilder = InventoryMovement.builder()
                .quantity(10)
                .type(InventoryMovement.MovementType.IN)
                .product(Product.builder().id(3L).name("Test").build())
                .build();

        InventoryMovement movementWithNew = new InventoryMovement();

        // Assert
        // Builder sin @Builder.Default no respeta el inicializador del campo (date = LocalDateTime.now())
        assertNull(movementWithBuilder.getDate(), "Builder debería dejar date como null (sin @Builder.Default)");
        // Constructor no-args SÍ respeta el inicializador
        assertNotNull(movementWithNew.getDate(), "Constructor no-args respeta el inicializador date = LocalDateTime.now()");
    }

    @Test
    void testBuilderWithExplicitDate() {
        // Arrange & Act
        LocalDateTime explicitDate = LocalDateTime.of(2026, 11, 9, 10, 30);
        InventoryMovement movement = InventoryMovement.builder()
                .quantity(10)
                .type(InventoryMovement.MovementType.IN)
                .product(Product.builder().id(3L).name("Test").build())
                .date(explicitDate)
                .build();

        // Assert
        assertEquals(explicitDate, movement.getDate(), "Fecha explícita debería ser respetada");
    }

    @Test
    void testMovementTypeEnum() {
        // Arrange & Act
        InventoryMovement movementIn = InventoryMovement.builder()
                .quantity(10)
                .type(InventoryMovement.MovementType.IN)
                .build();

        InventoryMovement movementOut = InventoryMovement.builder()
                .quantity(5)
                .type(InventoryMovement.MovementType.OUT)
                .build();

        // Assert
        assertEquals(InventoryMovement.MovementType.IN, movementIn.getType());
        assertEquals(InventoryMovement.MovementType.OUT, movementOut.getType());
    }

    @Test
    void testMovementTypeEnumValues() {
        // Assert
        assertEquals(2, InventoryMovement.MovementType.values().length);
        assertEquals("IN", InventoryMovement.MovementType.IN.name());
        assertEquals("OUT", InventoryMovement.MovementType.OUT.name());
    }

}
