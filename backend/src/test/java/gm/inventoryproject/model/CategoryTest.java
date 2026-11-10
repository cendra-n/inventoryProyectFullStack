package gm.inventoryproject.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para Category.
 * <p>
 * Verifica el comportamiento del builder, constructor sin argumentos,
 * getters/setters y valores por defecto utilizando JUnit 5 puro.
 * </p>
 */
@DisplayName("Tests de Category model con JUnit puro")
class CategoryTest {

    @Test
    @DisplayName("Builder crea instancia con todos los campos correctamente")
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
    @DisplayName("Constructor sin argumentos crea instancia con campos null")
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
    @DisplayName("Constructor con todos los argumentos inicializa correctamente los campos")
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
    @DisplayName("Setters y getters actualizan y retornan los valores correctamente")
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
    @DisplayName("Builder sin ID crea instancia con ID null")
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
