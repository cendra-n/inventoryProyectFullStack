package gm.inventoryproject.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para Category.
 * <p>
 * Verifica el comportamiento del builder, constructor sin argumentos,
 * getters/setters y valores por defecto utilizando JUnit 5 puro.
 * </p>
 */
class CategoryTest {

    @Test
    void testBuilder() {
        // Arrange
        Long expectedId = 1L;
        String expectedName = "Electrónica";
        String expectedDescription = "Dispositivos electrónicos";

        // Act
        Category category = Category.builder()
                .id(expectedId)
                .name(expectedName)
                .description(expectedDescription)
                .build();

        // Assert
        assertAll("category",
                () -> assertEquals(expectedId, category.getId()),
                () -> assertEquals(expectedName, category.getName()),
                () -> assertEquals(expectedDescription, category.getDescription())
        );
    }

    @Test
    void testNoArgsConstructor() {
        // Act
        Category category = new Category();

        // Assert
        assertNotNull(category);
        assertNull(category.getId());
        assertNull(category.getName());
        assertNull(category.getDescription());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        Long expectedId = 2L;
        String expectedName = "Alimentos";
        String expectedDescription = "Productos alimenticios";

        // Act
        Category category = new Category(expectedId, expectedName, expectedDescription);

        // Assert
        assertAll("category",
                () -> assertEquals(expectedId, category.getId()),
                () -> assertEquals(expectedName, category.getName()),
                () -> assertEquals(expectedDescription, category.getDescription())
        );
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        Category category = new Category();

        // Act
        category.setId(3L);
        category.setName("Ropa");
        category.setDescription("Prendas de vestir");

        // Assert
        assertAll("category",
                () -> assertEquals(3L, category.getId()),
                () -> assertEquals("Ropa", category.getName()),
                () -> assertEquals("Prendas de vestir", category.getDescription())
        );
    }

    @Test
    void testBuilderWithoutId() {
        // Arrange
        String expectedName = "Hogar";
        String expectedDescription = "Artículos para el hogar";

        // Act
        Category category = Category.builder()
                .name(expectedName)
                .description(expectedDescription)
                .build();

        // Assert
        assertAll("category",
                () -> assertNull(category.getId()),
                () -> assertEquals(expectedName, category.getName()),
                () -> assertEquals(expectedDescription, category.getDescription())
        );
    }
}
