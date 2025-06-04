package bci.api.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final int codigo;

    public CustomException(String mensaje, int codigo) {
        super(mensaje);
        this.codigo = codigo;
    }

}
