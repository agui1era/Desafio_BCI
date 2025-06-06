package bci.api.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserAlreadyExistsExceptionTest {

    @Test
    void testUserAlreadyExistsException_withMessage() {
        String errorMessage = "El usuario ya existe.";
        UserAlreadyExistsException exception = new UserAlreadyExistsException(errorMessage);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testUserAlreadyExistsException_withNullMessage() {
        UserAlreadyExistsException exception = new UserAlreadyExistsException(null);

        assertNotNull(exception);
        assertEquals(null, exception.getMessage());
    }

    @Test
    void testUserAlreadyExistsException_withEmptyMessage() {
        String errorMessage = "";
        UserAlreadyExistsException exception = new UserAlreadyExistsException(errorMessage);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testUserAlreadyExistsException_ResponseStatusAnnotation() {
        ResponseStatus responseStatus = UserAlreadyExistsException.class.getAnnotation(ResponseStatus.class);
        assertNotNull(responseStatus, "La anotación @ResponseStatus debería estar presente.");
        assertEquals(HttpStatus.CONFLICT, responseStatus.value(), "El código de estado HTTP debería ser CONFLICT.");
    }
}