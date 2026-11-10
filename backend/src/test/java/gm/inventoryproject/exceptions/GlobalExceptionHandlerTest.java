package gm.inventoryproject.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para GlobalExceptionHandler.
 * <p>
 * Verifica el comportamiento de cada handler instanciando el handler con new
 * y comprobando el status HTTP y el cuerpo de la respuesta actual.
 * </p>
 */
@DisplayName("Tests de GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    // -----------------------------------------------------------
    // 1) ERROR DE CAMPOS DUPLICADOS - custom (email, phone, name)
    // -----------------------------------------------------------

    @Test
    @DisplayName("DuplicateFieldException retorna 400 con formato buildResponse")
    void testHandleDuplicate() {
        // Arrange
        DuplicateFieldException ex = new DuplicateFieldException("El email ya está registrado");

        // Act
        ResponseEntity<?> response = handler.handleDuplicate(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("timestamp"));
        assertEquals(400, body.get("status"));
        assertEquals("Bad Request", body.get("error"));
        assertEquals("El email ya está registrado", body.get("message"));
    }

    // -----------------------------------------------------------
    // 2) ERROR DE VIOLACIÓN DE CONSTRAINTS EN BD (unique)
    // -----------------------------------------------------------

    @Test
    @DisplayName("DataIntegrityViolationException con email retorna mensaje específico")
    void testHandleSQLDuplicateWithEmail() {
        // Arrange
        DataIntegrityViolationException ex = new DataIntegrityViolationException(
            "Duplicate entry 'test@example.com' for key 'supplier.email'"
        );

        // Act
        ResponseEntity<?> response = handler.handleSQLDuplicate(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("El email ya está registrado.", body.get("message"));
    }

    @Test
    @DisplayName("DataIntegrityViolationException con phone retorna mensaje específico")
    void testHandleSQLDuplicateWithPhone() {
        // Arrange
        DataIntegrityViolationException ex = new DataIntegrityViolationException(
            "Duplicate entry '1234567890' for key 'supplier.phone'"
        );

        // Act
        ResponseEntity<?> response = handler.handleSQLDuplicate(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("El teléfono ya está registrado.", body.get("message"));
    }

    @Test
    @DisplayName("DataIntegrityViolationException con name retorna mensaje específico")
    void testHandleSQLDuplicateWithName() {
        // Arrange
        DataIntegrityViolationException ex = new DataIntegrityViolationException(
            "Duplicate entry 'Test Category' for key 'category.name'"
        );

        // Act
        ResponseEntity<?> response = handler.handleSQLDuplicate(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("El nombre ya está registrado.", body.get("message"));
    }

    @Test
    @DisplayName("DataIntegrityViolationException sin palabra clave retorna mensaje genérico")
    void testHandleSQLDuplicateDefault() {
        // Arrange
        DataIntegrityViolationException ex = new DataIntegrityViolationException(
            "Duplicate entry for key 'unknown'"
        );

        // Act
        ResponseEntity<?> response = handler.handleSQLDuplicate(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals("Error en los datos enviados.", body.get("message"));
    }

    // -----------------------------------------------------------
    // 3) VALIDACIÓN DE CAMPOS @Valid  (400)
    // -----------------------------------------------------------

    @Test
    @DisplayName("MethodArgumentNotValidException retorna 400 con clave messages")
    void testHandleValidationErrors() {
        // Arrange
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(null, "testObject");
        bindingResult.addError(new FieldError("testObject", "name", "no puede estar vacío"));
        bindingResult.addError(new FieldError("testObject", "email", "formato inválido"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        // Act
        ResponseEntity<?> response = handler.handleValidationErrors(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("timestamp"));
        assertEquals(400, body.get("status"));
        assertEquals("Bad Request", body.get("error"));
        assertTrue(body.containsKey("messages"));
        
        @SuppressWarnings("unchecked")
        Map<String, String> messages = (Map<String, String>) body.get("messages");
        assertEquals("no puede estar vacío", messages.get("name"));
        assertEquals("formato inválido", messages.get("email"));
    }

    // -----------------------------------------------------------
    // 4) ENTIDAD NO ENCONTRADA (404)
    // -----------------------------------------------------------

    @Test
    @DisplayName("ResourceNotFoundException retorna 404")
    void testHandleNotFound() {
        // Arrange
        ResourceNotFoundException ex = new ResourceNotFoundException("No se encontró un producto con ID: 1");

        // Act
        ResponseEntity<?> response = handler.handleNotFound(ex);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(404, body.get("status"));
        assertEquals("Not Found", body.get("error"));
        assertEquals("No se encontró un producto con ID: 1", body.get("message"));
    }

    // -----------------------------------------------------------
    // 5) ERROR GENERAL (500) / ERRORES DE VALIDACIÓN DE LÓGICA DE NEGOCIO (400)
    // -----------------------------------------------------------

    @Test
    @DisplayName("IllegalArgumentException retorna 400")
    void testHandleIllegalArgument() {
        // Arrange
        IllegalArgumentException ex = new IllegalArgumentException("Stock insuficiente");

        // Act
        ResponseEntity<?> response = handler.handleIllegalArgument(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(400, body.get("status"));
        assertEquals("Bad Request", body.get("error"));
        assertEquals("Stock insuficiente", body.get("message"));
    }

    // -----------------------------------------------------------
    // 6) ERROR POR ENVIO DE NÚMEROS CON COMA EL INGRESO/EGRESO
    // -----------------------------------------------------------

    /**
     * Verifica que InvalidFormatException con path no vacío incluye el nombre del campo
     * en el mensaje de error.
     */
    @Test
    @DisplayName("InvalidFormatException con path incluye nombre del campo")
    void testHandleInvalidFormatWithPath() throws JsonMappingException {
        // Arrange
        JsonMappingException.Reference ref = new JsonMappingException.Reference(new Object(), "quantity");
        InvalidFormatException ex = new InvalidFormatException(null, "Cannot deserialize", "2,30", Integer.class);
        ex.prependPath(ref);

        // Act
        ResponseEntity<?> response = handler.handleInvalidFormat(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(400, body.get("status"));
        assertEquals("Bad Request", body.get("error"));
        String message = (String) body.get("message");
        assertTrue(message.contains("quantity"), "El mensaje debería incluir el nombre del campo");
        assertTrue(message.contains("solo acepta números enteros"));
    }

    /**
     * Verifica que InvalidFormatException con path vacío usa "valor enviado"
     * en lugar del nombre del campo.
     */
    @Test
    @DisplayName("InvalidFormatException con path vacío usa 'valor enviado'")
    void testHandleInvalidFormatWithoutPath() {
        // Arrange
        InvalidFormatException ex = new InvalidFormatException(null, "Cannot deserialize", "2,30", Integer.class);

        // Act
        ResponseEntity<?> response = handler.handleInvalidFormat(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(400, body.get("status"));
        assertEquals("Bad Request", body.get("error"));
        String message = (String) body.get("message");
        assertTrue(message.contains("valor enviado"), "El mensaje debería usar 'valor enviado'");
        assertTrue(message.contains("solo acepta números enteros"));
    }

    // -----------------------------------------------------------
    // 7) ERROR 409 NO SE PUEDE ELIMINAR CATEGORIA CON PRODUCTO ASOCIADO
    // -----------------------------------------------------------

    /**
     * Verifica que CategoryHasProductsException retorna 409 con cuerpo String plano.
     */
    @Test
    @DisplayName("CategoryHasProductsException retorna 409 con cuerpo String plano")
    void testHandleCategoryHasProducts() {
        // Arrange
        CategoryHasProductsException ex = new CategoryHasProductsException(
            "La categoría tiene productos asociados"
        );

        // Act
        ResponseEntity<String> response = handler.handleCategoryHasProducts(ex);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("La categoría tiene productos asociados", response.getBody());
    }

    // -----------------------------------------------------------
    // 8) ERROR 409 NO SE PUEDE ELIMINAR PROVEEDOR CON PRODUCTO ASOCIADO
    // -----------------------------------------------------------

    /**
     * Verifica que SupplierHasProductsException retorna 409 con cuerpo String plano.
     */
    @Test
    @DisplayName("SupplierHasProductsException retorna 409 con cuerpo String plano")
    void testHandleSupplierHasProducts() {
        // Arrange
        SupplierHasProductsException ex = new SupplierHasProductsException(
            "El proveedor tiene productos asociados"
        );

        // Act
        ResponseEntity<String> response = handler.handleSupplierHasProducts(ex);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("El proveedor tiene productos asociados", response.getBody());
    }

    // -----------------------------------------------------------
    // 9) ERROR 409 NO SE PERMITE BORRAR UN PRODUCTO CON MOVIMIENTO DE INVENTARIO
    // -----------------------------------------------------------

    /**
     * Verifica que ProductHasMovementsException retorna 409 con Map con clave "error".
     * Este formato es distinto al de CategoryHasProductsException y SupplierHasProductsException,
     * que retornan String plano.
     */
    @Test
    @DisplayName("ProductHasMovementsException retorna 409 con Map con clave error")
    void testHandleProductHasMovements() {
        // Arrange
        ProductHasMovementsException ex = new ProductHasMovementsException(
            "No se puede eliminar un producto con movimientos de inventario"
        );

        // Act
        ResponseEntity<Map<String, String>> response = handler.handleProductHasMovements(ex);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<String, String> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.containsKey("error"));
        assertEquals("No se puede eliminar un producto con movimientos de inventario", body.get("error"));
    }

    // -----------------------------------------------------------
    // 10) ERROR GENERAL (500)
    // -----------------------------------------------------------

    @Test
    @DisplayName("Exception genérica retorna 500 con mensaje genérico")
    void testHandleGeneral() {
        // Arrange
        Exception ex = new Exception("Error inesperado");

        // Act
        ResponseEntity<?> response = handler.handleGeneral(ex);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertNotNull(body);
        assertEquals(500, body.get("status"));
        assertEquals("Internal Server Error", body.get("error"));
        assertEquals("Ocurrió un error inesperado. Por favor, contacte al administrador.", body.get("message"));
    }
}
