package bci.api.exception;

import bci.api.dto.ErrorDetailDTO;
import bci.api.dto.ErrorResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest mockWebRequest;

    @Mock
    private MethodArgumentNotValidException mockMethodArgumentNotValidException;

    @Mock
    private BindingResult mockBindingResult;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleUserAlreadyExistsException_shouldReturnConflict() {
        // Arrange
        String errorMessage = "Test: El correo ya está registrado";
        UserAlreadyExistsException ex = new UserAlreadyExistsException(errorMessage);

        // Act
        ResponseEntity<ErrorResponseDTO> responseEntity = globalExceptionHandler.handleUserAlreadyExistsException(ex, mockWebRequest);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());

        ErrorResponseDTO errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.getError());
        assertEquals(1, errorResponse.getError().size());

        ErrorDetailDTO errorDetail = errorResponse.getError().get(0);
        assertNotNull(errorDetail.getTimestamp());
        assertEquals(HttpStatus.CONFLICT.value(), errorDetail.getCodigo());
        assertEquals(errorMessage, errorDetail.getDetail());
    }

    @Test
    void handleInvalidEmailException_shouldReturnBadRequest() {
        // Arrange
        String errorMessage = "Test: El formato del correo no es válido";
        InvalidEmailException ex = new InvalidEmailException(errorMessage);

        // Act
        ResponseEntity<ErrorResponseDTO> responseEntity = globalExceptionHandler.handleInvalidEmailException(ex, mockWebRequest);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        ErrorResponseDTO errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.getError());
        assertEquals(1, errorResponse.getError().size());

        ErrorDetailDTO errorDetail = errorResponse.getError().get(0);
        assertNotNull(errorDetail.getTimestamp());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorDetail.getCodigo());
        assertEquals(errorMessage, errorDetail.getDetail());
    }

    @Test
    void handleInvalidPasswordException_shouldReturnBadRequest() {
        // Arrange
        String errorMessage = "Test: El formato de la clave no es válido";
        InvalidPasswordException ex = new InvalidPasswordException(errorMessage);

        // Act
        ResponseEntity<ErrorResponseDTO> responseEntity = globalExceptionHandler.handleInvalidPasswordException(ex, mockWebRequest);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        ErrorResponseDTO errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.getError());
        assertEquals(1, errorResponse.getError().size());

        ErrorDetailDTO errorDetail = errorResponse.getError().get(0);
        assertNotNull(errorDetail.getTimestamp());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorDetail.getCodigo());
        assertEquals(errorMessage, errorDetail.getDetail());
    }

    @Test
    void handleGenericRuntimeException_shouldReturnInternalServerError() {
        // Arrange
        String errorMessage = "Test: Ocurrió un error genérico";
        RuntimeException ex = new RuntimeException(errorMessage);

        // Act
        ResponseEntity<ErrorResponseDTO> responseEntity = globalExceptionHandler.handleGenericRuntimeException(ex, mockWebRequest);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        ErrorResponseDTO errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.getError());
        assertEquals(1, errorResponse.getError().size());

        ErrorDetailDTO errorDetail = errorResponse.getError().get(0);
        assertNotNull(errorDetail.getTimestamp());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorDetail.getCodigo());
        assertEquals("Ha ocurrido un error inesperado: " + errorMessage, errorDetail.getDetail());
    }

    @Test
    void handleValidationExceptions_shouldReturnBadRequestWithFieldErrors() {
        // Arrange
        String fieldErrorMessage = "Error de validación en un campo";
        FieldError fieldError = new FieldError("objectName", "fieldName", fieldErrorMessage);

        when(mockMethodArgumentNotValidException.getBindingResult()).thenReturn(mockBindingResult);
        when(mockBindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        // Act
        ResponseEntity<ErrorResponseDTO> responseEntity = globalExceptionHandler.handleValidationExceptions(mockMethodArgumentNotValidException, mockWebRequest);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        ErrorResponseDTO errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.getError());
        assertEquals(1, errorResponse.getError().size());

        ErrorDetailDTO errorDetail = errorResponse.getError().get(0);
        assertNotNull(errorDetail.getTimestamp());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorDetail.getCodigo());
        assertEquals(fieldErrorMessage, errorDetail.getDetail());
    }

    @Test
    void handleValidationExceptions_shouldReturnBadRequestWithMultipleFieldErrors() {
        // Arrange
        String fieldErrorMessage1 = "Error de validación en campo1";
        String fieldErrorMessage2 = "Error de validación en campo2";
        FieldError fieldError1 = new FieldError("objectName", "fieldName1", fieldErrorMessage1);
        FieldError fieldError2 = new FieldError("objectName", "fieldName2", fieldErrorMessage2);

        when(mockMethodArgumentNotValidException.getBindingResult()).thenReturn(mockBindingResult);
        when(mockBindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        // Act
        ResponseEntity<ErrorResponseDTO> responseEntity = globalExceptionHandler.handleValidationExceptions(mockMethodArgumentNotValidException, mockWebRequest);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        ErrorResponseDTO errorResponse = responseEntity.getBody();
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.getError());
        assertEquals(2, errorResponse.getError().size());

        ErrorDetailDTO errorDetail1 = errorResponse.getError().stream()
                .filter(e -> fieldErrorMessage1.equals(e.getDetail()))
                .findFirst().orElse(null);
        assertNotNull(errorDetail1);
        assertNotNull(errorDetail1.getTimestamp());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorDetail1.getCodigo());

        ErrorDetailDTO errorDetail2 = errorResponse.getError().stream()
                .filter(e -> fieldErrorMessage2.equals(e.getDetail()))
                .findFirst().orElse(null);
        assertNotNull(errorDetail2);
        assertNotNull(errorDetail2.getTimestamp());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorDetail2.getCodigo());
    }
}