package bci.api.dto;

import lombok.Builder; // Para usar el patrón Builder
import lombok.Getter; // Para generar los métodos getter
import lombok.Setter; // Para generar los métodos setter

import java.util.List; // Para la lista de detalles de error

@Getter // Genera automáticamente todos los métodos getter para los campos
@Setter // Genera automáticamente todos los métodos setter para los campos
@Builder // Permite construir instancias de ErrorResponseDTO usando un patrón de construcción fluido
public class ErrorResponseDTO {
    // La especificación pide un arreglo bajo la clave "error": [{ ... }]
    private List<ErrorDetailDTO> error; // Lista de objetos ErrorDetailDTO
}