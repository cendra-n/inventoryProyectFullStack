package gm.inventoryproject.model;

import org.junit.jupiter.api.DisplayName;
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
@DisplayName("Tests de InventoryMovement model con JUnit puro")
class InventoryMovementTest {

    @Test
    @DisplayName("Builder crea instancia con todos los campos correctamente")
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

    /**
     * Verifica que el constructor no-args respeta el inicializador
     * del campo date = LocalDateTime.now(), mientras que el builder
     * sin @Builder.Default no lo respeta.
     */
    @Test
    @DisplayName("Constructor sin argumentos crea instancia con date inicializado")
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
    @DisplayName("Constructor con todos los argumentos inicializa correctamente los campos")
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
    @DisplayName("Setters y getters actualizan y retornan los valores correctamente")
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
    @DisplayName("Builder sin ID crea instancia con ID null")
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

    /**
     * Verifica que el builder sin @Builder.Default no respeta el inicializador
     * del campo date = LocalDateTime.now(), dejándolo como null cuando no se
     * especifica explícitamente.
     */
    @Test
    @DisplayName("Builder con solo campos requeridos deja date como null")
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

    /**
     * Verifica que el builder sin @Builder.Default no respeta el inicializador
     * del campo (date = LocalDateTime.now()), mientras que el constructor no-args
     * sí lo respeta.
     */
    @Test
    @DisplayName("Builder sin @Builder.Default no respeta inicializador de date")
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
    @DisplayName("Builder con fecha explícita respeta el valor")
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
    @DisplayName("Enum MovementType acepta valores IN y OUT")
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
    @DisplayName("Enum MovementType tiene 2 valores: IN y OUT")
    void testMovementTypeEnumValues() {
        // Assert
        assertEquals(2, InventoryMovement.MovementType.values().length);
        assertEquals("IN", InventoryMovement.MovementType.IN.name());
        assertEquals("OUT", InventoryMovement.MovementType.OUT.name());
    }

}
