package gm.inventoryproject.dto.inventorymovement;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para InventoryMovementRequestDto.
 * <p>
 * Verifica las validaciones de Bean Validation utilizando el Validator real.
 * </p>
 */
@DisplayName("Tests de validación de InventoryMovementRequestDto")
class InventoryMovementRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("DTO válido tipo IN no tiene violaciones de validación")
    void testValidDto() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(10);
        dto.setType("IN");
        dto.setProductId(3L);

        // Act
        Set<ConstraintViolation<InventoryMovementRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "El DTO válido no debería tener violaciones");
    }

    @Test
    @DisplayName("DTO válido tipo OUT no tiene violaciones de validación")
    void testValidDtoOut() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(5);
        dto.setType("OUT");
        dto.setProductId(4L);

        // Act
        Set<ConstraintViolation<InventoryMovementRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "El DTO válido con tipo OUT no debería tener violaciones");
    }

    // ---------------------------------------------------------
    // QUANTITY VALIDATIONS
    // ---------------------------------------------------------
    @Test
    @DisplayName("Cantidad nula genera violación de @NotNull")
    void testQuantityNull() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(null);
        dto.setType("IN");
        dto.setProductId(1L);

        // Act
        Set<ConstraintViolation<InventoryMovementRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean quantityNotNullViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("quantity")
                        && v.getMessage().equals("La cantidad es obligatoria"));
        assertTrue(quantityNotNullViolation, "Debería tener violación de @NotNull en quantity");
    }

    @Test
    @DisplayName("Cantidad cero genera violación de @Min")
    void testQuantityZero() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(0);
        dto.setType("IN");
        dto.setProductId(1L);

        // Act
        Set<ConstraintViolation<InventoryMovementRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean quantityMinViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("quantity")
                        && v.getMessage().equals("La cantidad debe ser mayor a 0"));
        assertTrue(quantityMinViolation, "Debería tener violación de @Min en quantity");
    }

    @Test
    @DisplayName("Cantidad negativa genera violación de @Min")
    void testQuantityNegative() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(-5);
        dto.setType("IN");
        dto.setProductId(1L);

        // Act
        Set<ConstraintViolation<InventoryMovementRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean quantityMinViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("quantity")
                        && v.getMessage().equals("La cantidad debe ser mayor a 0"));
        assertTrue(quantityMinViolation, "Debería tener violación de @Min en quantity");
    }

    @Test
    @DisplayName("Cantidad positiva mínima (1) es válida")
    void testQuantityPositiveValid() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(1);
        dto.setType("IN");
        dto.setProductId(1L);

        // Act
        Set<ConstraintViolation<InventoryMovementRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Cantidad positiva (1) debería ser aceptada");
    }

    @Test
    @DisplayName("Cantidad grande es válida")
    void testQuantityLargeValid() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(10000);
        dto.setType("IN");
        dto.setProductId(1L);

        // Act
        Set<ConstraintViolation<InventoryMovementRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Cantidad grande debería ser aceptada");
    }

    // ---------------------------------------------------------
    // TYPE VALIDATIONS
    // ---------------------------------------------------------
    @Test
    @DisplayName("Tipo nulo genera violación de @NotNull")
    void testTypeNull() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(10);
        dto.setType(null);
        dto.setProductId(1L);

        // Act
        Set<ConstraintViolation<InventoryMovementRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean typeNotNullViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("type")
                        && v.getMessage().equals("El tipo de movimiento es obligatorio"));
        assertTrue(typeNotNullViolation, "Debería tener violación de @NotNull en type");
    }

    @Test
    @DisplayName("Tipo vacío genera violación de @Pattern")
    void testTypeEmpty() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(10);
        dto.setType("");
        dto.setProductId(1L);

        // Act
        Set<ConstraintViolation<InventoryMovementRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean typePatternViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("type")
                        && v.getMessage().equals("El tipo debe ser IN o OUT"));
        assertTrue(typePatternViolation, "Debería tener violación de @Pattern en type");
    }

    @Test
    @DisplayName("Tipo IN es válido")
    void testTypeInValid() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(10);
        dto.setType("IN");
        dto.setProductId(1L);

        // Act
        Set<ConstraintViolation<InventoryMovementRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Tipo IN debería ser aceptado");
    }

    @Test
    @DisplayName("Tipo OUT es válido")
    void testTypeOutValid() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(10);
        dto.setType("OUT");
        dto.setProductId(1L);

        // Act
        Set<ConstraintViolation<InventoryMovementRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Tipo OUT debería ser aceptado");
    }

    @Test
    @DisplayName("Tipo en minúsculas genera violación de @Pattern (case-sensitive)")
    void testTypeLowerCaseInvalid() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(10);
        dto.setType("in");
        dto.setProductId(1L);

        // Act
        Set<ConstraintViolation<InventoryMovementRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean typePatternViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("type")
                        && v.getMessage().equals("El tipo debe ser IN o OUT"));
        assertTrue(typePatternViolation, "Debería tener violación de @Pattern en type (case-sensitive)");
    }

    @Test
    @DisplayName("Tipo con case mixto genera violación de @Pattern (case-sensitive)")
    void testTypeMixedCaseInvalid() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(10);
        dto.setType("In");
        dto.setProductId(1L);

        // Act
        Set<ConstraintViolation<InventoryMovementRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean typePatternViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("type")
                        && v.getMessage().equals("El tipo debe ser IN o OUT"));
        assertTrue(typePatternViolation, "Debería tener violación de @Pattern en type (case-sensitive)");
    }

    @Test
    @DisplayName("Tipo inválido genera violación de @Pattern")
    void testTypeInvalidValue() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(10);
        dto.setType("OUTSIDE");
        dto.setProductId(1L);

        // Act
        Set<ConstraintViolation<InventoryMovementRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean typePatternViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("type")
                        && v.getMessage().equals("El tipo debe ser IN o OUT"));
        assertTrue(typePatternViolation, "Debería tener violación de @Pattern en type");
    }

    // ---------------------------------------------------------
    // PRODUCT ID VALIDATIONS
    // ---------------------------------------------------------
    @Test
    @DisplayName("ProductId nulo genera violación de @NotNull")
    void testProductIdNull() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(10);
        dto.setType("IN");
        dto.setProductId(null);

        // Act
        Set<ConstraintViolation<InventoryMovementRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean productIdNotNullViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("productId")
                        && v.getMessage().equals("El ID del producto es obligatorio"));
        assertTrue(productIdNotNullViolation, "Debería tener violación de @NotNull en productId");
    }

    @Test
    @DisplayName("ProductId válido es aceptado")
    void testProductIdValid() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(10);
        dto.setType("IN");
        dto.setProductId(1L);

        // Act
        Set<ConstraintViolation<InventoryMovementRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "ProductId válido debería ser aceptado");
    }

    @Test
    @DisplayName("ProductId cero es aceptado (sin @Min ni @Positive)")
    void testProductIdZeroValid() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(10);
        dto.setType("IN");
        dto.setProductId(0L);

        // Act
        Set<ConstraintViolation<InventoryMovementRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "ProductId 0 debería ser aceptado (no hay @Min o @Positive)");
    }

    @Test
    @DisplayName("ProductId negativo es aceptado (sin @Min ni @Positive)")
    void testProductIdNegativeValid() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(10);
        dto.setType("IN");
        dto.setProductId(-1L);

        // Act
        Set<ConstraintViolation<InventoryMovementRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "ProductId negativo debería ser aceptado (no hay @Min o @Positive)");
    }

    // ---------------------------------------------------------
    // MULTIPLE VIOLATIONS
    // ---------------------------------------------------------
    @Test
    @DisplayName("Múltiples campos inválidos generan múltiples violaciones")
    void testMultipleViolations() {
        // Arrange
        InventoryMovementRequestDto dto = new InventoryMovementRequestDto();
        dto.setQuantity(0);
        dto.setType("INVALID");
        dto.setProductId(null);

        // Act
        Set<ConstraintViolation<InventoryMovementRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean quantityViolationFound = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("quantity")
                        && v.getMessage().equals("La cantidad debe ser mayor a 0"));
        assertTrue(quantityViolationFound, "Debería haber violación de @Min en quantity");

        boolean typeViolationFound = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("type")
                        && v.getMessage().equals("El tipo debe ser IN o OUT"));
        assertTrue(typeViolationFound, "Debería haber violación de @Pattern en type");

        boolean productIdViolationFound = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("productId")
                        && v.getMessage().equals("El ID del producto es obligatorio"));
        assertTrue(productIdViolationFound, "Debería haber violación de @NotNull en productId");
    }

}
