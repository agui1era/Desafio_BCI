package bci.api.dto;

import lombok.Builder; // Para usar el patrón Builder
import lombok.Getter; // Para generar los métodos getter
import lombok.Setter; // Para generar los métodos setter
import java.time.LocalDateTime; // Para los campos de fecha y hora
import java.util.List; // Para la lista de teléfonos

@Getter // Genera automáticamente todos los métodos getter para los campos
@Setter // Genera automáticamente todos los métodos setter para los campos
@Builder // Permite construir instancias de UserResponseDTO usando un patrón de construcción fluido (ej: UserResponseDTO.builder().id(...).build())
public class UserResponseDTO {
    private String id; // ID del usuario (UUID convertido a String)
    private LocalDateTime created; // Fecha y hora de creación del usuario
    private LocalDateTime lastLogin; // Fecha y hora del último inicio de sesión
    private String token; // Token de acceso JWT
    private boolean isActive; // Indica si el usuario está activo en el sistema
    private String nombre; // Nombre del usuario (corresponde a 'name' en la petición de entrada y 'nombre' en la entidad)
    private String email; // Correo electrónico del usuario
    // private String password; // No se incluye la contraseña (ni siquiera el hash) en la respuesta por seguridad.
    private List<PhoneDTO> phones; // Lista de teléfonos asociados al usuario, mapeados a PhoneDTO
}