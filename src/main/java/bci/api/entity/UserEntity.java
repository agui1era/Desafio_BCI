package bci.api.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity // Marca esta clase como una entidad JPA
@Table(name = "users") // Nombre de la tabla en la base de datos
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity { // <-- Nombre de la clase de entidad

    @Id // Marca el campo 'id' como la clave primaria
    @GeneratedValue(strategy = GenerationType.AUTO) // Estrategia de generación automática de UUID
    private UUID id;

    private String nombre; // Campo para el nombre del usuario

    @Column(unique = true, nullable = false) // Email debe ser único y no nulo
    private String email;

    private String password; // Contraseña (encriptada)
    private String token; // Token JWT

    private LocalDateTime created; // Fecha de creación
    private LocalDateTime modified; // Fecha de última modificación
    private LocalDateTime lastLogin; // Fecha del último inicio de sesión
    private boolean active; // Estado activo/inactivo

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneEntity> phones; // Lista de teléfonos asociados
}