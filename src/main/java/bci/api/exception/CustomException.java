package bci.api.exception;

public class CustomException extends RuntimeException {
    private final int codigo;

    public CustomException(String mensaje, int codigo) {
        super(mensaje);
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}
