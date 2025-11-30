package gm.inventoryproject.exceptions;

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
