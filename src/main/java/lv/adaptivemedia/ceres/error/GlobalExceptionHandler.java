package lv.adaptivemedia.ceres.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

import static java.util.Map.entry;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BackendTechnicalException.class)
    public ResponseEntity<Object> handleBackendException(BackendTechnicalException ex, HttpServletRequest request) {
        final var response = Map.ofEntries(
                entry("timestamp", LocalDateTime.now()),
                entry("status", ex.getStatusCode().value()),
                entry("statusMessage", ex.getStatusCode()),
                entry("error", ex.getLocalizedMessage()),
                entry("path", request.getRequestURI())
        );
        return ResponseEntity
                .status(HttpStatusCode.valueOf(ex.getStatusCode().value()))
                .body(response);
    }
}