package gm.inventoryproject.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // -----------------------------------------------------------
    // 1) ERROR DE CAMPOS DUPLICADOS - custom (email, phone, name)
    // -----------------------------------------------------------
    @ExceptionHandler(DuplicateFieldException.class)
    public ResponseEntity<?> handleDuplicate(DuplicateFieldException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // -----------------------------------------------------------
    // 2) ERROR DE VIOLACIÓN DE CONSTRAINTS EN BD (unique)
    // -----------------------------------------------------------
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleSQLDuplicate(DataIntegrityViolationException ex) {

        String message = "Error en los datos enviados.";

        if (ex.getMessage().toLowerCase().contains("email")) {
            message = "El email ya está registrado.";
        } else if (ex.getMessage().toLowerCase().contains("phone")) {
            message = "El teléfono ya está registrado.";
        } else if (ex.getMessage().toLowerCase().contains("name")) {
            message = "El nombre ya está registrado.";
        }

        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    // -----------------------------------------------------------
    // 3) VALIDACIÓN DE CAMPOS @Valid  (400)
    // -----------------------------------------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
        );

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("messages", errors);

        return ResponseEntity.badRequest().body(body);
    }

    // -----------------------------------------------------------
    // 4) ENTIDAD NO ENCONTRADA (404)
    // -----------------------------------------------------------
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // -----------------------------------------------------------
    // 5) ERROR GENERAL (500)
    //  ERRORES DE VALIDACIÓN DE LÓGICA DE NEGOCIO (400)
    //    Ej: stock insuficiente, cantidad inválida, tipo OUT incorrecto, etc.
    // -----------------------------------------------------------
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    //------------------------------------------------
    // 6) ERROR POR ENVIO DE NÚMEROS CON COMA EL INGRESO/EGRESO
    // EJ: out : 2,30
    //--------------------------------

    @ExceptionHandler(com.fasterxml.jackson.databind.exc.InvalidFormatException.class)
    public ResponseEntity<?> handleInvalidFormat(InvalidFormatException ex) {

        String field = "valor enviado";

        if (!ex.getPath().isEmpty()) {
            field = ex.getPath().get(0).getFieldName();
        }

        String message = "El campo '" + field + "' solo acepta números enteros. No se permiten comas ni decimales.";

        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    //-------------------------------------------------
    // 7) ERROR 409 NO SE PUEDE ELIMINAR CATEGORIA CON PRODUCTO ASOCIADO
    //-----------------------------------------------
    @ExceptionHandler(CategoryHasProductsException.class)
    public ResponseEntity<String> handleCategoryHasProducts(
            CategoryHasProductsException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    //-------------------------------------------------
    // 8) ERROR 409 NO SE PUEDE ELIMINAR PROVEEDOR CON PRODUCTO ASOCIADO
    //
    @ExceptionHandler(SupplierHasProductsException.class)
    public ResponseEntity<String> handleSupplierHasProducts(
             SupplierHasProductsException ex)
    {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    // -----------------------------------------------------------
    // 9) ERROR GENERAL (500)
    // -----------------------------------------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error inesperado. Por favor, contacte al administrador.");
    }


    // -----------------------------------------------------------
    // UTILIDAD PARA FORMATEAR RESPUESTAS
    // -----------------------------------------------------------
    private ResponseEntity<?> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        return ResponseEntity.status(status).body(body);
    }
}
