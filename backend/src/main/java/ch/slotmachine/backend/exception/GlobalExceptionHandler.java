package ch.slotmachine.backend.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global Exception Handler - Zentrale Fehlerbehandlung
 * 
 * Diese Klasse fängt alle Exceptions in der Anwendung ab und
 * wandelt sie in saubere HTTP-Responses um.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handler für RuntimeException (z.B. Player not found)
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> error = Map.of(
            "error", ex.getMessage(),
            "status", HttpStatus.BAD_REQUEST.value(),
            "timestamp", System.currentTimeMillis()
        );
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Handler für IllegalArgumentException (z.B. negative Coins)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> error = Map.of(
            "error", ex.getMessage(),
            "status", HttpStatus.BAD_REQUEST.value(),
            "timestamp", System.currentTimeMillis()
        );
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Handler für alle anderen Exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> error = Map.of(
            "error", "Internal server error",
            "details", ex.getMessage(),
            "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "timestamp", System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
