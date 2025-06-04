package bci.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustom(CustomException ex) {
        Map<String, Object> detalle = new HashMap<>();
        detalle.put("timestamp", Instant.now());
        detalle.put("codigo", ex.getCodigo());
        detalle.put("detail", ex.getMessage());

        Map<String, Object> body = new HashMap<>();
        body.put("error", Arrays.asList(detalle));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}