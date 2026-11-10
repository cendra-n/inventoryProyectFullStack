package gm.inventoryproject.dto.supplier;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para SupplierRequestDto.
 * <p>
 * Verifica las validaciones de Bean Validation utilizando el Validator real.
 * </p>
 */
@DisplayName("Tests de validación de SupplierRequestDto")
class SupplierRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("DTO válido no tiene violaciones de validación")
    void testValidDto() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat SA");
        dto.setEmail("contacto@simeflat.com");
        dto.setPhone("1144151093");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "El DTO válido no debería tener violaciones");
    }

    @Test
    @DisplayName("Nombre nulo genera violación de @NotBlank")
    void testNameNull() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName(null);
        dto.setEmail("valid@email.com");
        dto.setPhone("1144151093");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean nameBlankViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")
                        && v.getMessage().equals("El nombre del proveedor no puede estar vacío"));
        assertTrue(nameBlankViolation, "Debería tener violación de @NotBlank en name");
    }

    @Test
    @DisplayName("Nombre vacío genera violación de @NotBlank")
    void testNameEmpty() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("");
        dto.setEmail("valid@email.com");
        dto.setPhone("1144151093");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean nameBlankViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")
                        && v.getMessage().equals("El nombre del proveedor no puede estar vacío"));
        assertTrue(nameBlankViolation, "Debería tener violación de @NotBlank en name");
    }

    @Test
    @DisplayName("Nombre con solo espacios genera violación de @NotBlank")
    void testNameOnlySpaces() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("   ");
        dto.setEmail("valid@email.com");
        dto.setPhone("1144151093");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean nameBlankViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")
                        && v.getMessage().equals("El nombre del proveedor no puede estar vacío"));
        assertTrue(nameBlankViolation, "Debería tener violación de @NotBlank en name");
    }

    @Test
    @DisplayName("Nombre con menos de 2 caracteres genera violación de @Size")
    void testNameTooShort() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("A");
        dto.setEmail("valid@email.com");
        dto.setPhone("1144151093");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean nameLengthViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")
                        && v.getMessage().equals("El nombre del proveedor debe tener entre 2 y 30 caracteres"));
        assertTrue(nameLengthViolation, "Debería tener violación de @Size en name");
    }

    @Test
    @DisplayName("Nombre con 2 caracteres es válido")
    void testNameMinLengthValid() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("AB");
        dto.setEmail("valid@email.com");
        dto.setPhone("1144151093");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Nombre de longitud mínima válida (2) debería ser aceptado");
    }

    @Test
    @DisplayName("Nombre con 30 caracteres es válido")
    void testNameMaxLengthValid() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("A".repeat(30));
        dto.setEmail("valid@email.com");
        dto.setPhone("1144151093");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Nombre de longitud máxima válida (30) debería ser aceptado");
    }

    @Test
    @DisplayName("Nombre con más de 30 caracteres genera violación de @Size")
    void testNameTooLong() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("A".repeat(31));
        dto.setEmail("valid@email.com");
        dto.setPhone("1144151093");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean nameLengthViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")
                        && v.getMessage().equals("El nombre del proveedor debe tener entre 2 y 30 caracteres"));
        assertTrue(nameLengthViolation, "Debería tener violación de @Size en name");
    }

    @Test
    @DisplayName("Nombre con números genera violación de @Pattern")
    void testNameWithNumbers() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat123");
        dto.setEmail("valid@email.com");
        dto.setPhone("1144151093");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean namePatternViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")
                        && v.getMessage().equals("El nombre solo puede contener letras y espacios"));
        assertTrue(namePatternViolation, "Debería tener violación de @Pattern en name");
    }

    @Test
    @DisplayName("Nombre con caracteres especiales genera violación de @Pattern")
    void testNameWithSpecialCharacters() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat!");
        dto.setEmail("valid@email.com");
        dto.setPhone("1144151093");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean namePatternViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")
                        && v.getMessage().equals("El nombre solo puede contener letras y espacios"));
        assertTrue(namePatternViolation, "Debería tener violación de @Pattern en name");
    }

    @Test
    @DisplayName("Nombre con acentos es válido")
    void testNameWithAccentsValid() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat SÁ");
        dto.setEmail("valid@email.com");
        dto.setPhone("1144151093");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Nombre con acentos debería ser aceptado");
    }

    @Test
    @DisplayName("Email nulo genera violación de @NotBlank")
    void testEmailNull() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat SA");
        dto.setEmail(null);
        dto.setPhone("1144151093");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean emailBlankViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")
                        && v.getMessage().equals("El email no puede estar vacío"));
        assertTrue(emailBlankViolation, "Debería tener violación de @NotBlank en email");
    }

    @Test
    @DisplayName("Email vacío genera violación de @NotBlank")
    void testEmailEmpty() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat SA");
        dto.setEmail("");
        dto.setPhone("1144151093");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean emailBlankViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")
                        && v.getMessage().equals("El email no puede estar vacío"));
        assertTrue(emailBlankViolation, "Debería tener violación de @NotBlank en email");
    }

    @Test
    @DisplayName("Email con formato inválido genera violación de @Pattern")
    void testEmailInvalidFormat() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat SA");
        dto.setEmail("invalid-email");
        dto.setPhone("1144151093");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean emailPatternViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")
                        && v.getMessage().equals("Formato de email inválido"));
        assertTrue(emailPatternViolation, "Debería tener violación de @Pattern en email");
    }

    @Test
    @DisplayName("Email sin @ genera violación de @Pattern")
    void testEmailWithoutAt() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat SA");
        dto.setEmail("invalidemail.com");
        dto.setPhone("1144151093");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean emailPatternViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")
                        && v.getMessage().equals("Formato de email inválido"));
        assertTrue(emailPatternViolation, "Debería tener violación de @Pattern en email");
    }

    @Test
    @DisplayName("Email válido es aceptado")
    void testEmailValidFormat() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat SA");
        dto.setEmail("Joaquingo@simeflat.com");
        dto.setPhone("1144151093");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Email válido debería ser aceptado");
    }

    @Test
    @DisplayName("Teléfono nulo genera violación de @NotBlank")
    void testPhoneNull() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat SA");
        dto.setEmail("valid@email.com");
        dto.setPhone(null);

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean phoneBlankViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("phone")
                        && v.getMessage().equals("El teléfono no puede estar vacío"));
        assertTrue(phoneBlankViolation, "Debería tener violación de @NotBlank en phone");
    }

    @Test
    @DisplayName("Teléfono vacío genera violación de @NotBlank")
    void testPhoneEmpty() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat SA");
        dto.setEmail("valid@email.com");
        dto.setPhone("");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean phoneBlankViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("phone")
                        && v.getMessage().equals("El teléfono no puede estar vacío"));
        assertTrue(phoneBlankViolation, "Debería tener violación de @NotBlank en phone");
    }

    @Test
    @DisplayName("Teléfono con letras genera violación de @Pattern")
    void testPhoneWithLetters() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat SA");
        dto.setEmail("valid@email.com");
        dto.setPhone("1144151093a");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean phonePatternViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("phone")
                        && v.getMessage().equals("El teléfono debe contener solo números (8 a 15 dígitos)"));
        assertTrue(phonePatternViolation, "Debería tener violación de @Pattern en phone");
    }

    @Test
    @DisplayName("Teléfono con caracteres especiales genera violación de @Pattern")
    void testPhoneWithSpecialCharacters() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat SA");
        dto.setEmail("valid@email.com");
        dto.setPhone("1144-1510-93");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean phonePatternViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("phone")
                        && v.getMessage().equals("El teléfono debe contener solo números (8 a 15 dígitos)"));
        assertTrue(phonePatternViolation, "Debería tener violación de @Pattern en phone");
    }

    @Test
    @DisplayName("Teléfono con menos de 8 dígitos genera violación de @Pattern")
    void testPhoneTooShort() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat SA");
        dto.setEmail("valid@email.com");
        dto.setPhone("1234567");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean phonePatternViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("phone")
                        && v.getMessage().equals("El teléfono debe contener solo números (8 a 15 dígitos)"));
        assertTrue(phonePatternViolation, "Debería tener violación de @Pattern en phone");
    }

    @Test
    @DisplayName("Teléfono con 8 dígitos es válido")
    void testPhoneMinLengthValid() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat SA");
        dto.setEmail("valid@email.com");
        dto.setPhone("12345678");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Teléfono de longitud mínima válida (8) debería ser aceptado");
    }

    @Test
    @DisplayName("Teléfono con 15 dígitos es válido")
    void testPhoneMaxLengthValid() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat SA");
        dto.setEmail("valid@email.com");
        dto.setPhone("123456789012345");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Teléfono de longitud máxima válida (15) debería ser aceptado");
    }

    @Test
    @DisplayName("Teléfono con más de 15 dígitos genera violación de @Pattern")
    void testPhoneTooLong() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat SA");
        dto.setEmail("valid@email.com");
        dto.setPhone("1234567890123456");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean phonePatternViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("phone")
                        && v.getMessage().equals("El teléfono debe contener solo números (8 a 15 dígitos)"));
        assertTrue(phonePatternViolation, "Debería tener violación de @Pattern en phone");
    }

    @Test
    @DisplayName("Múltiples violaciones se detectan simultáneamente")
    void testMultipleViolations() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("A");
        dto.setEmail("invalid");
        dto.setPhone("123");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        boolean nameViolationFound = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")
                        && v.getMessage().equals("El nombre del proveedor debe tener entre 2 y 30 caracteres"));
        assertTrue(nameViolationFound, "Debería haber violación de longitud en nombre");

        boolean emailViolationFound = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")
                        && v.getMessage().equals("Formato de email inválido"));
        assertTrue(emailViolationFound, "Debería haber violación de formato en email");

        boolean phoneViolationFound = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("phone")
                        && v.getMessage().equals("El teléfono debe contener solo números (8 a 15 dígitos)"));
        assertTrue(phoneViolationFound, "Debería haber violación de formato en teléfono");
    }

    /**
     * Verifica que los números de teléfono pueden comenzar con cero,
     * lo cual es válido para códigos de área internacionales o locales.
     */
    @Test
    @DisplayName("Teléfono con cero al inicio es válido")
    void testPhoneWithLeadingZero() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat SA");
        dto.setEmail("valid@email.com");
        dto.setPhone("01147335520");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Teléfono con cero al inicio debería ser aceptado");
    }

    /**
     * Verifica que los emails pueden contener el símbolo + y puntos,
     * lo cual es válido según el estándar RFC 5322 para emails.
     */
    @Test
    @DisplayName("Email con + y . es válido")
    void testEmailWithPlusAndDot() {
        // Arrange
        SupplierRequestDto dto = new SupplierRequestDto();
        dto.setName("Simeflat SA");
        dto.setEmail("joaquin.go+simeflat@test.com");
        dto.setPhone("1144151093");

        // Act
        Set<ConstraintViolation<SupplierRequestDto>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty(), "Email con + y . debería ser aceptado");
    }

}
