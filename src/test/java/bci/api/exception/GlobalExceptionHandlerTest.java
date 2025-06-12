package bci.api.exception;

import bci.api.dto.ErrorDetailDTO;
import bci.api.dto.ErrorResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        // Common setup if needed
    }

    @Test
    void handleUserAlreadyExistsException_shouldReturnConflict() {
        UserAlreadyExistsException ex = new UserAlreadyExistsException("User already exists");
        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleUserAlreadyExistsException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getError().size());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().getError().get(0).getCodigo());
        assertEquals("User already exists", response.getBody().getError().get(0).getDetail());
    }

    @Test
    void handleInvalidEmailException_shouldReturnBadRequest() {
        InvalidEmailException ex = new InvalidEmailException("Invalid email format");
        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleInvalidEmailException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getError().size());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getError().get(0).getCodigo());
        assertEquals("Invalid email format", response.getBody().getError().get(0).getDetail());
    }

    @Test
    void handleInvalidPasswordException_shouldReturnBadRequest() {
        InvalidPasswordException ex = new InvalidPasswordException("Invalid password format");
        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleInvalidPasswordException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getError().size());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getError().get(0).getCodigo());
        assertEquals("Invalid password format", response.getBody().getError().get(0).getDetail());
    }

    @Test
    void handleGenericRuntimeException_shouldReturnInternalServerError() {
        RuntimeException ex = new RuntimeException("Something unexpected happened");
        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleGenericRuntimeException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getError().size());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getError().get(0).getCodigo());
        assertEquals("Ha ocurrido un error inesperado: Something unexpected happened", response.getBody().getError().get(0).getDetail());
    }

    @Test
    void handleValidationExceptions_shouldReturnBadRequest() {
        // Mock MethodArgumentNotValidException
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "fieldName", "default message");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ErrorResponseDTO> response = globalExceptionHandler.handleValidationExceptions(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getError().size());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getError().get(0).getCodigo());
        assertEquals("default message", response.getBody().getError().get(0).getDetail());
    }
}