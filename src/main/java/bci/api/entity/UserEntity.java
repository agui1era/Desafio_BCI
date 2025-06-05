package bci.api.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users") // Good practice to explicitly name table, avoid conflicts with SQL 'user' keyword
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEnitty {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String nombre; // Coincide con request.getName()

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String token;

    private LocalDateTime created;
    private LocalDateTime modified;
    private LocalDateTime lastLogin; // Es LocalDateTime
    private boolean active;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneEntity> phones; // Referencia a PhoneEntity
}