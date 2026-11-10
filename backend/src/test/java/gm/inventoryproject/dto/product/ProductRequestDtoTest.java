package gm.inventoryproject.dto.product;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para ProductRequestDto.
 * <p>
 * Verifica las validaciones de Bean Validation utilizando el Validator real.
 * </p>
 */
@DisplayName("Tests de validación de ProductRequestDto")
class ProductRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("DTO válido no tiene violaciones de validación")
    void testValidDto() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Botella de agua");
        dto.setDescription("Botella de agua no gasificada de 500ml");
        dto.setPrice(2999.99);
        dto.setStock(15);
        dto.setCategoryId(1L);
        dto.setSupplierId(4L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "El DTO válido no debería tener violaciones");
    }

    // ---------------------------------------------------------
    // NAME VALIDATIONS
    // ---------------------------------------------------------
    @Test
    @DisplayName("Nombre nulo genera violación de @NotBlank")
    void testNameNull() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName(null);
        dto.setDescription("Descripción válida");
        dto.setPrice(100.0);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean nameBlankViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")
                        && v.getMessage().equals("El nombre no puede estar vacío"));
        assertTrue(nameBlankViolation, "Debería tener violación de @NotBlank en name");
    }

    @Test
    @DisplayName("Nombre vacío genera violación de @NotBlank")
    void testNameEmpty() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("");
        dto.setDescription("Descripción válida");
        dto.setPrice(100.0);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

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
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("   ");
        dto.setDescription("Descripción válida");
        dto.setPrice(100.0);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean nameBlankViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")
                        && v.getMessage().equals("El nombre no puede estar vacío"));
        assertTrue(nameBlankViolation, "Debería tener violación de @NotBlank en name");
    }

    @Test
    @DisplayName("Nombre con menos de 2 caracteres genera violación de @Size")
    void testNameTooShort() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("A");
        dto.setDescription("Descripción válida");
        dto.setPrice(100.0);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean nameLengthViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")
                        && v.getMessage().equals("El nombre debe tener entre 2 y 40 caracteres"));
        assertTrue(nameLengthViolation, "Debería tener violación de @Size en name");
    }

    @Test
    @DisplayName("Nombre con 2 caracteres es válido")
    void testNameMinLengthValid() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("AB");
        dto.setDescription("Descripción válida");
        dto.setPrice(100.0);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Nombre de longitud mínima válida (2) debería ser aceptado");
    }

    @Test
    @DisplayName("Nombre con 40 caracteres es válido")
    void testNameMaxLengthValid() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("A".repeat(40));
        dto.setDescription("Descripción válida");
        dto.setPrice(100.0);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Nombre de longitud máxima válida (40) debería ser aceptado");
    }

    @Test
    @DisplayName("Nombre con más de 40 caracteres genera violación de @Size")
    void testNameTooLong() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("A".repeat(41));
        dto.setDescription("Descripción válida");
        dto.setPrice(100.0);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean nameLengthViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")
                        && v.getMessage().equals("El nombre debe tener entre 2 y 40 caracteres"));
        assertTrue(nameLengthViolation, "Debería tener violación de @Size en name");
    }

    @Test
    @DisplayName("Nombre con caracteres especiales genera violación de @Pattern")
    void testNameWithSpecialCharacters() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Producto@#$");
        dto.setDescription("Descripción válida");
        dto.setPrice(100.0);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean namePatternViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")
                        && v.getMessage().equals("El nombre solo puede contener letras, números y espacios"));
        assertTrue(namePatternViolation, "Debería tener violación de @Pattern en name");
    }

    @Test
    @DisplayName("Nombre con acentos es válido")
    void testNameWithAccentsValid() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Botella de agua");
        dto.setDescription("Descripción válida");
        dto.setPrice(100.0);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Nombre con acentos debería ser aceptado");
    }

    @Test
    @DisplayName("Nombre con números es válido")
    void testNameWithNumbersValid() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Producto 500ml");
        dto.setDescription("Descripción válida");
        dto.setPrice(100.0);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Nombre con números debería ser aceptado");
    }

    // ---------------------------------------------------------
    // DESCRIPTION VALIDATIONS
    // ---------------------------------------------------------
    @Test
    @DisplayName("Descripción nula genera violación de @NotBlank")
    void testDescriptionNull() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription(null);
        dto.setPrice(100.0);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean descBlankViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("description")
                        && v.getMessage().equals("La descripción no puede estar vacía"));
        assertTrue(descBlankViolation, "Debería tener violación de @NotBlank en description");
    }

    @Test
    @DisplayName("Descripción vacía genera violación de @NotBlank")
    void testDescriptionEmpty() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription("");
        dto.setPrice(100.0);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

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
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription("   ");
        dto.setPrice(100.0);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean descBlankViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("description")
                        && v.getMessage().equals("La descripción no puede estar vacía"));
        assertTrue(descBlankViolation, "Debería tener violación de @NotBlank en description");
    }

    @Test
    @DisplayName("Descripción con 240 caracteres es válida")
    void testDescriptionMaxLengthValid() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription("A".repeat(240));
        dto.setPrice(100.0);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Descripción de longitud máxima válida (240) debería ser aceptada");
    }

    @Test
    @DisplayName("Descripción con más de 240 caracteres genera violación de @Size")
    void testDescriptionTooLong() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription("A".repeat(241));
        dto.setPrice(100.0);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean descLengthViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("description")
                        && v.getMessage().equals("La descripción no puede superar los 240 caracteres"));
        assertTrue(descLengthViolation, "Debería tener violación de @Size en description");
    }

    @Test
    @DisplayName("Descripción con caracteres especiales inválidos genera violación de @Pattern")
    void testDescriptionWithInvalidSpecialCharacters() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription("Descripción con símbolos inválidos !@#$%");
        dto.setPrice(100.0);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean descPatternViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("description")
                        && v.getMessage().equals("La descripción solo puede contener letras, números, espacios y signos de puntuación básicos (.,;-)"));
        assertTrue(descPatternViolation, "Debería tener violación de @Pattern en description");
    }

    @Test
    @DisplayName("Descripción con caracteres especiales válidos es aceptada")
    void testDescriptionWithValidSpecialCharacters() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription("Productos, artículos; varios - básicos");
        dto.setPrice(100.0);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Descripción con caracteres permitidos (.,;-) debería ser aceptada");
    }

    // ---------------------------------------------------------
    // PRICE VALIDATIONS
    // ---------------------------------------------------------
    @Test
    @DisplayName("Precio nulo genera violación de @NotNull")
    void testPriceNull() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription("Descripción válida");
        dto.setPrice(null);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean priceNotNullViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("price")
                        && v.getMessage().equals("El precio es obligatorio"));
        assertTrue(priceNotNullViolation, "Debería tener violación de @NotNull en price");
    }

    @Test
    @DisplayName("Precio cero genera violación de @Positive")
    void testPriceZero() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription("Descripción válida");
        dto.setPrice(0.0);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean pricePositiveViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("price")
                        && v.getMessage().equals("El precio debe ser mayor que 0"));
        assertTrue(pricePositiveViolation, "Debería tener violación de @Positive en price");
    }

    @Test
    @DisplayName("Precio negativo genera violación de @Positive")
    void testPriceNegative() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription("Descripción válida");
        dto.setPrice(-100.0);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean pricePositiveViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("price")
                        && v.getMessage().equals("El precio debe ser mayor que 0"));
        assertTrue(pricePositiveViolation, "Debería tener violación de @Positive en price");
    }

    @Test
    @DisplayName("Precio positivo es válido")
    void testPricePositiveValid() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription("Descripción válida");
        dto.setPrice(100.0);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Precio positivo debería ser aceptado");
    }

    // ---------------------------------------------------------
    // STOCK VALIDATIONS
    // ---------------------------------------------------------
    @Test
    @DisplayName("Stock nulo genera violación de @NotNull")
    void testStockNull() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription("Descripción válida");
        dto.setPrice(100.0);
        dto.setStock(null);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean stockNotNullViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("stock")
                        && v.getMessage().equals("El stock es obligatorio"));
        assertTrue(stockNotNullViolation, "Debería tener violación de @NotNull en stock");
    }

    @Test
    @DisplayName("Stock negativo genera violación de @PositiveOrZero")
    void testStockNegative() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription("Descripción válida");
        dto.setPrice(100.0);
        dto.setStock(-5);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean stockPositiveOrZeroViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("stock")
                        && v.getMessage().equals("El stock no puede ser negativo"));
        assertTrue(stockPositiveOrZeroViolation, "Debería tener violación de @PositiveOrZero en stock");
    }

    @Test
    @DisplayName("Stock cero es válido")
    void testStockZeroValid() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription("Descripción válida");
        dto.setPrice(100.0);
        dto.setStock(0);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Stock cero debería ser aceptado");
    }

    @Test
    @DisplayName("Stock positivo es válido")
    void testStockPositiveValid() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription("Descripción válida");
        dto.setPrice(100.0);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Stock positivo debería ser aceptado");
    }

    // ---------------------------------------------------------
    // CATEGORY ID VALIDATIONS
    // ---------------------------------------------------------
    @Test
    @DisplayName("CategoryId nulo genera violación de @NotNull")
    void testCategoryIdNull() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription("Descripción válida");
        dto.setPrice(100.0);
        dto.setStock(10);
        dto.setCategoryId(null);
        dto.setSupplierId(1L);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean categoryIdNotNullViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("categoryId")
                        && v.getMessage().equals("La categoría es obligatoria"));
        assertTrue(categoryIdNotNullViolation, "Debería tener violación de @NotNull en categoryId");
    }

    // ---------------------------------------------------------
    // SUPPLIER ID VALIDATIONS
    // ---------------------------------------------------------
    @Test
    @DisplayName("SupplierId nulo genera violación de @NotNull")
    void testSupplierIdNull() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Nombre válido");
        dto.setDescription("Descripción válida");
        dto.setPrice(100.0);
        dto.setStock(10);
        dto.setCategoryId(1L);
        dto.setSupplierId(null);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean supplierIdNotNullViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("supplierId")
                        && v.getMessage().equals("El proveedor es obligatorio"));
        assertTrue(supplierIdNotNullViolation, "Debería tener violación de @NotNull en supplierId");
    }

    // ---------------------------------------------------------
    // MULTIPLE VIOLATIONS
    // ---------------------------------------------------------
    @Test
    @DisplayName("Múltiples campos inválidos generan múltiples violaciones")
    void testMultipleViolations() {
        // Arrange
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("A");
        dto.setDescription("B");
        dto.setPrice(0.0);
        dto.setStock(-1);
        dto.setCategoryId(null);
        dto.setSupplierId(null);

        // Act
        Set<ConstraintViolation<ProductRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean nameViolationFound = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")
                        && v.getMessage().equals("El nombre debe tener entre 2 y 40 caracteres"));
        assertTrue(nameViolationFound, "Debería haber violación de longitud en nombre");

        boolean priceViolationFound = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("price")
                        && v.getMessage().equals("El precio debe ser mayor que 0"));
        assertTrue(priceViolationFound, "Debería haber violación de @Positive en precio");

        boolean stockViolationFound = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("stock")
                        && v.getMessage().equals("El stock no puede ser negativo"));
        assertTrue(stockViolationFound, "Debería haber violación de @PositiveOrZero en stock");

        boolean categoryIdViolationFound = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("categoryId")
                        && v.getMessage().equals("La categoría es obligatoria"));
        assertTrue(categoryIdViolationFound, "Debería haber violación de @NotNull en categoryId");

        boolean supplierIdViolationFound = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("supplierId")
                        && v.getMessage().equals("El proveedor es obligatorio"));
        assertTrue(supplierIdViolationFound, "Debería haber violación de @NotNull en supplierId");
    }

}
