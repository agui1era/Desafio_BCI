package bci.api.exception;

import bci.api.dto.ErrorDetailDTO;
import bci.api.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
        ErrorDetailDTO errorDetail = ErrorDetailDTO.builder()
                .timestamp(LocalDateTime.now())
                .codigo(HttpStatus.CONFLICT.value())
                .detail(ex.getMessage())
                .build();
        return new ResponseEntity<>(ErrorResponseDTO.builder().error(Collections.singletonList(errorDetail)).build(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidEmailException(InvalidEmailException ex, WebRequest request) {
        ErrorDetailDTO errorDetail = ErrorDetailDTO.builder()
                .timestamp(LocalDateTime.now())
                .codigo(HttpStatus.BAD_REQUEST.value())
                .detail(ex.getMessage())
                .build();
        return new ResponseEntity<>(ErrorResponseDTO.builder().error(Collections.singletonList(errorDetail)).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidPasswordException(InvalidPasswordException ex, WebRequest request) {
        ErrorDetailDTO errorDetail = ErrorDetailDTO.builder()
                .timestamp(LocalDateTime.now())
                .codigo(HttpStatus.BAD_REQUEST.value())
                .detail(ex.getMessage())
                .build();
        return new ResponseEntity<>(ErrorResponseDTO.builder().error(Collections.singletonList(errorDetail)).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericRuntimeException(RuntimeException ex, WebRequest request) {
        ErrorDetailDTO errorDetail = ErrorDetailDTO.builder()
                .timestamp(LocalDateTime.now())
                .codigo(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .detail("Ha ocurrido un error inesperado: " + ex.getMessage())
                .build();
        return new ResponseEntity<>(ErrorResponseDTO.builder().error(Collections.singletonList(errorDetail)).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        List<ErrorDetailDTO> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> ErrorDetailDTO.builder()
                        .timestamp(LocalDateTime.now())
                        .codigo(HttpStatus.BAD_REQUEST.value())
                        .detail(error.getDefaultMessage()) // Mensaje por defecto de la validación
                        .build())
                .collect(Collectors.toList());

        return new ResponseEntity<>(ErrorResponseDTO.builder().error(errors).build(), HttpStatus.BAD_REQUEST);
    }

}