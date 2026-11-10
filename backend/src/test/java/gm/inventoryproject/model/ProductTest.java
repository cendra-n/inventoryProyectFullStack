package gm.inventoryproject.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para Product.
 * <p>
 * Verifica el comportamiento del builder, constructor sin argumentos,
 * getters/setters y valores por defecto utilizando JUnit 5 puro.
 * </p>
 */
class ProductTest {

    @Test
    void testBuilder() {
        // Arrange
        Long expectedId = 1L;
        String expectedName = "Botella de agua";
        String expectedDescription = "Botella de agua no gasificada de 500ml";
        Double expectedPrice = 2999.99;
        Integer expectedStock = 15;
        Category expectedCategory = Category.builder().id(1L).name("Bebidas").build();
        Supplier expectedSupplier = Supplier.builder().id(4L).name("Simeflat SA").build();

        // Act
        Product product = Product.builder()
                .id(expectedId)
                .name(expectedName)
                .description(expectedDescription)
                .price(expectedPrice)
                .stock(expectedStock)
                .category(expectedCategory)
                .supplier(expectedSupplier)
                .build();

        // Assert
        assertAll("product",
                () -> assertEquals(expectedId, product.getId()),
                () -> assertEquals(expectedName, product.getName()),
                () -> assertEquals(expectedDescription, product.getDescription()),
                () -> assertEquals(expectedPrice, product.getPrice()),
                () -> assertEquals(expectedStock, product.getStock()),
                () -> assertEquals(expectedCategory, product.getCategory()),
                () -> assertEquals(expectedSupplier, product.getSupplier())
        );
    }

    @Test
    void testNoArgsConstructor() {
        // Act
        Product product = new Product();

        // Assert
        assertNotNull(product);
        assertNull(product.getId());
        assertNull(product.getName());
        assertNull(product.getDescription());
        assertNull(product.getPrice());
        assertEquals(0, product.getStock(), "El constructor no-args respeta el inicializador stock = 0");
        assertNull(product.getCategory());
        assertNull(product.getSupplier());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        Long expectedId = 2L;
        String expectedName = "Coca Cola";
        String expectedDescription = "Bebida gaseosa 500ml";
        Double expectedPrice = 1500.0;
        Integer expectedStock = 50;
        Category expectedCategory = Category.builder().id(2L).name("Bebidas").build();
        Supplier expectedSupplier = Supplier.builder().id(5L).name("Coca Cola SA").build();

        // Act
        Product product = new Product(
                expectedId,
                expectedName,
                expectedDescription,
                expectedPrice,
                expectedStock,
                expectedCategory,
                expectedSupplier
        );

        // Assert
        assertAll("product",
                () -> assertEquals(expectedId, product.getId()),
                () -> assertEquals(expectedName, product.getName()),
                () -> assertEquals(expectedDescription, product.getDescription()),
                () -> assertEquals(expectedPrice, product.getPrice()),
                () -> assertEquals(expectedStock, product.getStock()),
                () -> assertEquals(expectedCategory, product.getCategory()),
                () -> assertEquals(expectedSupplier, product.getSupplier())
        );
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        Product product = new Product();
        Category category = Category.builder().id(1L).name("Bebidas").build();
        Supplier supplier = Supplier.builder().id(4L).name("Simeflat SA").build();

        // Act
        product.setId(3L);
        product.setName("Fanta");
        product.setDescription("Bebida gaseosa sabor naranja");
        product.setPrice(1200.0);
        product.setStock(30);
        product.setCategory(category);
        product.setSupplier(supplier);

        // Assert
        assertAll("product",
                () -> assertEquals(3L, product.getId()),
                () -> assertEquals("Fanta", product.getName()),
                () -> assertEquals("Bebida gaseosa sabor naranja", product.getDescription()),
                () -> assertEquals(1200.0, product.getPrice()),
                () -> assertEquals(30, product.getStock()),
                () -> assertEquals(category, product.getCategory()),
                () -> assertEquals(supplier, product.getSupplier())
        );
    }

    @Test
    void testBuilderWithoutId() {
        // Arrange
        String expectedName = "Sprite";
        String expectedDescription = "Bebida gaseosa sabor lima-limón";
        Double expectedPrice = 1100.0;
        Integer expectedStock = 25;
        Category expectedCategory = Category.builder().id(3L).name("Bebidas").build();
        Supplier expectedSupplier = Supplier.builder().id(6L).name("Coca Cola SA").build();

        // Act
        Product product = Product.builder()
                .name(expectedName)
                .description(expectedDescription)
                .price(expectedPrice)
                .stock(expectedStock)
                .category(expectedCategory)
                .supplier(expectedSupplier)
                .build();

        // Assert
        assertAll("product",
                () -> assertNull(product.getId()),
                () -> assertEquals(expectedName, product.getName()),
                () -> assertEquals(expectedDescription, product.getDescription()),
                () -> assertEquals(expectedPrice, product.getPrice()),
                () -> assertEquals(expectedStock, product.getStock()),
                () -> assertEquals(expectedCategory, product.getCategory()),
                () -> assertEquals(expectedSupplier, product.getSupplier())
        );
    }

    @Test
    void testBuilderWithOnlyRequiredFields() {
        // Arrange
        String expectedName = "Producto Test";
        Double expectedPrice = 100.0;

        // Act
        Product product = Product.builder()
                .name(expectedName)
                .price(expectedPrice)
                .build();

        // Assert
        assertAll("product",
                () -> assertNull(product.getId()),
                () -> assertEquals(expectedName, product.getName()),
                () -> assertNull(product.getDescription()),
                () -> assertEquals(expectedPrice, product.getPrice()),
                // Nota: stock tiene inicializador = 0 en el modelo, pero @Builder sin @Builder.Default
                // no respeta el inicializador. El builder deja stock como null.
                () -> assertNull(product.getStock()),
                () -> assertNull(product.getCategory()),
                () -> assertNull(product.getSupplier())
        );
    }

    @Test
    void testBuilderStockBehavior() {
        // Arrange & Act
        Product productWithBuilder = Product.builder()
                .name("Test")
                .price(100.0)
                .build();

        Product productWithNew = new Product();

        // Assert
        // Builder sin @Builder.Default no respeta el inicializador del campo (stock = 0)
        assertNull(productWithBuilder.getStock(), "Builder debería dejar stock como null (sin @Builder.Default)");
        // Constructor no-args SÍ respeta el inicializador
        assertEquals(0, productWithNew.getStock(), "Constructor no-args respeta el inicializador stock = 0");
    }

    @Test
    void testBuilderWithExplicitStockZero() {
        // Arrange & Act
        Product product = Product.builder()
                .name("Test")
                .price(100.0)
                .stock(0)
                .build();

        // Assert
        assertEquals(0, product.getStock(), "Stock explícito en cero debería ser respetado");
    }

}
