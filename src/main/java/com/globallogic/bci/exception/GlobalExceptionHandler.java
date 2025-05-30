package main.java.com.globallogic.bci.exception;

import com.globallogic.bci.dto.ErrorDetailDto;
import com.globallogic.bci.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ErrorDetailDto errorDetail = new ErrorDetailDto(LocalDateTime.now(), HttpStatus.CONFLICT.value(), ex.getMessage()); // [cite: 18]
        ErrorResponseDto errorResponse = new ErrorResponseDto();
        errorResponse.setError(Collections.singletonList(errorDetail));
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT); // [cite: 8] appropriate HTTP code
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ErrorDetailDto errorDetail = new ErrorDetailDto(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), errors); // [cite: 18]
        ErrorResponseDto errorResponse = new ErrorResponseDto();
        errorResponse.setError(Collections.singletonList(errorDetail));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // [cite: 8]
    }
    
    // Add more specific exception handlers (e.g., InvalidPasswordFormatException, InvalidEmailFormatException)

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex) {
        ErrorDetailDto errorDetail = new ErrorDetailDto(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + ex.getMessage()); // [cite: 18]
        ErrorResponseDto errorResponse = new ErrorResponseDto();
        errorResponse.setError(Collections.singletonList(errorDetail));
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR); // [cite: 8]
    }
}