package bci.api.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InvalidPasswordExceptionTest {

    @Test
    void testInvalidPasswordException_withMessage() {
        String errorMessage = "La contraseña no cumple con los requisitos.";
        InvalidPasswordException exception = new InvalidPasswordException(errorMessage);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testInvalidPasswordException_withNullMessage() {
        InvalidPasswordException exception = new InvalidPasswordException(null);

        assertNotNull(exception);
        assertEquals(null, exception.getMessage());
    }

    @Test
    void testInvalidPasswordException_withEmptyMessage() {
        String errorMessage = "";
        InvalidPasswordException exception = new InvalidPasswordException(errorMessage);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testInvalidPasswordException_ResponseStatusAnnotation() {
        ResponseStatus responseStatus = InvalidPasswordException.class.getAnnotation(ResponseStatus.class);
        assertNotNull(responseStatus, "La anotación @ResponseStatus debería estar presente.");
        assertEquals(HttpStatus.BAD_REQUEST, responseStatus.value(), "El código de estado HTTP debería ser BAD_REQUEST.");
    }
}