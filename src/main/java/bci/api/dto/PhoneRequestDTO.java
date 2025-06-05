package bci.api.dto;

import lombok.Getter; // Para generar los métodos getter
import lombok.Setter; // Para generar los métodos setter

@Getter // Genera automáticamente todos los métodos getter para los campos
@Setter // Genera automáticamente todos los métodos setter para los campos
public class PhoneRequestDTO {
    private String number; // Número de teléfono
    private String citycode; // Código de ciudad
    private String contrycode; // Código de país
}