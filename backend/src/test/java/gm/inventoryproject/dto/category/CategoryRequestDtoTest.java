package gm.inventoryproject.dto.category;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para CategoryRequestDto.
 * <p>
 * Verifica las validaciones de Bean Validation utilizando el Validator real.
 * </p>
 */
@DisplayName("Tests de validación de CategoryRequestDto")
class CategoryRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("DTO válido no tiene violaciones de validación")
    void testValidDto() {
        // Arrange
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName("Electrónica");
        dto.setDescription("Dispositivos electrónicos y accesorios");

        // Act
        Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "El DTO válido no debería tener violaciones");
    }

    @Test
    @DisplayName("Nombre nulo genera violación de @NotBlank")
    void testNameNull() {
        // Arrange
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName(null);
        dto.setDescription("Descripción válida");

        // Act
        Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<CategoryRequestDto> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("El nombre no puede estar vacío", violation.getMessage());
    }

    @Test
    @DisplayName("Nombre vacío genera violación de @NotBlank")
    void testNameEmpty() {
        // Arrange
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName("");
        dto.setDescription("Descripción válida");

        // Act
        Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean nameBlankViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")
                        && v.getMessage().equals("El nombre no puede estar vacío"));
        assertTrue(nameBlankViolation, "Debería tener violación de @NotBlank en name");
    }

    @Test
    @DisplayName("Nombre con solo espacios genera violación de @NotBlank")
    void testNameOnlySpaces() {
        // Arrange
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName("   ");
        dto.setDescription("Descripción válida");

        // Act
        Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<CategoryRequestDto> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("El nombre no puede estar vacío", violation.getMessage());
    }

    @Test
    @DisplayName("Nombre con menos de 2 caracteres genera violación de @Size")
    void testNameTooShort() {
        // Arrange
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName("A");
        dto.setDescription("Descripción válida");

        // Act
        Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<CategoryRequestDto> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("El nombre debe tener entre 2 y 40 caracteres", violation.getMessage());
    }

    @Test
    @DisplayName("Nombre con 2 caracteres es válido")
    void testNameMinLengthValid() {
        // Arrange
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName("AB");
        dto.setDescription("Descripción válida");

        // Act
        Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Nombre de longitud mínima válida (2) debería ser aceptado");
    }

    @Test
    @DisplayName("Nombre con 40 caracteres es válido")
    void testNameMaxLengthValid() {
        // Arrange
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName("A".repeat(40));
        dto.setDescription("Descripción válida");

        // Act
        Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Nombre de longitud máxima válida (40) debería ser aceptado");
    }

    @Test
    @DisplayName("Nombre con más de 40 caracteres genera violación de @Size")
    void testNameTooLong() {
        // Arrange
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName("A".repeat(41));
        dto.setDescription("Descripción válida");

        // Act
        Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<CategoryRequestDto> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("El nombre debe tener entre 2 y 40 caracteres", violation.getMessage());
    }

    @Test
    @DisplayName("Nombre con números genera violación de @Pattern")
    void testNameWithNumbers() {
        // Arrange
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName("Electrónica123");
        dto.setDescription("Descripción válida");

        // Act
        Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<CategoryRequestDto> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("El nombre solo puede contener letras y espacios", violation.getMessage());
    }

    @Test
    @DisplayName("Nombre con caracteres especiales genera violación de @Pattern")
    void testNameWithSpecialCharacters() {
        // Arrange
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName("Electrónica!");
        dto.setDescription("Descripción válida");

        // Act
        Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<CategoryRequestDto> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("El nombre solo puede contener letras y espacios", violation.getMessage());
    }

    @Test
    @DisplayName("Descripción nula genera violación de @NotBlank")
    void testDescriptionNull() {
        // Arrange
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription(null);

        // Act
        Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<CategoryRequestDto> violation = violations.iterator().next();
        assertEquals("description", violation.getPropertyPath().toString());
        assertEquals("La descripción no puede estar vacía", violation.getMessage());
    }

    @Test
    @DisplayName("Descripción vacía genera violación de @NotBlank")
    void testDescriptionEmpty() {
        // Arrange
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription("");

        // Act
        Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean descBlankViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("description")
                        && v.getMessage().equals("La descripción no puede estar vacía"));
        assertTrue(descBlankViolation, "Debería tener violación de @NotBlank en description");
    }

    @Test
    @DisplayName("Descripción con solo espacios genera violación de @NotBlank")
    void testDescriptionOnlySpaces() {
        // Arrange
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription("   ");

        // Act
        Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<CategoryRequestDto> violation = violations.iterator().next();
        assertEquals("description", violation.getPropertyPath().toString());
        assertEquals("La descripción no puede estar vacía", violation.getMessage());
    }

    @Test
    @DisplayName("Descripción con 200 caracteres es válida")
    void testDescriptionMaxLengthValid() {
        // Arrange
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription("A".repeat(200));

        // Act
        Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Descripción de longitud máxima válida (200) debería ser aceptada");
    }

    @Test
    @DisplayName("Descripción con más de 200 caracteres genera violación de @Size")
    void testDescriptionTooLong() {
        // Arrange
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription("A".repeat(201));

        // Act
        Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<CategoryRequestDto> violation = violations.iterator().next();
        assertEquals("description", violation.getPropertyPath().toString());
        assertEquals("La descripción no puede superar los 200 caracteres", violation.getMessage());
    }

    @Test
    @DisplayName("Descripción con caracteres especiales inválidos genera violación de @Pattern")
    void testDescriptionWithInvalidSpecialCharacters() {
        // Arrange
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription("Descripción con símbolos inválidos !@#$%");

        // Act
        Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<CategoryRequestDto> violation = violations.iterator().next();
        assertEquals("description", violation.getPropertyPath().toString());
        assertEquals("La descripción solo puede contener letras, números, espacios y signos de puntuación básicos (.,;-)", violation.getMessage());
    }

    @Test
    @DisplayName("Descripción con caracteres especiales válidos (.,;-) es aceptada")
    void testDescriptionWithValidSpecialCharacters() {
        // Arrange
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription("Productos, artículos; varios - básicos");

        // Act
        Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Descripción con caracteres permitidos (.,;-) debería ser aceptada");
    }

    @Test
    @DisplayName("Múltiples violaciones se detectan simultáneamente")
    void testMultipleViolations() {
        // Arrange
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName("A");
        dto.setDescription("B");

        // Act
        Set<ConstraintViolation<CategoryRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean nameViolationFound = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")
                        && v.getMessage().equals("El nombre debe tener entre 2 y 40 caracteres"));
        assertTrue(nameViolationFound, "Debería haber violación de longitud en nombre");

        boolean descViolationFound = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("description")
                        && v.getMessage().equals("La descripción no puede superar los 200 caracteres"));
        assertFalse(descViolationFound, "No debería haber violación de longitud en descripción (solo 1 char)");
    }
}
