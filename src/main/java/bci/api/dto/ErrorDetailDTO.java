package bci.api.dto;

import lombok.Builder; // Para usar el patrón Builder
import lombok.Getter; // Para generar los métodos getter
import lombok.Setter; // Para generar los métodos setter

import java.time.LocalDateTime; // Para el timestamp del error

@Getter // Genera automáticamente todos los métodos getter para los campos
@Setter // Genera automáticamente todos los métodos setter para los campos
@Builder // Permite construir instancias de ErrorDetailDTO usando un patrón de construcción fluido
public class ErrorDetailDTO {
    private LocalDateTime timestamp; // Marca de tiempo del error
    private int codigo; // Código numérico del error (ej. código HTTP)
    private String detail; // Mensaje detallado del error
}