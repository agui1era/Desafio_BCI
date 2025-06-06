package bci.api.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InvalidEmailExceptionTest {

    @Test
    void testInvalidEmailException() {
        String errorMessage = "El formato del correo electrónico es inválido.";
        InvalidEmailException exception = new InvalidEmailException(errorMessage);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testInvalidEmailException_nullMessage() {
        InvalidEmailException exception = new InvalidEmailException(null);

        assertNotNull(exception);
        assertEquals(null, exception.getMessage());
    }

    @Test
    void testInvalidEmailException_emptyMessage() {
        String errorMessage = "";
        InvalidEmailException exception = new InvalidEmailException(errorMessage);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }
}