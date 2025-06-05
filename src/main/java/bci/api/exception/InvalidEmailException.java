package bci.api.exception;

import org.springframework.http.HttpStatus; // Importa HttpStatus para el código de estado HTTP
import org.springframework.web.bind.annotation.ResponseStatus; // Importa ResponseStatus para mapear la excepción a un estado HTTP

@ResponseStatus(HttpStatus.BAD_REQUEST) // <-- Mapea esta excepción a un código HTTP 400 Bad Request
public class InvalidEmailException extends RuntimeException { // Extiende RuntimeException para ser una excepción no chequeada
    public InvalidEmailException(String message) {
        super(message); // Llama al constructor de la clase padre (RuntimeException) con el mensaje de error
    }
}