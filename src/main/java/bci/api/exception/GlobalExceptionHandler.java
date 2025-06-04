package bci.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustom(CustomException ex) {
        Map<String, Object> body = Map.of(
            "error", Collections.singletonList(
                Map.of(
                    "timestamp", Instant.now(),
                    "codigo", ex.getCodigo(),
                    "detail", ex.getMessage()
                )
            )
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
